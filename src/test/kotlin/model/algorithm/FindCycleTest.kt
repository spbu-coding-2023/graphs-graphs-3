package model.algorithm

import model.graph.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FindCycleTest {
    lateinit var graph: Graph

    private fun Graph.addVertices(vararg indexes: Int): Vertex {
        for (index in indexes.drop(1)) {
            addVertex(index)
        }

        return addVertex(indexes.first()) ?: throw IllegalStateException()
    }

    private fun Graph.addEdges(vararg edges: Pair<Int, Int>) {
        for (edge in edges) {
            addEdge(edge.first, edge.second)
        }
    }

    private fun checkPath(expected: List<Pair<Int, Int>>, path: List<Edge>) {
        assertEquals(expected, path.map { it.first.key to it.second.key })
    }

    private infix fun Int.goto(value: Int): MutableList<Pair<Int, Int>> {
        return mutableListOf(Pair(this, value))
    }

    private infix fun MutableList<Pair<Int, Int>>.goto(value: Int): MutableList<Pair<Int, Int>> {
        this.add(this.last().second to value)
        return this
    }

    private fun Graph.createPath(list: List<Pair<Int, Int>>) {
        list.forEach { addEdge(it.first, it.second) }
    }

    @Nested
    inner class `Undirected graph` {
        @BeforeEach
        fun setup() {
            graph = UndirectedGraph()
        }

        @Test
        fun `Connected graph with 3 nodes`() {
            var vertex: Vertex;

            graph.apply {
                vertex = addVertices(1, 2, 3)
                addEdges(1 to 2, 1 to 3, 2 to 3)
            }

            val result = FindCycle(graph).calculate(vertex)
            checkPath(listOf(1 to 2, 2 to 3, 3 to 1), result)
        }

        @Test
        fun `Connected graph with 5 nodes`() {
            var vertex: Vertex;

            graph.apply {
                vertex = addVertices(1, 2, 3, 4, 5)
                addEdges(1 to 2, 1 to 3, 1 to 4, 1 to 5, 2 to 3, 2 to 4, 2 to 5, 3 to 4, 3 to 5, 4 to 5)
            }

            val result = FindCycle(graph).calculate(vertex)
            checkPath(listOf(1 to 2, 2 to 3, 3 to 1), result)
        }

        @Test
        fun `Graph with 5 nodes with one cycle`() {
            var vertex: Vertex;

            graph.apply {
                vertex = addVertices(1, 2, 3, 4, 5)
                addEdges(1 to 2, 2 to 3, 3 to 4, 4 to 5, 5 to 1)
            }

            val result = FindCycle(graph).calculate(vertex)
            checkPath(listOf(1 to 2, 2 to 3, 3 to 4, 4 to 5, 5 to 1), result)
        }

        //   2 - 3
        //  /
        // 1
        //  \
        //   4 - 5
        @Test
        fun `Graph with 5 nodes without cycle`() {
            var vertex: Vertex;

            graph.apply {
                vertex = addVertices(1, 2, 3, 4, 5)
                addEdges(1 to 2, 2 to 3, 4 to 5, 5 to 1)
            }

            val result = FindCycle(graph).calculate(vertex)
            checkPath(listOf(), result)
        }

        //         4
        //         |
        // 1 - 2 - 3 - 5 - 7 - 1
        //             |
        //             6
        @Test
        fun `Graph with one cycle and with additional paths`() {
            var vertex: Vertex;

            graph.apply {
                vertex = addVertices(1, 2, 3, 4, 5, 6, 7)
                addEdges(1 to 2, 2 to 3, 3 to 4, 3 to 5, 5 to 6, 5 to 7, 7 to 1)
            }

            val result = FindCycle(graph).calculate(vertex)
            checkPath(listOf(1 to 2, 2 to 3, 3 to 5, 5 to 7, 7 to 1), result)
        }
    }

    @Nested
    inner class `Directed graph` {
        @BeforeEach
        fun setup() {
            graph = DirectedGraph()
        }

        @Test
        fun `Straight graph`() {
            val vertex = graph.addVertices(1, 2, 3, 4, 5)
            graph.addEdges(1 to 2, 2 to 3, 3 to 4, 4 to 5)

            val result = FindCycle(graph).calculate(vertex)
            assertEquals(listOf(), result)
        }

        //   2-3-4
        //  /     \
        // 1       5
        //  \     /
        //   6-7-8
        @Test
        fun `Circle without cycles`() {
            val vertex = graph.addVertices(1, 2, 3, 4, 5, 6, 7, 8)

            graph.createPath(1 goto 2 goto 3 goto 4 goto 5)
            graph.createPath(1 goto 6 goto 7 goto 8 goto 5)

            val result = FindCycle(graph).calculate(vertex)
            assertEquals(listOf(), result)
        }

        //   2-3-4
        //  /     \
        // 1       5
        //  \     /
        //   6-7-8
        @Test
        fun `Circle with cycle`() {
            val vertex = graph.addVertices(1, 2, 3, 4, 5, 6, 7, 8)

            graph.createPath(1 goto 2 goto 3 goto 4 goto 5 goto 8 goto 7 goto 6 goto 1)

            val result = FindCycle(graph).calculate(vertex).map { it.first.key to it.second.key }
            assertEquals((1 goto 2 goto 3 goto 4 goto 5 goto 8 goto 7 goto 6 goto 1), result)
        }
    }
}