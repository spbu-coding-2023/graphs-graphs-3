package model.graph

class UndirectedGraph<V>: Graph<V> {
    private val _vertices = hashMapOf<V, Vertex<V>>()
    private val _adjacencyList = hashMapOf<V, ArrayList<Edge<V>>>()

    override val vertices: Collection<Vertex<V>>
        get() = _vertices.values

    override val adjacencyList: HashMap<V, ArrayList<Edge<V>>>
        get() = _adjacencyList

    override fun addVertex(key: V): Vertex<V>? {
        if (_vertices[key] != null) return null

        val vertex = UndirectedVertex(key)

        _vertices[key] = vertex
        _adjacencyList[key] = arrayListOf()

        return vertex
    }

    override fun removeVertex(key: V): Vertex<V>? {
        val vertex = _vertices[key] ?: return null

        _vertices.remove(key)
        _adjacencyList.remove(key)

        return vertex
    }

    override fun addEdge(first: V, second: V): Edge<V>? {
        val vertex1 = _vertices[first] ?: return null
        val vertex2 = _vertices[second] ?: return null

        // edge already exists
        if (_adjacencyList[first]?.find { it.second.key == second } != null) return null

        _adjacencyList[first]?.add(UndirectedEdge(vertex1, vertex2))
        _adjacencyList[second]?.add(UndirectedEdge(vertex2, vertex1))

        return _adjacencyList[first]?.last()
    }

    override fun removeEdge(first: V, second: V): Edge<V>? {
        val edge1 = _adjacencyList[first]?.find { it.second.key == second }
        val edge2 = _adjacencyList[second]?.find { it.second.key == first }

        // edge doesn't exist
        if (edge1 == null || edge2 == null) return null

        _adjacencyList[first]?.remove(edge1)
        _adjacencyList[second]?.remove(edge1)

        return edge1
    }

    override fun updateVertex(key: V, newKey: V): Vertex<V>? {
        val vertex = _vertices[key] ?: return null
        if (_vertices[newKey] != null) return null

        vertex.key = newKey
        return vertex
    }

    private data class UndirectedVertex<V>(override var key: V): Vertex<V>

    private data class UndirectedEdge<V>(override val first: Vertex<V>, override val second: Vertex<V>): Edge<V> {
        override var weight: Long
            get() = 1
            set(value) {}
    }
}