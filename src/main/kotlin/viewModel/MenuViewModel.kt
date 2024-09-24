package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import viewModel.canvas.CanvasViewModel

class MenuViewModel(
    val canvasViewModel: CanvasViewModel
) {
    var isNodeCreating by canvasViewModel::isNodeCreatingMode
    var isEdgeCreating by canvasViewModel::isEdgeCreatingMode
    var isClustering by canvasViewModel::isClustering
    var isRanked by canvasViewModel::isRanked
    var isBridgeFinded by canvasViewModel::isFinded
    var isDijkstraMode by canvasViewModel::isDijkstraMode
    var isAlgorithmMenuOpen by mutableStateOf(false)
}
