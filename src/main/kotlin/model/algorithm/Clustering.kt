package model.algorithm

import model.graph.Graph
import model.graph.Vertex
import org.jetbrains.research.ictl.louvain.Link
import org.jetbrains.research.ictl.louvain.getPartition

class Clustering(graph: Graph) {
    val ids = hashMapOf<Vertex, Int>()
    val vIds = hashMapOf<Int, Vertex>()
    val links = mutableListOf<Link>()

    init {
        graph.vertices.forEachIndexed { i, v ->
            ids[v] = i
            vIds[i] = v
        }

        graph.adjacencyList.values.flatten().forEach { e ->
            val first = ids[e.first] ?: throw IllegalStateException("Vertex ${e.first} doesn't have id")
            val second = ids[e.second] ?: throw IllegalStateException("Vertex ${e.second} doesn't have id")

            links.add(MyLink(first, second, e.weight.toDouble()))
        }

    }

    fun calculate(): HashMap<Vertex, Int> {
        val map = getPartition(links, 0)
        val result = map.mapKeys { vIds[it.key] ?: throw IllegalStateException() }
        return HashMap(result)
    }

    inner class MyLink(private val source: Int, private val target: Int, private val weight: Double) : Link {
        override fun source(): Int {
            return source
        }

        override fun target(): Int {
            return target
        }

        override fun weight(): Double {
            return weight
        }
    }
}

