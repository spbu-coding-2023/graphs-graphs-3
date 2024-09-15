package viewModel

import androidx.compose.runtime.mutableStateOf
import viewModel.canvas.CanvasViewModel

class MenuViewModel(
    val canvasViewModel: CanvasViewModel
) {
    var isNodeCreating
        get() = canvasViewModel.isNodeCreatingMode
        set(value) {
            canvasViewModel.isNodeCreatingMode = value
        }

    fun onNodeCreatingChange() {
        isNodeCreating = !isNodeCreating
    }

    var isClustering
        get() = canvasViewModel.isClustering
        set(value) {
            canvasViewModel.isClustering = value
        }

    fun onClusteringChange() {
        isClustering = !isClustering
    }

    var isRanked
        get() = canvasViewModel.isRanked
        set(value) {
            canvasViewModel.isRanked = value
        }

    fun onRankedChange() {
        isRanked = !isRanked
    }

    val _isAlgorithmMenuOpen = mutableStateOf(false)
    var isAlgorithmMenuOpen
        get() = _isAlgorithmMenuOpen.value
        set(value) {
            _isAlgorithmMenuOpen.value = value
        }

    fun onAlgorithmMenuOpenChange() {
        isAlgorithmMenuOpen = !isAlgorithmMenuOpen
    }
}