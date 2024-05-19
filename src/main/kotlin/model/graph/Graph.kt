package model.graph

interface Graph <V> {
    val vertices: Collection<Vertex<V>>
    val adjacencyList: HashMap<V, ArrayList<Edge<V>>>

    fun addVertex(key: V): Vertex<V>?
    fun removeVertex(key: V): Vertex<V>?
    fun updateVertex(key: V, newKey: V): Vertex<V>?
    fun addEdge(first: V, second: V): Edge<V>?
    fun removeEdge(first: V, second: V): Edge<V>?
}