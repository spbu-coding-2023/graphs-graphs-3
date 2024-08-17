package model.algorithm

import model.graph.*

class Dijkstra(private val graph: WeightedGraph) {

    fun findShortestPath(startKey: Int, endKey: Int): List<Edge>? {
        val startVertex = graph.vertices.find { it.key == startKey } ?: return null
        val endVertex = graph.vertices.find { it.key == endKey } ?: return null

        // Инициализация расстояний и предшественников
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

        // Восстановление пути
        val path = mutableListOf<Edge>()
        var currentVertex: Vertex? = endVertex

        while (currentVertex != null && currentVertex != startVertex) {
            val prevVertex = previous[currentVertex] ?: break
            val edge = graph.adjacencyList[prevVertex]?.find { it.second == currentVertex }
            if (edge != null) {
                path.add(edge)
            }
            currentVertex = prevVertex
        }

        return if (path.isEmpty()) null else path.reversed()
    }
}
fun main(){
    val graph = WeightedGraph()
    graph.addVertex(1)
    graph.addVertex(2)
    graph.addVertex(3)
    graph.addVertex(4)
    graph.addVertex(5)
    graph.addVertex(6)
    graph.addVertex(7)
    graph.addVertex(8)
    graph.addVertex(9)

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

    val result = Dijkstra(graph).findShortestPath(1, 9)
    if (result != null) {
        for (i in result){
            println(listOf(i.first, i.second, i.weight))
        }
    }

}
