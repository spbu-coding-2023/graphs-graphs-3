package viewModel.canvas

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import viewModel.graph.VertexViewModel

class VertexCanvasViewModel(
    val vertexViewModel: VertexViewModel,
    private val zoom: MutableState<Float>,
    private val center: MutableState<Offset>,
    private val canvasSize: MutableState<Offset>
) {
    var color by vertexViewModel::color

    val strokeWidth
        get() = 8f * zoom.value
    val radius
        get() = vertexViewModel.radius * zoom.value
    val offset
        get() = calculateOffset()
    val textSize
        get() = vertexViewModel.radius * 0.6f * zoom.value

    fun onDrag(it: Offset): Unit {
        vertexViewModel.onDrag(it * (1f / zoom.value))
    }

    private fun calculateOffset() = Offset(
        (canvasSize.value.x / 2) + ((vertexViewModel.x - center.value.x) * zoom.value),
        (canvasSize.value.y / 2) + ((vertexViewModel.y - center.value.y) * zoom.value)
    )
}