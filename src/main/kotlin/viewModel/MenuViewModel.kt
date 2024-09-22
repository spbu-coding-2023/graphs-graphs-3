package viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import viewModel.canvas.CanvasViewModel

class MenuViewModel(
    val canvasViewModel: CanvasViewModel
) {
    var isNodeCreating by canvasViewModel::isNodeCreatingMode
    var isClustering by canvasViewModel::isClustering
    var isRanked by canvasViewModel::isRanked
    var isAlgorithmMenuOpen by mutableStateOf(false)
}