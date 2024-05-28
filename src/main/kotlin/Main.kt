import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import model.algorithm.Clustering
import model.graph.UndirectedGraph
import view.HeaderView
import view.MainView
import viewModel.graph.UndirectedViewModel

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
val undirectedViewModel = UndirectedViewModel(graph, false)

fun main() = application {
    var isOpen by remember { mutableStateOf(true) }
    var isMaximized by remember { mutableStateOf(true) }
    var isMinimize by remember { mutableStateOf(false) }
    var position: WindowPosition by remember { mutableStateOf(WindowPosition.PlatformDefault) }
    var headerName by remember { mutableStateOf("Dimabase.db") }

    val windowState = WindowState(
        placement = if (isMaximized) WindowPlacement.Maximized else WindowPlacement.Floating,
    )

    if (isOpen) {
        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            undecorated = true,
        ) {
            WindowDraggableArea {
                HeaderView(headerName,
                    { isOpen = false }, {
                        isMaximized = !isMaximized
                    },
                    isMaximized, { windowState.isMinimized = !windowState.isMinimized })
            }
            MainView(
                undirectedViewModel,
            )
        }
    }
}
