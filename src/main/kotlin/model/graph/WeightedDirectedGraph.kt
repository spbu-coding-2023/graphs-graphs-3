package model.graph

class WeightedDirectedGraph: DirectedGraph() {
    override fun addEdge(first: Int, second: Int, weight: Long): Edge? {
        if (first == second) return null

        val vertex1 = _vertices[first] ?: return null
        val vertex2 = _vertices[second] ?: return null

        // edge already exists
        if (_adjacencyList[vertex1]?.find { it.second.key == second } != null) return null

        _adjacencyList[vertex1]?.add(WeightedDirectedEdge(vertex1, vertex2, weight))


        return _adjacencyList[vertex1]?.last()
    }

    private data class WeightedDirectedEdge(
        override val first: Vertex,
        override val second: Vertex,
        override var weight: Long
    ) : Edge
}