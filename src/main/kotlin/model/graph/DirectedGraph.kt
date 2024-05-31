package model.graph

class DirectedGraph : UndirectedGraph() {

    override fun addEdge(first: Int, second: Int, weight: Long): Edge? {
        if (first == second) return null

        val vertex1 = _vertices[first] ?: return null
        val vertex2 = _vertices[second] ?: return null

        // edge already exists
        if (_adjacencyList[vertex1]?.find { it.second.key == second } != null) return null

        _adjacencyList[vertex1]?.add(DirectedEdge(vertex1, vertex2))


        return _adjacencyList[vertex1]?.last()
    }

    private data class DirectedEdge(override val first: Vertex, override val second: Vertex) : Edge {
        override var weight: Long
            get() = 1
            set(value) {}
    }
}