package viewModel.graph

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import model.graph.Edge

class EdgeViewModel<V>(
    val first: VertexViewModel<V>,
    val second: VertexViewModel<V>,
    private val edge: Edge<V>,
    private val _weightVisibility: State<Boolean>
) {
    private var _weight = mutableStateOf(edge.weight)
    var weight
        get() = _weight.value
        set(value) {
            _weight.value = value
            edge.weight = value
        }
}