package viewModel.graph

import Config
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import model.graph.Edge

class EdgeViewModel(
    val first: VertexViewModel,
    val second: VertexViewModel,
    val edge: Edge,
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

    var color by mutableStateOf(color)
    var strokeWidth by mutableStateOf(strokeWidth)
}
