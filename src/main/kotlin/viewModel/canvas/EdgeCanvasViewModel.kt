package viewModel.canvas

import androidx.compose.ui.graphics.Color

class EdgeCanvasViewModel(
    val first: VertexCanvasViewModel,
    val second: VertexCanvasViewModel,
    val color: Color,
    strokeWidth: Float,
    zoom: Float,
    val showOrientation: Boolean = true
) {
    val strokeWidth = strokeWidth * zoom
}