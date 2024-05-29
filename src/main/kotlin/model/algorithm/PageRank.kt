package model.algorithm

import model.graph.Graph
import model.graph.Vertex

class PageRank<V>(
    private val graph: Graph,
    private val dampingFactor: Double = 0.85,
    private val iterations: Int = 100
) {
    fun computePageRank(topN: Int): List<Pair<Vertex, Double>> {
        val ranks = mutableMapOf<Vertex, Double>()
        val vertices = graph.vertices

        // Initialize ranks
        vertices.forEach { vertex ->
            ranks[vertex] = 1.0 / vertices.size
        }

        repeat(iterations) {
            val newRanks = mutableMapOf<Vertex, Double>()

            vertices.forEach { vertex ->
                var rankSum = 0.0
                vertices.forEach { neighbor ->
                    val edges = graph.adjacencyList[neighbor]
                    if (neighbor != vertex && edges != null) {
                        if (edges.any { it.second == vertex }) {
                            rankSum += ranks[neighbor]?.div(edges.size) ?: 0.0
                        }
                    }
                }
                newRanks[vertex] = (1 - dampingFactor) / vertices.size + dampingFactor * rankSum
            }

            // Update ranks
            newRanks.forEach { (vertex, rank) ->
                ranks[vertex] = rank
            }
        }

        return ranks.entries
            .sortedByDescending { it.value }
            .take(topN)
            .map { it.toPair() }
    }
}