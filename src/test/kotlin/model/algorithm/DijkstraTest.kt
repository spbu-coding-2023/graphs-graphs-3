package model.algorithm

import model.graph.Graph
import model.graph.WeightedGraph
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DijkstraTest {
    lateinit var graph: Graph

    @Nested
    inner class `Undirected graph`{
        @BeforeEach
        fun setup(){
            graph = WeightedGraph()
        }

        @Test
        fun `base test`(){
            for (i in 1..5) {
                graph.addVertex(i)
            }
            graph.addEdge(1, 2, 2)
            graph.addEdge(2, 5, 4)
            graph.addEdge(1, 4, 4)
            graph.addEdge(4, 2, 1)
            graph.addEdge(1, 3, 3)
            graph.addEdge(4, 5, 1)
            graph.addEdge(3, 5, 5)

            val path = Dijkstra(graph).findShortestPath(1, 5)

            assertNotNull(path)
            assertEquals<List<Triple<Int, Int, Long>>>(path ?: emptyList(),
                listOf(Triple(1, 2, 2),
                    Triple(2, 4, 1),
                    Triple(4, 5, 1)))
        }

        @Test
        fun `test with identical paths`(){
            for (i in 1..5) {
                graph.addVertex(i)
            }
            graph.addEdge(1, 2, 1)
            graph.addEdge(1, 3, 1)
            graph.addEdge(1, 4, 2)
            graph.addEdge(2, 5, 1)
            graph.addEdge(3, 5, 1)
            graph.addEdge(4, 5, 2)

            val path = Dijkstra(graph).findShortestPath(1, 5)

            assertNotNull(path)
            assertEquals<List<Triple<Int, Int, Long>>>(path ?: emptyList(),
                listOf(Triple(1, 2, 1),
                    Triple(2, 5, 1)))
        }

        @Test
        fun `test this bridges`(){
            for (i in 1..9) {
                graph.addVertex(i)
            }
            graph.addEdge(1, 2, 4)
            graph.addEdge(2, 5, 2)
            graph.addEdge(1, 3, 3)
            graph.addEdge(3, 5, 1)
            graph.addEdge(1, 4, 2)
            graph.addEdge(4, 5, 3)
            graph.addEdge(5, 6, 5)
            graph.addEdge(6, 7, 8)
            graph.addEdge(6, 8, 9)
            graph.addEdge(7, 9, 1)
            graph.addEdge(8, 9, 2)

            val path = Dijkstra(graph).findShortestPath(1, 9)

            assertNotNull(path)
            assertEquals<List<Triple<Int, Int, Long>>>(path ?: emptyList(),
                listOf(Triple(1, 3, 3),
                    Triple(3, 5, 1),
                    Triple(5, 6, 5),
                    Triple(6, 7, 8),
                    Triple(7, 9, 1)))
        }

        @Test
        fun `test with a nonexistent path`(){
            for (i in 1..9) {
                graph.addVertex(i)
            }
            graph.addEdge(1, 1, 4)
            graph.addEdge(2, 5, 2)
            graph.addEdge(1, 3, 3)
            graph.addEdge(3, 5, 1)
            graph.addEdge(1, 4, 2)
            graph.addEdge(4, 5, 3)
            graph.addEdge(5, 5, 5)
            graph.addEdge(6, 7, 8)
            graph.addEdge(6, 8, 9)
            graph.addEdge(7, 9, 1)
            graph.addEdge(8, 9, 2)

            val path = Dijkstra(graph).findShortestPath(1, 9)

            assertEquals(path,null)
        }

        @Test
        fun `test with one vertex`(){
            for (i in 1..1) {
                graph.addVertex(i)
            }
            graph.addEdge(1, 1, 4)

            val path = Dijkstra(graph).findShortestPath(1, 1)

            assertEquals(path, null)
        }
    }
}