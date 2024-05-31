package model.algorithm

import model.graph.Graph
import model.graph.Vertex
import model.graph.WeightedGraph
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BellmanFordTest {
    private fun Graph.addVertices(vararg indexes: Int): Pair<Vertex, Vertex> {
        for (index in indexes.drop(1).dropLast(1)) {
            addVertex(index)
        }

        val first = addVertex(indexes.first()) ?: throw IllegalStateException()
        val second = addVertex(indexes.last()) ?: throw IllegalStateException()

        return first to second
    }

    private fun Graph.addEdges(vararg edges: Triple<Int, Int, Long>) {
        for (edge in edges) {
            addEdge(edge.first, edge.second, edge.third)
        }
    }

    private infix fun Pair<Int, Int>.weight(weight: Long): Triple<Int, Int, Long> {
        return Triple(this.first, this.second, weight)
    }

    @Nested
    inner class `Undirected graph` {
        @Test
        fun `two straight paths to node`() {
            var first: Vertex
            var second: Vertex

            val graph = WeightedGraph().apply {
                val pair = addVertices(1, 2, 3, 4)
                first = pair.first
                second = pair.second

                addEdges(
                    1 to 2 weight 2,
                    2 to 4 weight -2,
                    1 to 3 weight -2,
                    3 to 4 weight 4
                )
            }

            val result = BellmanFord(graph).calculate(first, second).map { it.first.key to it.second.key }
            assertEquals(listOf(1 to 2, 2 to 4), result)
        }
    }

}