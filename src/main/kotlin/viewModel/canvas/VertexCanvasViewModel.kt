package viewModel.canvas

import androidx.compose.ui.geometry.Offset
import viewModel.graph.VertexViewModel

class VertexCanvasViewModel<V>(
    val viewModel: VertexViewModel<V>,
    private val zoom: Float,
    center: Offset,
    canvasSize: Offset
) {
    val offset = Offset(
        (canvasSize.x / 2) + ((viewModel.x - center.x) * zoom),
        (canvasSize.y / 2) + ((viewModel.y - center.y) * zoom)
    )

    val radius = viewModel.radius * zoom
    val color = viewModel.color

    fun onDrag(it: Offset): Unit {
        viewModel.onDrag(it * (1f / zoom))
    }
}