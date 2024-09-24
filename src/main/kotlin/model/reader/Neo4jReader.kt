package model.reader

import model.graph.*
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import org.neo4j.driver.Transaction

class Neo4jReader(uri: String, user: String, password: String) : Reader {

    private val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
    private val session = driver.session()

    private fun createNode(node: Vertex, graphName: String, txInput: Transaction?) {
        val tx = txInput ?: session.beginTransaction()

        tx.run(
            "MERGE (n:Node {graphName: \$graphName, key: \$key})",
            mapOf("key" to node.key, "graphName" to graphName)
        )

        if (txInput == null) {
            tx.commit()
            tx.close()
        }
    }

    private fun createEdge(edge: Edge, nameGraph: String, txInput: Transaction?) {
        val tx = txInput ?: session.beginTransaction()

        tx.run(
            "MERGE (v1:Node {graphName: \$graphName, key: \$key1})" +
                    "MERGE (v2:Node {graphName: \$graphName, key: \$key2})" +
                    "MERGE (v1)-[:DIRECTED_TO {weight: \$weight}]->(v2)",
            mapOf(
                "key1" to edge.first.key,
                "key2" to edge.second.key,
                "weight" to edge.weight,
                "graphName" to nameGraph
            )
        )

        if (txInput == null) {
            tx.commit()
            tx.close()
        }
    }

    private fun deleteGraph(graphName: String, txInput: Transaction?) {
        val tx = txInput ?: session.beginTransaction()

        tx.run(
            "MATCH (n:Node {graphName: \$graphName}) DETACH DELETE n",
            mapOf(
                "graphName" to graphName
            )
        )
        tx.run(
            "MATCH (g:Graph {graphName: \$graphName}) DETACH DELETE g",
            mapOf(
                "graphName" to graphName
            )
        )

        if (txInput == null) {
            tx.commit()
            tx.close()
        }
    }

    /**
     * Save graph to Neo4j Database
     */
    override fun saveGraph(graph: Graph, filepath: String, nameGraph: String) {
        val transaction = session.beginTransaction()

        deleteGraph(nameGraph, transaction)

        val graphType: String = when (graph) {
            is WeightedDirectedGraph -> "WeightedUndirected"
            is WeightedGraph -> "Weighted"
            is DirectedGraph -> "Directed"
            else -> "Undirected"
        }

        transaction.run(
            "MERGE (g:Graph {graphName: \$graphName, type: \$graphType})",
            mapOf(
                "graphName" to nameGraph,
                "graphType" to graphType
            )
        )

        graph.vertices.forEach { v ->
            createNode(v, nameGraph, transaction)

            graph.adjacencyList[v]?.forEach { e ->
                createEdge(e, nameGraph, transaction)
            }
        }

        transaction.commit()
        transaction.close()
    }

    /**
     * Load graph to Neo4j Database
     *
     * @return the loaded graph
     * @throws NoSuchRecordException if there is no graph with given graph name
     */
    override fun loadGraph(filepath: String, nameGraph: String): Graph {
        var graph: Graph = UndirectedGraph()

        session.executeRead { tx ->
            val graphType =
                tx.run("MATCH (g:Graph {graphName: \$graphName}) return g", mapOf("graphName" to nameGraph)).single()
                    .get("g").get("type").asString()

            graph = when (graphType) {
                "Undirected" -> UndirectedGraph()
                "Directed" -> DirectedGraph()
                "Weighted" -> WeightedGraph()
                else -> WeightedDirectedGraph()
            }

            tx.run("MATCH (n:Node {graphName: \$graphName}) return n", mapOf("graphName" to nameGraph))
                .forEach { v -> graph.addVertex((v.get("n").get("key").asInt())) }

            tx.run(
                "MATCH p=(v1: Node {graphName: \$graphName})-[r]-(v2: Node {graphName: \$graphName}) return v1, v2, r",
                mapOf("graphName" to nameGraph)
            ).forEach { v ->
                val values = v.values()
                graph.addEdge(
                    values[0].get("key").asInt(),
                    values[1].get("key").asInt(),
                    values[2].get("weight").asLong()
                )
            }
        }

        return graph
    }
}