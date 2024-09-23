package model.reader

import model.algorithm.Dijkstra
import model.graph.Graph
import model.graph.UndirectedGraph
import model.graph.Vertex
import model.graph.WeightedGraph
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.neo4j.driver.exceptions.NoSuchRecordException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Neo4jReaderTest {
    lateinit var testGraph: Graph
    val neo4jReader = Neo4jReader("bolt://localhost:7687", "neo4j", "qwertyui")
    val testGraphName = "testGraph"

    private fun isSameNodes(graph1: Graph, graph2: Graph): Boolean =
        graph1.vertices.sortedBy { v -> v.key } == graph2.vertices.sortedBy { v -> v.key }

    private fun isSameEdges(graph1: Graph, graph2: Graph): Boolean {
        listOf(graph1, graph2).forEach { graph ->
            graph.vertices.forEach { v ->
                val graph1EdgeNodesWithWeights =
                    graph1.adjacencyList[v]?.map { edge -> Pair(edge.second, edge.weight) }
                        ?.sortedBy { node -> node.first.key }

                val graph2EdgeNodesWithWeights =
                    graph2.adjacencyList[v]?.map { edge -> Pair(edge.second, edge.weight) }
                        ?.sortedBy { node -> node.first.key }

                if (graph1EdgeNodesWithWeights != graph2EdgeNodesWithWeights) return false
            }
        }

        return true
    }

    @Nested
    inner class `Save and load graph` {
        @BeforeEach
        fun setup() {
            testGraph = WeightedGraph()
        }

        @Test
        fun `save and load empty graph one time`() {
            neo4jReader.saveGraph(testGraph, "", testGraphName)
            val graph = neo4jReader.loadGraph("", testGraphName)

            assertEquals(graph.vertices.size, testGraph.vertices.size)
            assertEquals(graph.adjacencyList.values.size, testGraph.adjacencyList.size)
        }

        @Test
        fun `save and load empty graph 100 times in a row`() {
            var graph: Graph = testGraph

            for (i in 1..100) {
                neo4jReader.saveGraph(testGraph, "", testGraphName)
                graph = neo4jReader.loadGraph("", testGraphName)
            }

            assertEquals(graph.vertices.size, testGraph.vertices.size)
            assertEquals(graph.adjacencyList.values.size, testGraph.adjacencyList.size)
        }

        @Test
        fun `save and load non-empty graph one time`() {
            for (i in 1..5) {
                testGraph.addVertex(i)
            }
            testGraph.addEdge(1, 2, 2)
            testGraph.addEdge(2, 5, 4)
            testGraph.addEdge(1, 4, 4)
            testGraph.addEdge(4, 2, 1)
            testGraph.addEdge(1, 3, 3)
            testGraph.addEdge(4, 5, 1)
            testGraph.addEdge(3, 5, 5)

            neo4jReader.saveGraph(testGraph, "", testGraphName)
            val graph = neo4jReader.loadGraph("", testGraphName)

            assertEquals(graph.vertices.size, testGraph.vertices.size)
            assertEquals(graph.adjacencyList.size, testGraph.adjacencyList.size)

            assertTrue(isSameNodes(graph, testGraph))
            assertTrue(isSameEdges(graph, testGraph))
        }

        @Test
        fun `save and load non-empty graph 100 times in a row`() {
            for (i in 1..5) {
                testGraph.addVertex(i)
            }
            testGraph.addEdge(1, 2, 2)
            testGraph.addEdge(2, 5, 4)
            testGraph.addEdge(1, 4, 4)
            testGraph.addEdge(4, 2, 1)
            testGraph.addEdge(1, 3, 3)
            testGraph.addEdge(4, 5, 1)
            testGraph.addEdge(3, 5, 5)

            var graph = testGraph
            for (i in 1..100) {
                neo4jReader.saveGraph(testGraph, "", testGraphName)
                graph = neo4jReader.loadGraph("", testGraphName)

            }
            assertEquals(graph.vertices.size, testGraph.vertices.size)
            assertEquals(graph.adjacencyList.size, testGraph.adjacencyList.size)

            assertTrue(isSameNodes(graph, testGraph))
            assertTrue(isSameEdges(graph, testGraph))
        }

        @Test
        fun `load graph that don't exist in DB`() {
            try {
                neo4jReader.loadGraph("", "Homka")
            } catch (_: NoSuchRecordException) {

            }
        }
    }
}