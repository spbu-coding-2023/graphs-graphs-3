package model.graph

interface Graph {
    val vertices: Collection<Vertex>
    val adjacencyList: HashMap<Vertex, ArrayList<Edge>>

    fun addVertex(key: Int): Vertex?
    fun removeVertex(key: Int): Vertex?
    fun updateVertex(key: Int, newKey: Int): Vertex?
    fun addEdge(first: Int, second: Int, weight: Long = 1): Edge?
    fun removeEdge(first: Int, second: Int): Edge?
}