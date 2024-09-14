package viewModel

import model.graph.UndirectedGraph
import viewModel.canvas.CanvasViewModel

class MainViewModel(
    graph: UndirectedGraph
) {
    val canvasViewModel = CanvasViewModel(graph)

    val settingsViewModel = SettingsViewModel(
        canvasViewModel::onColorChange,
        canvasViewModel::onSizeChange,
        canvasViewModel::onOrientatedChange
    )

    val menuViewModel = MenuViewModel(canvasViewModel)
}