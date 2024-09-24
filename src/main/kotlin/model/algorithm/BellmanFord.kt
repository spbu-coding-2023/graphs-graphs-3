package model.algorithm

import model.graph.Edge
import model.graph.Graph
import model.graph.Vertex

class BellmanFord(private val graph: Graph) {
    val parentMap = HashMap<Vertex, Vertex>()
    val dist = graph.vertices.associateWith { Long.MAX_VALUE }.toMutableMap()

    private fun getDistance(v: Vertex): Long {
        return dist[v] ?: throw IllegalStateException("Distance don't initialized")
    }

    fun calculate(start: Vertex, dest: Vertex): List<Edge> {
        dist[start] = 0

        for (i in 0 until graph.vertices.size - 1) {
            updateDist { v, u, uDistance, weight ->
                dist[v] = uDistance + weight
                parentMap[v] = u
            }

            if (updateDist()) {
                // Negative cycle found
                return listOf()
            }
        }


        val result = mutableListOf<Edge>()
        var vertex = dest
        var parent = parentMap[vertex]
        while (parent != start) {
            if (parent == null) return listOf()

            result.add(findEdge(parent, vertex))

            vertex = parent
            parent = parentMap[vertex]
        }

        result.add(findEdge(parent, vertex))
        return result.reversed()
    }

    private fun updateDist(action: (Vertex, Vertex, Long, Long) -> Unit = { _, _, _, _ -> }): Boolean {
        var flag = false

        for (v in graph.vertices) {
            for (u in graph.vertices) {
                val uDistance = getDistance(u)
                val vDistance = getDistance(v)
                if (!isEdgeExists(u, v)) continue

                val weight = getWeight(u, v)
                if (uDistance != Long.MAX_VALUE && uDistance + weight < vDistance) {
                    action(v, u, uDistance, weight)
                    flag = true
                }
            }
        }

        return flag
    }

    private fun getWeight(first: Vertex, second: Vertex): Long {
        return findEdge(first, second).weight
    }

    private fun findEdge(first: Vertex, second: Vertex): Edge {
        val adj = graph.adjacencyList[first] ?: throw IllegalStateException("vertex must have adjacency list")
        val edge = adj.find { it.second == second } ?: throw IllegalStateException("There is no edge between vertices")

        return edge
    }

    private fun isEdgeExists(first: Vertex, second: Vertex): Boolean {
        val adj = graph.adjacencyList[first] ?: throw IllegalStateException("vertex must have adjacency list")
        val edge = adj.find { it.second == second }

        return edge !== null
    }
}