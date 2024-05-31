package model.graph

open class UndirectedGraph : Graph {
    protected val _vertices = hashMapOf<Int, Vertex>()
    protected val _adjacencyList = hashMapOf<Vertex, ArrayList<Edge>>()

    override val vertices: Collection<Vertex>
        get() = _vertices.values

    override val adjacencyList: HashMap<Vertex, ArrayList<Edge>>
        get() = _adjacencyList

    override fun addVertex(key: Int): Vertex? {
        if (_vertices[key] != null) return null

        val vertex = UndirectedVertex(key)

        _vertices[key] = vertex
        _adjacencyList[vertex] = arrayListOf()

        return vertex
    }

    override fun removeVertex(key: Int): Vertex? {
        val vertex = _vertices[key] ?: return null

        _vertices.remove(key)
        _adjacencyList.remove(vertex)

        return vertex
    }

    override fun addEdge(first: Int, second: Int, weight: Long): Edge? {
        if (first == second) return null

        val vertex1 = _vertices[first] ?: return null
        val vertex2 = _vertices[second] ?: return null

        // edge already exists
        if (_adjacencyList[vertex1]?.find { it.second.key == second } != null) return null

        _adjacencyList[vertex1]?.add(UndirectedEdge(vertex1, vertex2))
        _adjacencyList[vertex2]?.add(UndirectedEdge(vertex2, vertex1))

        return _adjacencyList[vertex1]?.last()
    }

    override fun removeEdge(first: Int, second: Int): Edge? {
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

    override fun updateVertex(key: Int, newKey: Int): Vertex? {
        val vertex = _vertices[key] ?: return null
        if (_vertices[newKey] != null) return null

        vertex.key = newKey
        return vertex
    }

    fun findVertex(key: Int) = _vertices[key]

    fun getEdges(vertex: Vertex) = _adjacencyList[vertex]

    private data class UndirectedVertex(override var key: Int) : Vertex

    private data class UndirectedEdge(override val first: Vertex, override val second: Vertex) : Edge {
        override var weight: Long
            get() = 1
            set(value) {}
    }
}