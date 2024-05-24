import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import model.algorithm.Clustering
import model.graph.UndirectedGraph
import view.MainView
import viewModel.UndirectedViewModel

val AMOUNT_NODES = 500
val EDGE_CHANGE = 0.05f

val graph = UndirectedGraph<Int>().apply {
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

val groups = Clustering(graph).calculate()
val undirectedViewModel = UndirectedViewModel(graph, false, groups)

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(placement = WindowPlacement.Maximized)
    ) {
        MainView(undirectedViewModel)
    }
}
