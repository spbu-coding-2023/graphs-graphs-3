package model.graph

open class UndirectedGraph<V> : Graph<V> {
    protected val _vertices = hashMapOf<V, Vertex<V>>()
    protected val _adjacencyList = hashMapOf<Vertex<V>, ArrayList<Edge<V>>>()

    override val vertices: Collection<Vertex<V>>
        get() = _vertices.values

    override val adjacencyList: HashMap<Vertex<V>, ArrayList<Edge<V>>>
        get() = _adjacencyList

    override fun addVertex(key: V): Vertex<V>? {
        if (_vertices[key] != null) return null

        val vertex = UndirectedVertex(key)

        _vertices[key] = vertex
        _adjacencyList[vertex] = arrayListOf()

        return vertex
    }

    override fun removeVertex(key: V): Vertex<V>? {
        val vertex = _vertices[key] ?: return null

        _vertices.remove(key)
        _adjacencyList.remove(vertex)

        return vertex
    }

    open override fun addEdge(first: V, second: V, weight: Long): Edge<V>? {
        if (first == second) return null

        val vertex1 = _vertices[first] ?: return null
        val vertex2 = _vertices[second] ?: return null

        // edge already exists
        if (_adjacencyList[vertex1]?.find { it.second.key == second } != null) return null

        _adjacencyList[vertex1]?.add(UndirectedEdge(vertex1, vertex2))
        _adjacencyList[vertex2]?.add(UndirectedEdge(vertex2, vertex1))

        return _adjacencyList[vertex1]?.last()
    }

    override fun removeEdge(first: V, second: V): Edge<V>? {
        val vertex1 = _vertices[first]
        val vertex2 = _vertices[second]

        val edge1 = _adjacencyList[vertex1]?.find { it.second.key == second }
        val edge2 = _adjacencyList[vertex2]?.find { it.second.key == first }

        // edge doesn't exist
        if (edge1 == null || edge2 == null) return null

        _adjacencyList[vertex1]?.remove(edge1)
        _adjacencyList[vertex2]?.remove(edge1)

        return edge1
    }

    override fun updateVertex(key: V, newKey: V): Vertex<V>? {
        val vertex = _vertices[key] ?: return null
        if (_vertices[newKey] != null) return null

        vertex.key = newKey
        return vertex
    }

    fun findVertex(key: V) = _vertices[key]

    fun getEdges(vertex: Vertex<V>) = _adjacencyList[vertex]

    private data class UndirectedVertex<V>(override var key: V) : Vertex<V>

    private data class UndirectedEdge<V>(override val first: Vertex<V>, override val second: Vertex<V>) : Edge<V> {
        override var weight: Long
            get() = 1
            set(value) {}
    }
}