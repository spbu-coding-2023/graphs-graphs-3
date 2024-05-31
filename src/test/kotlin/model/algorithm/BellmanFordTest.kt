package model.algorithm

import model.graph.Graph
import model.graph.Vertex
import model.graph.WeightedGraph
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BellmanFordTest {
    lateinit var graph: Graph

    /*
    * Add vertices to graph
    * return first and last vertex
    * */
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
        @BeforeEach
        fun setup() {
            graph = WeightedGraph()
        }

        //   2
        //  / \
        // 1   4
        //  \ /
        //   3
        @Test
        fun `graph with two straight paths`() {
            val (first, last) = graph.addVertices(1, 2, 3, 4).toList()
            graph.addEdges(
                1 to 2 weight 2,
                2 to 4 weight 2,
                1 to 3 weight 2,
                3 to 4 weight 4
            )

            val result = BellmanFord(graph).calculate(first, last).map { it.first.key to it.second.key }
            assertEquals(listOf(1 to 2, 2 to 4), result)
        }

        //   2
        //  / \
        // 1-3-5
        //  \ /
        //   4
        @Test
        fun `graph with three straight paths`() {
            val (first, last) = graph.addVertices(1, 2, 3, 4, 5).toList()
            graph.addEdges(
                1 to 2 weight 2,
                2 to 5 weight 2,
                1 to 3 weight 2,
                3 to 5 weight 6,
                1 to 4 weight 1,
                4 to 5 weight 4,
            )

            val result = BellmanFord(graph).calculate(first, last).map { it.first.key to it.second.key }
            assertEquals(listOf(1 to 2, 2 to 5), result)
        }

        //   2
        //  / \
        // 1   4
        //  \ /
        //   3
        @Test
        fun `graph with same weight's sum`() {
            val (first, last) = graph.addVertices(1, 2, 3, 4).toList()
            graph.addEdges(
                1 to 2 weight 2,
                2 to 4 weight 2,
                1 to 3 weight 2,
                3 to 4 weight 2
            )

            val result = BellmanFord(graph).calculate(first, last).map { it.first.key to it.second.key }
            assertEquals(true, listOf(1 to 2, 2 to 4) == result || listOf(1 to 3, 3 to 4) == result)
        }

        //   2
        //  / \
        // 1   4
        //  \ /
        //   3
        @Test
        fun `graph with negative edge don't have shortest path (negative cycle)`() {
            var first: Vertex
            var second: Vertex

            graph.apply {
                val pair = addVertices(1, 2, 3, 4)
                first = pair.first
                second = pair.second

                addEdges(
                    1 to 2 weight 2,
                    2 to 4 weight -2,
                    1 to 3 weight 2,
                    3 to 4 weight 4
                )
            }

            val result = BellmanFord(graph).calculate(first, second).map { it.first.key to it.second.key }
            assertEquals(listOf(), result)
        }

        @Test
        fun `dest and source are same`() {
            val firstVertex = (graph.addVertices(1, 2, 3, 4)).first

            val result = BellmanFord(graph).calculate(firstVertex, firstVertex)

            assertEquals(listOf(), result)
        }
    }

}