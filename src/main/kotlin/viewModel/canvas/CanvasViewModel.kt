package viewModel.canvas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputScope
import model.graph.UndirectedGraph
import view.HEADER_HEIGHT
import view.MENU_WIDTH
import viewModel.graph.UndirectedViewModel
import viewModel.graph.VertexViewModel

class CanvasViewModel(
    val graph: UndirectedGraph,
) {
    private val graphViewModel = UndirectedViewModel(graph, true)

    var isClustering
        get() = graphViewModel.clustering
        set(value) {
            graphViewModel.clustering = value
        }

    var isRanked
        get() = graphViewModel.ranked
        set(value) {
            graphViewModel.ranked = value
        }

    val _isNodeCreatingMode = mutableStateOf(false)
    var isNodeCreatingMode
        get() = _isNodeCreatingMode.value
        set(value) {
            _isNodeCreatingMode.value = value
        }

    fun createNode(offset: Offset) {
        if (isNodeCreatingMode) {
            val coordinates = (offset - (canvasSize / 2.0F)) * (1 / zoom) + center
            println(offset - (canvasSize / 2.0F))
            val viewModel = graphViewModel.createVertex(coordinates) ?: return

            _vertices[viewModel] = VertexCanvasViewModel(viewModel, _zoom, _center, _canvasSize)
        }
    }


    private val _zoom = mutableStateOf(1f)
    var zoom
        get() = _zoom.value
        set(value) {
            _zoom.value = value
            updateEdges()
        }

    private val _center = mutableStateOf(Offset(0f, 0f))
    var center
        get() = _center.value
        set(value) {
            _center.value = value
        }

    private val _canvasSize = mutableStateOf(Offset(400f, 400f))
    var canvasSize
        get() = _canvasSize.value
        set(value) {
            _canvasSize.value = value
        }

    private val _isOrientated = mutableStateOf(false)
    var isOrientated
        get() = _isOrientated.value
        set(value) {
            _isOrientated.value = value
        }

    private val _vertices = mutableStateMapOf<VertexViewModel, VertexCanvasViewModel>()

    init {
        graphViewModel.vertices.forEach { v ->
            _vertices[v] = VertexCanvasViewModel(v, _zoom, _center, _canvasSize)
        }
    }

    private val _edges = graphViewModel.adjacencyList.map { it.value }.flatten().map {
        val vertex1 =
            _vertices[it.first] ?: throw IllegalStateException("There is no VertexCanvasViewModel for ${it.first}")
        val vertex2 =
            _vertices[it.second] ?: throw IllegalStateException("There is no VertexCanvasViewModel for ${it.second}")

        EdgeCanvasViewModel(vertex1, vertex2, it.color, it.strokeWidth, zoom, _isOrientated)
    }

    val vertices
        get() = _vertices.values

    val edges
        get() = _edges

    private fun updateEdges() {
        edges.forEach { it.updateEdge(zoom) }
    }

    val onScroll: AwaitPointerEventScope.(PointerEvent) -> Unit = {
        if (it.changes.first().scrollDelta.y > 0) {
            zoom -= zoom / 8
        } else {
            zoom += zoom / 8

            val awtEvent = it.awtEventOrNull
            if (awtEvent != null) {
                val xPosition = awtEvent.x.toFloat() - MENU_WIDTH
                val yPosition = awtEvent.y.toFloat() - HEADER_HEIGHT
                val pointerVector =
                    (Offset(xPosition, yPosition) - (canvasSize / 2f)) * (1 / zoom)
                center += pointerVector * 0.15f
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    val onDrag: suspend PointerInputScope.() -> Unit = {
        detectDragGestures(
            matcher = PointerMatcher.Primary
        ) {
            center -= it * (1 / zoom)
        }
    }

    fun onColorChange(color: Color) {
        graphViewModel.onColorChange(color)
    }

    fun onSizeChange(newSize: Float) {
        graphViewModel.onSizeChange(newSize)
    }

    fun onOrientatedChange(isOrientated: Boolean) {
        this.isOrientated = isOrientated
    }

//    fun getViews(): Collection<VertexCanvasViewModel> {
//        if (Config.optimizeCanvas) {
//            return _vertices.filter { abs(it.value.offset.x) < canvasSize.x && abs(it.value.offset.y) < canvasSize.y }.values
//        }
//
//        return _vertices.values
//    }
}