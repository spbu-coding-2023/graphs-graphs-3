package viewModel.graph

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.graph.Vertex

class VertexViewModel<V>(
    private val _labelVisible: Boolean,
    private val vertex: Vertex<V>,
    x: Float = 0f,
    y: Float = 0f,
    color: Color = Color.Black,
    radius: Dp = 8f.dp,
) {
    private val _x = mutableStateOf(x)
    private val _y = mutableStateOf(y)
    private val _color = mutableStateOf(color)
    private val _radius = mutableStateOf(radius)

    var x: Float
        get() = _x.value
        set(value) {
            _x.value = value
        }

    var y: Float
        get() = _y.value
        set(value) {
            _y.value = value
        }

    var color: Color
        get() = _color.value
        set(value) {
            _color.value = value
        }
    var radius: Dp
        get() = _radius.value
        set(value) {
            _radius.value = value
        }

    val label
        get() = vertex.key.toString()

    val labelVisibility
        get() = _labelVisible
}