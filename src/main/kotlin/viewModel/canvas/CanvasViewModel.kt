package viewModel.canvas

import Config
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.AwaitPointerEventScope
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.input.pointer.PointerInputScope
import model.algorithm.Dijkstra
import model.algorithm.FindCycle
import model.graph.Edge
import model.graph.UndirectedGraph
import view.HEADER_HEIGHT
import view.MENU_WIDTH
import viewModel.graph.EdgeViewModel
import viewModel.graph.UndirectedViewModel
import viewModel.graph.VertexViewModel

class CanvasViewModel(
    val graph: UndirectedGraph,
) {
    private val graphViewModel = UndirectedViewModel(graph, true)

    var isClustering by graphViewModel::clustering
    var isRanked by graphViewModel::ranked
    var isFinded by graphViewModel::bridgeFinded

    var isEdgeCreatingMode by mutableStateOf(false)
    var isDijkstraMode by mutableStateOf(false)
    var pickedNodeForEdgeCreating by mutableStateOf<VertexCanvasViewModel?>(null)
    var pickedNodeForDijkstra by mutableStateOf<VertexCanvasViewModel?>(null)

    var isEdgeFindCycleMode by mutableStateOf(false)

    var isNodeCreatingMode by mutableStateOf(false)
    var edgesCount by mutableStateOf(0)
    var zoom by mutableStateOf(1f)
    var center by mutableStateOf(Offset(0f, 0f))
    var canvasSize by mutableStateOf(Offset(400f, 400f))
    var isOrientated by mutableStateOf(false)

    private val _vertices = mutableStateMapOf<VertexViewModel, VertexCanvasViewModel>()
    private val _edges = mutableStateMapOf<VertexCanvasViewModel, ArrayList<EdgeCanvasViewModel>>()

    private fun getVertex(vm: VertexViewModel): VertexCanvasViewModel {
        return _vertices[vm] ?: throw IllegalArgumentException("There is no VertexCanvasViewModel for $vm")
    }

    fun createNode(offset: Offset) {
        if (isNodeCreatingMode) {
            val coordinates = (offset - (canvasSize / 2.0F)) * (1 / zoom) + center
            val viewModel = graphViewModel.createVertex(coordinates) ?: return

            _vertices[viewModel] = VertexCanvasViewModel(viewModel, this)
            _edges[getVertex(viewModel)] = ArrayList()
        }
    }

    init {
        graphViewModel.vertices.forEach { v ->
            _vertices[v] = VertexCanvasViewModel(v, this)
        }

        graphViewModel.adjacencyList.forEach {
            _edges[getVertex(it.key)] = ArrayList(it.value.map { edgeViewModel ->
                val vertex1 = getVertex(edgeViewModel.first)
                val vertex2 = getVertex(edgeViewModel.second)

                EdgeCanvasViewModel(vertex1, vertex2, edgeViewModel, this)
            }.toList())
        }

        edgesCount = _edges.values.flatten().size
    }

    val vertices
        get() = _vertices.values

    val edges
        get() = _edges.values

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

    /*
    * Change edges' color
    * */
    fun changeEdgesColor(edges: MutableList<Pair<Edge, Color>>) {
        println(edges)
        graphViewModel.changeEdgesColor(edges)
    }

    fun resetEdgesColorToDefault() {
        println("reset")
        graphViewModel.resetEdgesColorToDefault()
    }

    fun createEdge(first: VertexCanvasViewModel, second: VertexCanvasViewModel) {
        val edgesVM = graphViewModel.createEdge(first.vertexViewModel, second.vertexViewModel)
        val firstCanvasEdgeList = _edges[first] ?: return
        val secondCanvasEdgeList = _edges[second] ?: return

        if (edgesVM != null) {
            firstCanvasEdgeList.add(EdgeCanvasViewModel(first, second, edgesVM.first, this))
            secondCanvasEdgeList.add(EdgeCanvasViewModel(second, first, edgesVM.second, this))
        }

        edgesCount++
    }

    fun onClick(vm: VertexCanvasViewModel) {
        onClickNodeEdgeCreating(vm)
        onClickNodeFindCycle(vm)
        onClickNodeDijkstraOn(vm)
    }

    fun onClickNodeEdgeCreating(vm: VertexCanvasViewModel) {
        if (!isEdgeCreatingMode) return

        if (pickedNodeForEdgeCreating == vm) {
            pickedNodeForEdgeCreating = null
            return
        }

        if (pickedNodeForEdgeCreating == null) {
            pickedNodeForEdgeCreating = vm
            return
        }

        createEdge(pickedNodeForEdgeCreating ?: return, vm)
        pickedNodeForEdgeCreating = null
    }

    fun onClickNodeFindCycle(vm: VertexCanvasViewModel) {
        if (!isEdgeFindCycleMode) return

        resetEdgesColorToDefault()
        println(graph)
        changeEdgesColor(FindCycle(graph).calculate(vm.vertexViewModel.vertex).map { Pair(it, Color.Red) }
            .toMutableList())
    }

    fun onClickNodeDijkstraOn(vm: VertexCanvasViewModel) {
        if (!isDijkstraMode) {
            return
        }

        if (pickedNodeForDijkstra == vm) {
            pickedNodeForDijkstra = null
            return
        }

        if (pickedNodeForDijkstra == null) {
            pickedNodeForDijkstra = vm
            return
        }

        val firstVertex =
            pickedNodeForDijkstra ?: throw IllegalStateException("there is no node in pickedNodeForDijkstra method")

        val dijksta = Dijkstra(graph)
        val path = dijksta.findShortestPath(firstVertex.vertexViewModel.getKey(), vm.vertexViewModel.getKey()) ?: return
        val edges = dijksta.triplesToEdges(path)

        val PathWthColor = edges.map { it to Config.Edge.dijkstraColor }
        resetEdgesColorToDefault()
        changeEdgesColor(PathWthColor.toMutableList())
        pickedNodeForDijkstra = null
    }
}