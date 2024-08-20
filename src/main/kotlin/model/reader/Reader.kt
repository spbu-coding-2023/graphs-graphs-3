package model.reader

import model.graph.Graph

interface Reader {
    fun saveGraph(graph: Graph, filepath: String, nameGraph: String): Unit
    fun loadGraph(filepath: String, nameGraph: String): Graph
}