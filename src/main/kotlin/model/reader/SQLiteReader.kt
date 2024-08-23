package model.reader

import model.graph.*

import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement

class SQLiteReader: Reader {

    private fun createTable(connection: Connection) {
        val statement = connection.createStatement()
        val createTableVertex = """
        CREATE TABLE IF NOT EXISTS vertex (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            vertex_key INTEGER,
            graph_id INTEGER,
            FOREIGN KEY (graph_id) REFERENCES graph(graph_id)
        )
    """
        val createTableEdge = """
        CREATE TABLE IF NOT EXISTS edge (
            start_vertex_id INTEGER,
            end_vertex_id INTEGER,
            weight INTEGER,
            FOREIGN KEY (end_vertex_id) REFERENCES graph(graph_id),
            FOREIGN KEY (start_vertex_id) REFERENCES graph(graph_id)
        )
    """
        val createTableGraph = """
        CREATE TABLE IF NOT EXISTS graph (
            graph_id INTEGER PRIMARY KEY AUTOINCREMENT,
            graph_name TEXT NOT NULL UNIQUE,
            graph_type_flag INTEGER
        )
    """
        statement.execute(createTableGraph)
        statement.execute(createTableVertex)
        statement.execute(createTableEdge)
        statement.close()
    }

    private fun insertGraph(connect: Connection, graph: Graph, nameGraph: String){

        val insertName = "INSERT INTO graph (graph_name, graph_type_flag) VALUES (?, ?)"
        val insertNameStmt: PreparedStatement = connect.prepareStatement(insertName)
        insertNameStmt.setString(1, nameGraph)

        if (graph is WeightedGraph){
            insertNameStmt.setInt(2, 1)
        }
        if (graph is UndirectedGraph){
            insertNameStmt.setInt(2, 2)
        }
        if (graph is DirectedGraph){
            insertNameStmt.setInt(2, 3)
        }
        if (graph is WeightedDirectedGraph){
            insertNameStmt.setInt(2, 4)
        }

        insertNameStmt.executeUpdate()

        val graphId = insertNameStmt.generatedKeys.getInt(1)
        insertNameStmt.close()

        val insertVertexSql = "INSERT INTO vertex (vertex_key, graph_id) VALUES (?, ?)"
        val insertVertexStmt: PreparedStatement = connect.prepareStatement(insertVertexSql)

        val vertexIdMap = mutableMapOf<Vertex, Int>()

        for (vertex in graph.vertices){
            insertVertexStmt.setInt(1, vertex.key)
            insertVertexStmt.setInt(2, graphId)

            insertVertexStmt.executeUpdate()

            val vertexIdResult = insertVertexStmt.generatedKeys
            if (vertexIdResult.next()) {
                val vertexId = vertexIdResult.getInt(1)
                vertexIdMap[vertex] = vertexId
            }
        }
        insertVertexStmt.close()

        val insertEdgeSql = "INSERT INTO edge (start_vertex_id, end_vertex_id, weight) VALUES (?, ?, ?)"
        val insertEdgeStmt: PreparedStatement = connect.prepareStatement(insertEdgeSql)

        for ((vertex, edges) in graph.adjacencyList) {
            val startVertexId = vertexIdMap[vertex] ?: throw Exception("Vertex not found in vertexIdMap")
            for (edge in edges) {
                val endVertexId = vertexIdMap[edge.second] ?: throw Exception("End vertex not found in vertexIdMap")
                insertEdgeStmt.setInt(1, startVertexId)
                insertEdgeStmt.setInt(2, endVertexId)
                insertEdgeStmt.setLong(3, edge.weight)
                insertEdgeStmt.executeUpdate()
            }
        }
        insertEdgeStmt.close()
    }

    private fun connect(filepath: String): Connection = DriverManager.getConnection("jdbc:sqlite:$filepath")


    override fun saveGraph(graph: Graph, filepath: String, nameGraph: String) {

        //Сконектились с базой
        val connection = connect(filepath)

        //Создали таблицы и связи между ними
        createTable(connection)

        //Сохранили граф по полочкам:)
        insertGraph(connection, graph, nameGraph)
    }

    override fun loadGraph(filepath: String, nameGraph: String): Graph {

        //Сконектились с базой
        val connection = connect(filepath)

        val graph: Graph

        //Сделали запрос на получение id графа
        val graphStmt = connection.prepareStatement(
            "SELECT graph_id, graph_type_flag FROM graph WHERE graph_name = ?"
        )

        graphStmt.setString(1, nameGraph)
        val graphResultSet = graphStmt.executeQuery()

        if (!graphResultSet.next()) {
            throw IllegalArgumentException("Graph with name $nameGraph not found")
        }

        val graphId = graphResultSet.getInt("graph_id")
        val graphType = graphResultSet.getInt("graph_type_flag")

        graph = when (graphType) {
            1 -> WeightedGraph()
            2 -> UndirectedGraph()
            3 -> DirectedGraph()
            4 -> WeightedDirectedGraph()
            else -> throw IllegalArgumentException("Unknown graph type: $graphType")
        }

        graphResultSet.close()
        graphStmt.close()

        //Сделали запрос на получение id и ключа вершины
        val vertexStmt = connection.prepareStatement(
            "SELECT id, vertex_key FROM vertex WHERE graph_id = ?"
        )

        vertexStmt.setInt(1, graphId)
        val vertexResultSet = vertexStmt.executeQuery()

        // Нужна для нахождение вершин ребра через их id
        val vertexMap = mutableMapOf<Int, Vertex>()

        while (vertexResultSet.next()){
            val vertexId = vertexResultSet.getInt("id")
            val vertexKey = vertexResultSet.getInt("vertex_key")
            val vertex = graph.addVertex(vertexKey)

            if (vertex != null){
                vertexMap[vertexId] = vertex
            }
        }

        vertexResultSet.close()
        vertexStmt.close()

        /*
        Сделали запрос на получение id начальной и конечной вершины, а также веса, ребра,
         через id вершины полученной от graph_id
        */
        val edgeStmt = connection.prepareStatement(
            "SELECT start_vertex_id, end_vertex_id, weight FROM edge WHERE start_vertex_id" +
                    " IN (SELECT id FROM vertex WHERE graph_id = ?)"
        )

        edgeStmt.setInt(1, graphId)
        val edgeResultSet = edgeStmt.executeQuery()

        while (edgeResultSet.next()) {
            val startVertexId = edgeResultSet.getInt("start_vertex_id")
            val endVertexId = edgeResultSet.getInt("end_vertex_id")
            val weight = edgeResultSet.getLong("weight")

            val startVertex = vertexMap[startVertexId]
            val endVertex = vertexMap[endVertexId]

            if (startVertex != null && endVertex != null) {
                graph.addEdge(startVertex.key, endVertex.key, weight)
            }
        }
        edgeResultSet.close()
        edgeStmt.close()

        connection.close()
        return graph
    }
}


