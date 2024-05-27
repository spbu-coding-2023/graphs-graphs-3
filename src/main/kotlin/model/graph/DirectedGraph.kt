package model.graph

class DirectedGraph<V>: UndirectedGraph<V>() {

    override fun addEdge(first: V, second: V, weight: Long): Edge<V>? {
            if (first == second) return null

            val vertex1 = _vertices[first] ?: return null
            val vertex2 = _vertices[second] ?: return null

            // edge already exists
            if (_adjacencyList[vertex1]?.find { it.second.key == second } != null) return null

            _adjacencyList[vertex1]?.add(DirectedEdge(vertex1, vertex2))


            return _adjacencyList[vertex1]?.last()
    }

    private data class DirectedEdge<V>(override val first: Vertex<V>, override val second: Vertex<V>) : Edge<V> {
        override var weight: Long
            get() = 1
            set(value) {}
    }
}