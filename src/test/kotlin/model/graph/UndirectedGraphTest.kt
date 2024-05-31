package model.graph

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@Suppress("ClassName")
class UndirectedGraphTest {
    lateinit var graph: UndirectedGraph

    fun Graph.getSize() = this.vertices.size
    fun Graph.findVertex(key: Int) = this.vertices.find { it.key == key }
    fun Graph.findEdge(key1: Int, key2: Int) = this.adjacencyList[findVertex(key1)]?.find { it.second.key == key2 }

    fun Graph.checkSize(size: Int) = assertEquals(this.getSize(), size)
    fun Graph.checkContainVertex(vertex: Vertex) = assertEquals(this.findVertex(vertex.key), vertex)
    fun Graph.checkNotContainVertex(vertex: Vertex) = assertEquals(this.findVertex(vertex.key), null)
    fun Graph.checkNotNullEdgeArray(vertex: Vertex) = assertNotNull(this.adjacencyList[vertex])
    fun Graph.checkNullEdgeArray(vertex: Vertex) = assertNull(this.adjacencyList[vertex])

    fun Graph.checkExistingUndirectedEdge(key1: Int, key2: Int) {
        val edge1 = this.findEdge(key1, key2)
        val edge2 = this.findEdge(key2, key1)

        assertNotNull(edge1)
        assertNotNull(edge2)
        assertEquals(edge1.weight, 1)
        assertEquals(edge2.weight, 1)
    }

    @BeforeEach
    fun setup() {
        graph = UndirectedGraph()
    }

    @Nested
    inner class addVertex {
        @Test
        fun `Empty graph`() {
            val vertex = graph.addVertex(1)

            graph.checkSize(1)
            assertEquals(graph.getSize(), 1)
            assertNotNull(vertex)
            assertEquals(vertex.key, 1)
            graph.checkContainVertex(vertex)
            graph.checkNotNullEdgeArray(vertex)
        }

        @Test
        fun `Non-empty graph`() {
            graph.addVertex(1)
            graph.addVertex(2)
            val vertex = graph.addVertex(3)

            graph.checkSize(3)
            assertNotNull(vertex)
            assertEquals(vertex.key, 3)
            graph.checkContainVertex(vertex)
            graph.checkNotNullEdgeArray(vertex)
        }

        @Test
        fun `Existing vertex`() {
            graph.addVertex(1)
            val vertex = graph.addVertex(1)

            assertNull(vertex)
            graph.checkSize(1)
        }
    }

    @Nested
    inner class removeVertex {
        @Test
        fun `Existing vertex`() {
            graph.addVertex(1)
            val vertex = graph.removeVertex(1)

            graph.checkSize(0)
            assertNotNull(vertex)
            assertEquals(vertex.key, 1)
            graph.checkNotContainVertex(vertex)
            graph.checkNullEdgeArray(vertex)
            assertNull(graph.adjacencyList[vertex])
        }

        @Test
        fun `Non existing vertex`() {
            graph.addVertex(2)
            val vertex = graph.removeVertex(1)

            graph.checkSize(1)
            assertNull(vertex)
        }
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
            graph.checkExistingUndirectedEdge(1, 2)
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
            graph.checkExistingUndirectedEdge(1, 2)
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
    }
}