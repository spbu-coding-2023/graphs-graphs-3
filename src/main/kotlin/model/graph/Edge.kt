package model.graph

interface Edge <V> {
    val first: Vertex<V>
    val second: Vertex<V>
    var weight: Long
}