package viewModel.graph

import Config
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import model.algorithm.Clustering
import model.algorithm.FindBridges
import model.algorithm.PageRank
import model.graph.Edge
import model.graph.UndirectedGraph
import model.graph.Vertex

class UndirectedViewModel(
    private val graph: UndirectedGraph,
    val showVerticesLabels: Boolean,
    var groups: HashMap<Vertex, Int> = hashMapOf(),
    var ranks: List<Pair<Vertex, Double>> = listOf(),
    var bridges: List<Edge> = listOf()
) {
    private val _vertices = hashMapOf<Vertex, VertexViewModel>()
    private val _adjacencyList = hashMapOf<VertexViewModel, ArrayList<EdgeViewModel>>()
    private val groupColors = hashMapOf<Int, Color>(0 to Color.Black)
    private val BridgesWthColor = mutableListOf<Pair<Edge, Color>>()

    private val _color = mutableStateOf(Color.Black)
    private val _clustering = mutableStateOf(false)
    private val _ranked = mutableStateOf(false)
    private val _bridgeFinded = mutableStateOf(false)

    private var size by mutableStateOf(10f)

    val vertices
        get() = _vertices.values

    val adjacencyList
        get() = _adjacencyList

    var clustering
        get() = _clustering.value
        set(value) {
            _clustering.value = value
            groups = Clustering(graph).calculate()
            updateColor()
        }

    var ranked
        get() = _ranked.value
        set(value) {
            _ranked.value = value
            ranks = PageRank(graph).computePageRank(3)
            println("хуй")
            updateSizes()
        }
    
    var bridgeFinded
        get() = _bridgeFinded.value
        set(value) {
            _bridgeFinded.value = value
            bridges = FindBridges(graph).findBridges()
            bridges.forEach {
                BridgesWthColor.add(it to Color.Red)
            }
            if (bridgeFinded) {
                changeEdgesColor(BridgesWthColor)
            }
            else{
                resetEdgesColorToDefault()
            }
        }

    private fun getColor(group: Int): Color {
        if (clustering) {
            val color = groupColors[group]

            if (color == null) {
                val newColor = Color((0..255).random(), (0..255).random(), (0..255).random())
                groupColors[group] = newColor
                return newColor
            }

            return color
        }

        return _color.value
    }

    fun updateColor() {
        _vertices.forEach {
            it.value.color = getColor(groups.getOrDefault(it.key, 0))
        }
    }

    fun updateSizes() {
        if (ranked) {
            ranks.forEach {
                val vertex = _vertices[it.first] ?: return

                vertex.radius = (vertex.radius.value * 2f).dp
            }

            return
        }

        _vertices.forEach { it.value.radius = size.dp }
    }

    fun onColorChange(color: Color) {
        _color.value = color
        updateColor()
    }

    fun onSizeChange(newSize: Float) {
        size = newSize
        _vertices.forEach {
            it.value.radius = size.dp
        }
        updateSizes()
    }

    fun createVertex(coordinates: Offset): VertexViewModel? {
        val vertex = graph.addVertex(graph.vertices.last().key + 1) ?: return null

        val viewModel = VertexViewModel(
            showVerticesLabels,
            vertex,
            coordinates.x - size,
            coordinates.y - size,
            getColor(groups.getOrDefault(vertex, 0)),
            radius = size.dp
        )

        _vertices[vertex] = viewModel

        return viewModel
    }

    /*
    * Change edges' color
    * */
    fun changeEdgesColor(edges: MutableList<Pair<Edge, Color>>) {
        edges.forEach { p ->
            val edge = p.first
            val color = p.second

            val vertex1 = _vertices[edge.first] ?: return
            val vertex2 = _vertices[edge.second] ?: return

            val edgeViewModelList1 = _adjacencyList[vertex1] ?: return
            val edgeViewModel1 = edgeViewModelList1.find { it.second == vertex2 } ?: return
            edgeViewModel1.color = color

            val edgeViewModelList2 = _adjacencyList[vertex2] ?: return
            val edgeViewModel2 = edgeViewModelList2.find { it.second == vertex1 } ?: return
            edgeViewModel2.color = color
        }
    }

    /*
    * Reset current color on all edges to default in Config
    * */
    fun resetEdgesColorToDefault() {
        adjacencyList.values.flatten().forEach { it.color = Config.Edge.color }
    }

    init {
        graph.vertices.forEachIndexed { i, vertex ->
            val group = groups.getOrDefault(vertex, 0)

            if (_vertices[vertex] != null) return@forEachIndexed

            val vertexViewModel = VertexViewModel(
                showVerticesLabels,
                vertex,
                (-1000..1000).random().toFloat(),
                (-1000..1000).random().toFloat(),
                getColor(group),
                radius = size.dp
            )
            _vertices[vertex] = vertexViewModel

            fun setOffsetEdges(vertex: Vertex, from: Offset) {
                val edges = graph.adjacencyList[vertex] ?: return
                edges.forEach { edge ->
                    val second = edge.second
                    if (_vertices[second] != null) return@forEach

                    val secondVertexViewModel = VertexViewModel(
                        showVerticesLabels,
                        second,
                        (listOf(1f, -1f).random() * (100..200).random().toFloat()) + from.x,
                        (listOf(1f, -1f).random() * (100..200).random().toFloat()) + from.y,
                        getColor(groups.getOrDefault(second, 0)),
                    )

                    _vertices[second] = secondVertexViewModel
                    setOffsetEdges(second, Offset(secondVertexViewModel.x, secondVertexViewModel.y))
                }
            }

            setOffsetEdges(vertex, Offset(vertexViewModel.x, vertexViewModel.y))
        }

        graph.vertices.forEach { vertex ->
            val arrayList = arrayListOf<EdgeViewModel>()
            val vertexVM1 = _vertices[vertex] ?: throw IllegalStateException()

            graph.adjacencyList[vertex]?.forEach { edge ->
                val vertexVM2 = _vertices[edge.second] ?: throw IllegalStateException()

                arrayList.add(EdgeViewModel(vertexVM1, vertexVM2, edge, mutableStateOf(false)))
            }

            _adjacencyList[vertexVM1] = arrayList
        }
    }
}