package viewModel.graph

import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import model.graph.Vertex

class VertexViewModel(
    private val _labelVisible: Boolean,
    val vertex: Vertex,
    x: Float = 0f,
    y: Float = 0f,
    color: Color = Color.Black,
    radius: Dp = 8f.dp,
) {
    var x by mutableStateOf(x)
    var y by mutableStateOf(y)
    var color by mutableStateOf(color)
    var radius by mutableStateOf(radius)

    val label
        get() = vertex.key.toString()

    val labelVisibility
        get() = _labelVisible

    fun getKey(): Int {
        return vertex.key
    }

    fun onDrag(it: Offset): Unit {
        x += it.x
        y += it.y
    }

    override fun toString(): String {
        return "VertexViewModel ${vertex.key}"
    }
}