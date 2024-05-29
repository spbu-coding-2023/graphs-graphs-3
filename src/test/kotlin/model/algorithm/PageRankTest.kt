package model.algorithm

import model.graph.DirectedGraph
import model.graph.UndirectedGraph
import org.junit.jupiter.api.Assertions.assertNotNull
import kotlin.test.Test
import kotlin.test.assertEquals

class BetweennesCentralityTest {

    @Test
    fun basicDirectedGraph() {
        val graph = DirectedGraph()
        for (i in 0..3) {
            graph.addVertex(i)
        }
        graph.addEdge(0, 1)
        graph.addEdge(0, 2)
        graph.addEdge(1, 2)
        graph.addEdge(3, 2)
        graph.addEdge(2, 0)

        val centrality = PageRank(graph).computePageRank(1)

        assertNotNull(centrality)
        assertEquals(centrality[0].first.key, 2)
    }

    @Test
    fun basicUndirectedGraph() {
        val graph = UndirectedGraph()
        for (i in 0..3) {
            graph.addVertex(i)
        }
        graph.addEdge(0, 1)
        graph.addEdge(0, 2)
        graph.addEdge(1, 2)
        graph.addEdge(3, 2)

        val centrality = PageRank(graph).computePageRank(1)

        assertNotNull(centrality)
        assertEquals(centrality[0].first.key, 2)
    }
}