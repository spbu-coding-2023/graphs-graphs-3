package model.graph

class WeightedGraph<V>: UndirectedGraph<V>() {

    override fun addEdge(first: V, second: V, weight: Long): Edge<V>? {

        val vertex1 = _vertices[first] ?: return null
        val vertex2 = _vertices[second] ?: return null

        val edgesVertex1 = _adjacencyList[vertex1]
        val edgesVertex2 = _adjacencyList[vertex2]

        if (edgesVertex1 == null || edgesVertex2 == null)
            return addNewEdge(vertex1, vertex2, weight)

        val edge1 = edgesVertex1.find { it.second.key == second }
        val edge2 = edgesVertex2.find { it.second.key == first }

        if (edge1 == null || edge2 == null) return addNewEdge(vertex1, vertex2, weight)

        if (edge1.weight == weight) return null

        edge1.weight = weight
        edge2.weight = weight

        return edge1
    }

    private fun addNewEdge(vertex1: Vertex<V>, vertex2: Vertex<V>, weight: Long): Edge<V>?{

        _adjacencyList[vertex1]?.add(WeightedEdge(vertex1, vertex2, weight))
        _adjacencyList[vertex2]?.add(WeightedEdge(vertex2, vertex1, weight))

        return _adjacencyList[vertex1]?.last()
    }

    private data class WeightedEdge<V>(override val first: Vertex<V>, override val second: Vertex<V>,
                                       override var weight: Long) : Edge<V>

}

