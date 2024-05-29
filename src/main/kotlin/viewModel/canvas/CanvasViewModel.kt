package viewModel.canvas

import androidx.compose.ui.geometry.Offset
import viewModel.graph.UndirectedViewModel
import viewModel.graph.VertexViewModel
import kotlin.math.abs

class CanvasViewModel(
    val graphViewModel: UndirectedViewModel,
    var zoom: Float,
    var center: Offset,
    var canvasSize: Offset,
    var isOrientated: Boolean
) {
    private val _vertices = graphViewModel.vertices.associateWith { v ->
        VertexCanvasViewModel(v, zoom, center, canvasSize)
    }.toMutableMap()

    private val _edges = graphViewModel.adjacencyList.map { it.value }.flatten().map {
        val vertex1 =
            _vertices[it.first] ?: throw IllegalStateException("There is no VertexCanvasViewModel for ${it.first}")
        val vertex2 =
            _vertices[it.second] ?: throw IllegalStateException("There is no VertexCanvasViewModel for ${it.second}")

        EdgeCanvasViewModel(vertex1, vertex2, it.color, it.strokeWidth, zoom, showOrientation = isOrientated)
    }

    val vertices
        get() = _vertices.values

    val edges
        get() = _edges

    fun getViews(): Collection<VertexCanvasViewModel> {
        if (Config.optimizeCanvas) {
            return _vertices.filter { abs(it.value.offset.x) < canvasSize.x && abs(it.value.offset.y) < canvasSize.y }.values
        }

        return _vertices.values
    }

    fun createVertex(offset: Offset) {
        val coordinates = offset * zoom + center
        val viewModel = graphViewModel.createVertex(coordinates) ?: return

        _vertices[viewModel] = VertexCanvasViewModel(viewModel, zoom, center, canvasSize)
    }
}