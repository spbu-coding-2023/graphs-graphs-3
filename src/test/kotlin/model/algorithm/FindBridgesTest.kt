package model.algorithm

import model.graph.DirectedGraph
import model.graph.UndirectedGraph
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FindBridgesTest {
    @Nested
    inner class `find bridges in directed graph` {
        @Test
        fun `class base test`() {
            val graph = DirectedGraph()

            graph.addVertex(1)
            graph.addVertex(2)
            graph.addVertex(3)
            graph.addVertex(4)
            graph.addVertex(5)
            graph.addVertex(6)

            graph.addEdge(1, 2)
            graph.addEdge(2, 3)
            graph.addEdge(3, 1)
            graph.addEdge(1, 4)
            graph.addEdge(4, 5)
            graph.addEdge(5, 6)
            graph.addEdge(6, 4)

            val bridges = FindBridges(graph).findBridges()

            assertEquals(Pair(bridges[0].first.key, bridges[0].second.key), Pair(1, 4))
        }

        @Test
        fun `crossing multiple bridges`() {
            val graph = DirectedGraph()

            graph.addVertex(1)
            graph.addVertex(2)
            graph.addVertex(3)
            graph.addVertex(4)
            graph.addVertex(5)
            graph.addVertex(6)
            graph.addVertex(7)

            graph.addEdge(1, 2)
            graph.addEdge(2, 3)
            graph.addEdge(3, 1)
            graph.addEdge(1, 4)
            graph.addEdge(4, 5)
            graph.addEdge(5, 6)
            graph.addEdge(6, 7)
            graph.addEdge(7, 5)

            val bridges = FindBridges(graph).findBridges()

            assertEquals(
                listOf(
                    Pair(bridges[0].first.key, bridges[0].second.key),
                    Pair(bridges[1].first.key, bridges[1].second.key)
                ),
                listOf(Pair(4, 5), Pair(1, 4))
            )
        }
    }

    @Nested
    inner class `find bridges in undirected graph` {
        @Test
        fun `class base test`() {
            val graph = UndirectedGraph()

            graph.addVertex(1)
            graph.addVertex(2)
            graph.addVertex(3)
            graph.addVertex(4)
            graph.addVertex(5)
            graph.addVertex(6)

            graph.addEdge(1, 2)
            graph.addEdge(2, 3)
            graph.addEdge(3, 1)
            graph.addEdge(1, 4)
            graph.addEdge(4, 5)
            graph.addEdge(5, 6)
            graph.addEdge(6, 4)

            val bridges = FindBridges(graph).findBridges()

            assertEquals(Pair(bridges[0].first.key, bridges[0].second.key), Pair(1, 4))
        }

        @Test
        fun `crossing multiple bridges`() {
            val graph = UndirectedGraph()

            graph.addVertex(1)
            graph.addVertex(2)
            graph.addVertex(3)
            graph.addVertex(4)
            graph.addVertex(5)
            graph.addVertex(6)
            graph.addVertex(7)

            graph.addEdge(1, 2)
            graph.addEdge(2, 3)
            graph.addEdge(3, 1)
            graph.addEdge(1, 4)
            graph.addEdge(4, 5)
            graph.addEdge(5, 6)
            graph.addEdge(6, 7)
            graph.addEdge(7, 5)

            val bridges = FindBridges(graph).findBridges()

            assertEquals(
                listOf(
                    Pair(bridges[0].first.key, bridges[0].second.key),
                    Pair(bridges[1].first.key, bridges[1].second.key)
                ),
                listOf(Pair(4, 5), Pair(1, 4))
            )
        }
    }
}