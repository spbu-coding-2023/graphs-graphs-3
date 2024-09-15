package model.algorithm


import model.graph.Edge
import model.graph.Graph
import model.graph.Vertex


class FindBridges(
    private val graph: Graph
) {
    fun findBridges(): List<Edge> {
        val visited = mutableMapOf<Vertex, Boolean>()
        val disc = mutableMapOf<Vertex, Int>()
        val low = mutableMapOf<Vertex, Int>()
        val parent = mutableMapOf<Vertex, Vertex?>()
        val bridges = mutableListOf<Edge>()
        var time = 0

        graph.vertices.forEach { vertex ->
            if (visited[vertex] != true) {
                findBridgesUtil(vertex, visited, disc, low, parent, bridges, time)
            }
        }

        return bridges
    }

    private fun findBridgesUtil(
        u: Vertex,
        visited: MutableMap<Vertex, Boolean>,
        disc: MutableMap<Vertex, Int>,
        low: MutableMap<Vertex, Int>,
        parent: MutableMap<Vertex, Vertex?>,
        bridges: MutableList<Edge>,
        time: Int
    ) {
        var currentTime = time
        visited[u] = true
        disc[u] = currentTime
        low[u] = currentTime
        currentTime++

        graph.adjacencyList[u]?.forEach { edge ->
            val v = if (edge.first == u) edge.second else edge.first
            if (visited[v] != true) {
                parent[v] = u
                findBridgesUtil(v, visited, disc, low, parent, bridges, currentTime)

                low[u] = minOf(low[u]!!, low[v]!!)

                if (low[v]!! > disc[u]!!) {
                    bridges.add(edge)
                }
            } else if (v != parent[u]) {
                low[u] = minOf(low[u]!!, disc[v]!!)
            }
        }
    }
}
