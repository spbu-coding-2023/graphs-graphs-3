package model.reader

import model.graph.Graph

interface Reader {
    fun saveGraph(graph: Graph<Int>, filepath: String): Unit
    fun loadGraph(filepath: String): Graph<Int>
}