package model.algorithm

import model.graph.Graph
import model.graph.Vertex

class Dijkstra(private val graph: Graph) {

    fun findShortestPath(startKey: Int, endKey: Int): List<Triple<Int, Int, Long>>? {
        val startVertex = graph.vertices.find { it.key == startKey } ?: return null
        val endVertex = graph.vertices.find { it.key == endKey } ?: return null

        val distances = mutableMapOf<Vertex, Long>().withDefault { Long.MAX_VALUE }
        val previous = mutableMapOf<Vertex, Vertex?>()
        val visited = mutableSetOf<Vertex>()

        distances[startVertex] = 0

        val priorityQueue = java.util.PriorityQueue(compareBy<Vertex> { distances.getValue(it) })
        priorityQueue.add(startVertex)

        while (priorityQueue.isNotEmpty()) {
            val currentVertex = priorityQueue.poll()
            if (currentVertex == endVertex) break

            visited.add(currentVertex)

            val edges = graph.adjacencyList[currentVertex] ?: continue
            for (edge in edges) {
                val neighbor = edge.second
                if (neighbor in visited) continue

                val newDist = distances.getValue(currentVertex) + edge.weight
                if (newDist < distances.getValue(neighbor)) {
                    distances[neighbor] = newDist
                    previous[neighbor] = currentVertex
                    priorityQueue.add(neighbor)
                }
            }
        }

        val path = mutableListOf<Triple<Int, Int, Long>>()
        var currentVertex: Vertex? = endVertex

        while (currentVertex != null && currentVertex != startVertex) {
            val prevVertex = previous[currentVertex] ?: break
            val edge = graph.adjacencyList[prevVertex]?.find { it.second == currentVertex }
            if (edge != null) {
                path.add(Triple(edge.first.key, edge.second.key, edge.weight))
            }
            currentVertex = prevVertex
        }

        return if (path.isEmpty()) null else path.reversed()
    }
}
