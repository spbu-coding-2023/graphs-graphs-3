package model.graph

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class DirectedGraphTest {
    val graph = DirectedGraph()

    fun Graph.findVertex(key: Int) = this.vertices.find { it.key == key }
    fun Graph.findEdge(key1: Int, key2: Int) = this.adjacencyList[findVertex(key1)]?.find { it.second.key == key2 }

    fun Graph.checkExistingDirectedEdge(key1: Int, key2: Int) {
        val edge1 = this.findEdge(key1, key2)

        assertNotNull(edge1)
        assertEquals(edge1.weight, 1)
    }

    @Nested
    inner class addEdge {
        @Test
        fun `Not linked vertices`() {
            val vertex1 = graph.addVertex(1)
            val vertex2 = graph.addVertex(2)

            assertNotNull(vertex1)
            assertNotNull(vertex2)

            val edge = graph.addEdge(vertex1.key, vertex2.key)

            assertNotNull(edge)
            graph.checkExistingDirectedEdge(1, 2)
        }

        @Test
        fun `Already linked vertices`() {
            val vertex1 = graph.addVertex(1)
            val vertex2 = graph.addVertex(2)

            assertNotNull(vertex1)
            assertNotNull(vertex2)

            graph.addEdge(vertex1.key, vertex2.key)
            val edgeLinkedVertices = graph.addEdge(vertex1.key, vertex2.key)

            assertNull(edgeLinkedVertices)
            graph.checkExistingDirectedEdge(1, 2)
        }

        @Test
        fun `Edge with non existing vertex`() {
            val vertex = graph.addVertex(1)

            val edgeFirstNotExist = graph.addEdge(2, 1)
            val edgeSecondNotExist = graph.addEdge(1, 2)
            val edgeAllNotExist = graph.addEdge(3, 4)

            assertNull(edgeFirstNotExist)
            assertNull(edgeSecondNotExist)
            assertNull(edgeAllNotExist)

            assertEquals(graph.adjacencyList[vertex]?.size, 0)
        }

        @Test
        fun `Edge with identical vertices`() {
            val vertex1 = graph.addVertex(1)
            val vertex2 = graph.addVertex(2)

            graph.addEdge(2, 1)
            val edgeNotExist = graph.addEdge(1, 1)

            assertNull(edgeNotExist)

            assertEquals(graph.adjacencyList[vertex2]?.size, 1)
            assertEquals(graph.adjacencyList[vertex1]?.size, 0)

        }

        @Test
        fun `Identical edge`() {
            val vertex1 = graph.addVertex(1)
            val vertex2 = graph.addVertex(2)

            graph.addEdge(2, 1)
            val edgeNotExist = graph.addEdge(2, 1)

            assertNull(edgeNotExist)

            assertEquals(graph.adjacencyList[vertex2]?.size, 1)
            assertEquals(graph.adjacencyList[vertex1]?.size, 0)

        }
    }
}