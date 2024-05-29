package model.algorithm


import model.graph.*


class FindBridges<V>(
    private val graph: Graph<V>
) {
    fun findBridges(): List<Edge<V>> {
        val visited = mutableMapOf<Vertex<V>, Boolean>()
        val disc = mutableMapOf<Vertex<V>, Int>()
        val low = mutableMapOf<Vertex<V>, Int>()
        val parent = mutableMapOf<Vertex<V>, Vertex<V>?>()
        val bridges = mutableListOf<Edge<V>>()
        var time = 0

        graph.vertices.forEach { vertex ->
            if (visited[vertex] != true) {
                findBridgesUtil(vertex, visited, disc, low, parent, bridges, time)
            }
        }

        return bridges
    }

    private fun findBridgesUtil(
        u: Vertex<V>,
        visited: MutableMap<Vertex<V>, Boolean>,
        disc: MutableMap<Vertex<V>, Int>,
        low: MutableMap<Vertex<V>, Int>,
        parent: MutableMap<Vertex<V>, Vertex<V>?>,
        bridges: MutableList<Edge<V>>,
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
