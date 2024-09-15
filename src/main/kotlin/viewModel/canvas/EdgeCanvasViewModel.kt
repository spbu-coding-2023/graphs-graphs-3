package viewModel.canvas

import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Color

class EdgeCanvasViewModel(
    val first: VertexCanvasViewModel,
    val second: VertexCanvasViewModel,
    val color: Color,
    strokeWidth: Float,
    zoom: Float,
    val showOrientation: MutableState<Boolean>
) {
    var strokeWidth = strokeWidth * (zoom)

    fun updateEdge(zoom: Float) {
        strokeWidth = 8f * zoom
    }
}