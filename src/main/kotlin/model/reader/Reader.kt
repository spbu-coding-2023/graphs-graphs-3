package model.reader

import model.graph.Graph

interface Reader {
    fun saveGraph(graph: Graph, filepath: String): Unit
    fun loadGraph(filepath: String): Graph
}