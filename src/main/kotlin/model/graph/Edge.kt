package model.graph

interface Edge {
    val first: Vertex
    val second: Vertex
    var weight: Long
}