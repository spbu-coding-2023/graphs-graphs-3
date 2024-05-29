package model.graph

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class WeightedGraphTest {
    lateinit var graph: WeightedGraph<Int>

    @Nested
    inner class addEdge {

        @BeforeEach
        fun setup() {
            graph = WeightedGraph()
        }

        @Test
        fun `Graph without edges`() {

            val vertex1 = graph.addVertex(1)
            graph.addVertex(2)

            val edge = graph.addEdge(1, 2, 10)

            assertNotNull(edge)
            assertEquals(edge.weight, 10)

            graph.adjacencyList[vertex1]?.let { assertEquals(arrayListOf<Long>(10), it.map{it.weight}) }
        }

        @Test
        fun `Connect vertices without edge`() {

            val vertex1 = graph.addVertex(1)
            graph.addVertex(2)
            val vertex2 = graph.addVertex(3)
            graph.addVertex(4)

            graph.addEdge(1, 2, 10)
            graph.addEdge(3, 4, 11)
            graph.addEdge(3, 1, 12)

            graph.adjacencyList[vertex1]?.let { assertEquals(arrayListOf<Long>(10, 12), it.map{it.weight}) }
            graph.adjacencyList[vertex2]?.let { assertEquals(arrayListOf<Long>(11, 12), it.map{it.weight}) }
        }

        @Test
        fun `Add edge with the same weight`() {

            val vertex1 = graph.addVertex(1)
            val vertex2 = graph.addVertex(2)

            graph.addEdge(1, 2, 10)
            assertEquals(null, graph.addEdge(1, 2, 10))

            graph.adjacencyList[vertex1]?.let { assertEquals(arrayListOf<Long>(10), it.map{it.weight}) }
            graph.adjacencyList[vertex2]?.let { assertEquals(arrayListOf<Long>(10), it.map{it.weight}) }
        }

        @Test
        fun `Add the same edge with new weight`() {
            val vertex1 = graph.addVertex(1)
            val vertex2 = graph.addVertex(2)

            graph.addEdge(1, 2, 10)
            graph.addEdge(1, 2, 11)

            graph.adjacencyList[vertex1]?.let { assertEquals(arrayListOf<Long>(11), it.map{it.weight}) }
            graph.adjacencyList[vertex2]?.let { assertEquals(arrayListOf<Long>(11), it.map{it.weight}) }
        }
    }
}