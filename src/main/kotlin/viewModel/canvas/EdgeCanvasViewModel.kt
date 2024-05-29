package viewModel.canvas

import androidx.compose.ui.graphics.Color

class EdgeCanvasViewModel<V>(
    val first: VertexCanvasViewModel<V>,
    val second: VertexCanvasViewModel<V>,
    val color: Color,
    strokeWidth: Float,
    zoom: Float,
    val showOrientation: Boolean = true
) {
    val strokeWidth = strokeWidth * zoom
}