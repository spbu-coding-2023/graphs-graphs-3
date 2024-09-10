package viewModel.canvas

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import viewModel.graph.VertexViewModel

class VertexCanvasViewModel(
    val vertexViewModel: VertexViewModel,
    private val zoom: MutableState<Float>,
    private val center: MutableState<Offset>,
    private val canvasSize: MutableState<Offset>
) {
    private val _offset = mutableStateOf(
        calculateOffset()
    )
    var offset
        get() = _offset.value
        set(value) {
            _offset.value = value
        }

    private val _radius = mutableStateOf(calculateRadius())
    var radius
        get() = _radius.value
        set(value) {
            _radius.value = value
        }

    var color
        get() = vertexViewModel.color
        set(value) {
            vertexViewModel.color = value
        }

    private val _strokeWidth = mutableStateOf(calculateStrokeWidth())
    var strokeWidth
        get() = _strokeWidth.value
        set(value) {
            _strokeWidth.value = value
        }

    private val _textSize = mutableStateOf(calculateTextSize())
    var textSize
        get() = _textSize.value
        set(value) {
            _textSize.value = value
        }

    fun onDrag(it: Offset): Unit {
        vertexViewModel.onDrag(it * (1f / zoom.value))
    }

    fun updateVertex() {
        offset = calculateOffset()
        radius = calculateRadius()
        strokeWidth = calculateStrokeWidth()
        textSize = calculateTextSize()
    }

    private fun calculateOffset() = Offset(
        (canvasSize.value.x / 2) + ((vertexViewModel.x - center.value.x) * zoom.value),
        (canvasSize.value.y / 2) + ((vertexViewModel.y - center.value.y) * zoom.value)
    )

    private fun calculateRadius() = vertexViewModel.radius * zoom.value
    private fun calculateStrokeWidth() = 8f * zoom.value
    private fun calculateTextSize() = vertexViewModel.radius * 0.6f * zoom.value
}