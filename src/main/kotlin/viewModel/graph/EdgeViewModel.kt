package viewModel.graph

import Config
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import model.graph.Edge

class EdgeViewModel(
    val first: VertexViewModel,
    val second: VertexViewModel,
    private val edge: Edge,
    private val _weightVisibility: State<Boolean>,
    color: Color = Config.Edge.color,
    strokeWidth: Float = Config.Edge.strokeWidth
) {
    private var _weight = mutableStateOf(edge.weight)
    var weight
        get() = _weight.value
        set(value) {
            _weight.value = value
            edge.weight = value
        }

    private var _color = mutableStateOf(color)
    var color
        get() = _color.value
        set(value) {
            _color.value = value
        }

    private var _strokeWidth = mutableStateOf(strokeWidth)
    var strokeWidth
        get() = _strokeWidth.value
        set(value) {
            _strokeWidth.value = value
        }
}
