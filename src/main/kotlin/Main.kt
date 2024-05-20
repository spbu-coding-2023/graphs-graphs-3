import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import model.graph.UndirectedGraph
import view.MainView
import viewModel.UndirectedViewModel

val AMOUNT_NODES = 100

val graph = UndirectedGraph<Int>().apply {
    for (i in (0 until AMOUNT_NODES)) {
        addVertex(i)
    }

    for (i in (0 until AMOUNT_NODES)) {
        for (j in (0 until AMOUNT_NODES)) {
            if (Math.random() < 0.005) {
                addEdge(i, j)
            }
        }
    }
}

val undirectedViewModel = UndirectedViewModel(graph, false)

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication
    ) {
        MainView(undirectedViewModel)
    }
}
