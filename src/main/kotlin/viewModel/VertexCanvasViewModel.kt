package viewModel

import androidx.compose.ui.geometry.Offset

class VertexCanvasViewModel<V>(
    val viewModel: VertexViewModel<V>,
    zoom: Float,
    center: Offset,
    canvasSize: Offset
) {
    val offset = Offset(
        (canvasSize.x / 2) + ((viewModel.x - center.x) * zoom),
        (canvasSize.y / 2) + ((viewModel.y - center.y) * zoom)
    )

    val radius = viewModel.radius * zoom
    val color = viewModel.color
}