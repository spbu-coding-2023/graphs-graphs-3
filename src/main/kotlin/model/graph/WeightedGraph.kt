package model.graph

class WeightedGraph : UndirectedGraph() {

    override fun addEdge(first: Int, second: Int, weight: Long): Edge? {

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

    private fun addNewEdge(vertex1: Vertex, vertex2: Vertex, weight: Long): Edge? {

        _adjacencyList[vertex1]?.add(WeightedEdge(vertex1, vertex2, weight))
        _adjacencyList[vertex2]?.add(WeightedEdge(vertex2, vertex1, weight))

        return _adjacencyList[vertex1]?.last()
    }

    private data class WeightedEdge(
        override val first: Vertex, override val second: Vertex,
        override var weight: Long
    ) : Edge

}

