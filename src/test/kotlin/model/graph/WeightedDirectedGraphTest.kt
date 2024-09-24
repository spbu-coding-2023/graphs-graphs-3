package model.graph

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class WeightedDirectedGraphTest {

    @Nested
    inner class addEdge {
        private val graph = WeightedDirectedGraph()

        @Test
        fun `Not linked vertices`() {
            assertNotNull(graph.addVertex(2))
            assertNotNull(graph.addVertex(1))

            val edge = graph.addEdge(1, 2)

            assertNotNull(edge)

        }

        @Test
        fun `First vertex == second vertex`() {
            assertNotNull(graph.addVertex(1))

            val edge = graph.addEdge(1, 1)

            assertNull(edge)
        }

        @Test
        fun `First edge == second edge`() {
            assertNotNull(graph.addVertex(1))
            assertNotNull(graph.addVertex(2))

            val edge1 = graph.addEdge(1, 2)
            val edge2 = graph.addEdge(1, 2)

            assertNotNull(edge1)
            assertNull(edge2)
        }

        @Test
        fun `Already linked vertices`() {
            assertNotNull(graph.addVertex(1))
            assertNotNull(graph.addVertex(2))

            val edge = graph.addEdge(1, 2, 5)

            assertNotNull(edge)

            assertEquals(edge.weight, 5)
        }

        @Test
        fun `Base graph test`() {
            assertNotNull(graph.addVertex(1))
            assertNotNull(graph.addVertex(2))
            assertNotNull(graph.addVertex(3))

            val edge1 = graph.addEdge(1, 2, 5)
            val edge2 = graph.addEdge(2, 3, 7)
            val edge3 = graph.addEdge(3, 1, 9)

            assertNotNull(edge1)
            assertNotNull(edge2)
            assertNotNull(edge3)

            assertEquals(edge1.weight, 5)
            assertEquals(edge2.weight, 7)
            assertEquals(edge3.weight, 9)
        }

        @Test
        fun `Edge with non existing vertex`() {
            val vertex = graph.addVertex(1)

            assertNull(graph.addEdge(2, 1))
            assertNull(graph.addEdge(1, 2))
            assertNull(graph.addEdge(3, 4))

            assertEquals(graph.adjacencyList[vertex]?.size, 0)
        }
    }
}