package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import model.graph.UndirectedGraph
import viewModel.canvas.CanvasViewModel

class MainViewModel(
    graph: UndirectedGraph
) {
    var isClustering = mutableStateOf(false)
    var isRanked = mutableStateOf(false)
    var isNodeCreatingMode = mutableStateOf(false)

    val canvasViewModel = CanvasViewModel(graph)

    val settingsViewModel = SettingsViewModel(
        canvasViewModel::onColorChange,
        canvasViewModel::onSizeChange,
        canvasViewModel::onOrientatedChange
    )

    val menuViewModel = MenuViewModel(canvasViewModel)
}