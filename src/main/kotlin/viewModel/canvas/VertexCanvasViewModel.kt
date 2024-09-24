package viewModel.canvas

import androidx.compose.ui.geometry.Offset
import viewModel.graph.VertexViewModel

class VertexCanvasViewModel(
    val vertexViewModel: VertexViewModel,
    val canvasViewModel: CanvasViewModel,
) {
    val color by vertexViewModel::color
    val label by vertexViewModel::label

    val strokeWidth
        get() = 8f * canvasViewModel.zoom
    val radius
        get() = vertexViewModel.radius * canvasViewModel.zoom
    val offset
        get() = calculateOffset()

    val textSize
        get() = vertexViewModel.radius * 0.6f * canvasViewModel.zoom

    fun onDrag(it: Offset): Unit {
        vertexViewModel.onDrag(it * (1f / canvasViewModel.zoom))
    }

    fun onClick(vm: VertexCanvasViewModel) {
        canvasViewModel.onClick(this)
    }

    private fun calculateOffset() = Offset(
        (canvasViewModel.canvasSize.x / 2) + ((vertexViewModel.x - canvasViewModel.center.x) * canvasViewModel.zoom),
        (canvasViewModel.canvasSize.y / 2) + ((vertexViewModel.y - canvasViewModel.center.y) * canvasViewModel.zoom)
    )
}