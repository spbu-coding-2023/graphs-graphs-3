package model.algorithm

import model.graph.Edge
import model.graph.Graph
import model.graph.Vertex

class FindCycle(private val graph: Graph) {
    val color: MutableMap<Vertex, Color> = graph.vertices.associateWith { Color.WHITE }.toMutableMap()
    val path: MutableList<Edge> = mutableListOf()
    var isFound: Boolean = false

    fun calculate(vertex: Vertex): List<Edge> {
        dfs(vertex)

        return path
    }

    private fun dfs(vertex: Vertex) {
        if (isFound) return

        color[vertex] = Color.GRAY

        for (childEdge in getChildren(vertex)) {
            if (isFound) break
            if (isSameEdgeWithLast(childEdge)) continue

            when (color[childEdge.second]) {
                Color.WHITE -> {
                    path.add(childEdge)
                    dfs(childEdge.second)
                }

                Color.GRAY -> {
                    path.add(childEdge)
                    isFound = true
                }
                
                else -> path.dropLast(1)
            }
        }

        color[vertex] = Color.BLACK
    }

    private fun getChildren(vertex: Vertex): List<Edge> {
        val children =
            graph.adjacencyList[vertex] ?: throw IllegalStateException("Vertex must have at least empty adjacency list")

        return children
    }

    private fun isSameEdgeWithLast(edge: Edge): Boolean {
        val lastEdge = path.lastOrNull() ?: return false

        return (lastEdge.first == edge.second && lastEdge.second == edge.first)
    }

    enum class Color { GRAY, WHITE, BLACK }
}