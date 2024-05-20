package viewModel

import androidx.compose.ui.geometry.Offset

class CanvasViewModel<V>(
    graphViewModel: UndirectedViewModel<V>,
    var zoom: Float,
    var center: Offset,
    var canvasSize: Offset
) {
    private val _vertices = graphViewModel.vertices.associateWith { v ->
        VertexCanvasViewModel(v, zoom, center, canvasSize)
    }

    private val _edges = graphViewModel.adjacencyList.map { it.value }.flatten().map {
        val vertex1 =
            _vertices[it.first] ?: throw IllegalStateException("There is no VertexCanvasViewModel for ${it.first}")
        val vertex2 =
            _vertices[it.second] ?: throw IllegalStateException("There is no VertexCanvasViewModel for ${it.second}")

        EdgeCanvasViewModel(vertex1, vertex2)
    }

    val vertices
        get() = _vertices.values

    val edges
        get() = _edges
}