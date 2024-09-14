import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import model.graph.UndirectedGraph
import view.HeaderView
import view.MainView
import viewModel.MainViewModel

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

fun main() = application {
    var isOpen by remember { mutableStateOf(true) }
    var isMaximized by remember { mutableStateOf(true) }
    val headerName by remember { mutableStateOf("Dimabase.db") }

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
                mainViewModel,
            )
        }
    }
}
