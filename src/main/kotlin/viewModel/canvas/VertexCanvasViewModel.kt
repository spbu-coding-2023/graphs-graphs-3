package viewModel.canvas

import androidx.compose.ui.geometry.Offset
import viewModel.graph.VertexViewModel

class VertexCanvasViewModel(
    val vertexViewModel: VertexViewModel,
    private val canvasViewModel: CanvasViewModel,
) {
    var color by vertexViewModel::color
    private var zoom by canvasViewModel::zoom
    private var center by canvasViewModel::center
    private var canvasSize by canvasViewModel::canvasSize

    val strokeWidth
        get() = 8f * zoom
    val radius
        get() = vertexViewModel.radius * zoom
    val offset
        get() = calculateOffset()

    val textSize
        get() = vertexViewModel.radius * 0.6f * zoom

    fun onDrag(it: Offset): Unit {
        vertexViewModel.onDrag(it * (1f / zoom))
    }

    private fun calculateOffset() = Offset(
        (canvasSize.x / 2) + ((vertexViewModel.x - center.x) * zoom),
        (canvasSize.y / 2) + ((vertexViewModel.y - center.y) * zoom)
    )
}