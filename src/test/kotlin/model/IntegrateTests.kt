package model

import androidx.compose.ui.geometry.Offset
import model.graph.UndirectedGraph
import org.junit.jupiter.api.Test
import viewModel.MainViewModel
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class IntegrateTests {
    @Test
    fun scenario1() {
        // User launch app, mainViewModel is creating
        val AMOUNT_NODES = 16
        val EDGE_CHANGE = 5.0

        val graph = UndirectedGraph().apply {
            for (i in (0 until AMOUNT_NODES)) {
                addVertex(i)
            }

            for (i in (0 until AMOUNT_NODES)) {
                for (j in (0 until AMOUNT_NODES)) {
                    if (Math.random() < EDGE_CHANGE / 100) {
                        addEdge(i, j)
                    }
                }
            }
        }

        val mainViewModel = MainViewModel(graph)

        // User create a few nodes
        val oldSize = graph.vertices.size

        mainViewModel.canvasViewModel.isNodeCreatingMode = true
        mainViewModel.canvasViewModel.createNode(offset = Offset(100f, 100f))
        mainViewModel.canvasViewModel.createNode(offset = Offset(300f, 100f))
        mainViewModel.canvasViewModel.createNode(offset = Offset(200f, 100f))
        mainViewModel.canvasViewModel.createNode(offset = Offset(100f, 100f))

        // Graph changed
        assertTrue(graph.vertices.size != oldSize)

        mainViewModel.canvasViewModel.isNodeCreatingMode = false

        // User add edge
        mainViewModel.canvasViewModel.isEdgeCreatingMode = true
        val firstVertex = mainViewModel.canvasViewModel.vertices.find { it.vertexViewModel.getKey() == 17 }
            ?: throw Error("There is no vertex with id 17")
        val secondVertex = mainViewModel.canvasViewModel.vertices.find { it.vertexViewModel.getKey() == 18 }
            ?: throw Error("There is no vertex with id 18")

        // User click on two vertecies
        mainViewModel.canvasViewModel.onClick(firstVertex)
        mainViewModel.canvasViewModel.onClick(secondVertex)

        // Edge created
        val edges = mainViewModel.canvasViewModel.edges.flatten()
        val edge = edges.find { it.first == firstVertex && it.second == secondVertex }
        assertNotNull(edge)
    }
}