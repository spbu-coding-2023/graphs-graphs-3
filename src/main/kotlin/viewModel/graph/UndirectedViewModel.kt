package viewModel.graph

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.unit.dp
import model.graph.UndirectedGraph
import model.graph.Vertex

class UndirectedViewModel(
    private val graph: UndirectedGraph,
    val showVerticesLabels: Boolean,
    val groups: HashMap<Vertex, Int> = hashMapOf(),
) {
    private val _vertices = hashMapOf<Vertex, VertexViewModel>()
    private val _adjacencyList = hashMapOf<VertexViewModel, ArrayList<EdgeViewModel>>()
    private val groupColors = hashMapOf<Int, Color>(0 to Color.Black)
    private val _color = mutableStateOf(Color.Black)
    private val _size = mutableStateOf(10f)
    private val _clustering = mutableStateOf(false)

    private var size
        get() = _size.value
        set(value) {
            _size.value = value
        }

    val vertices
        get() = _vertices.values

    val adjacencyList
        get() = _adjacencyList

    var clustering
        get() = _clustering.value
        set(value) {
            _clustering.value = value
            updateColor()
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

    fun onColorChange(color: Color) {
        _color.value = color
        updateColor()
    }

    fun onSizeChange(newSize: Float) {
        size = newSize
        _vertices.forEach {
            it.value.radius = size.dp
        }
    }

    fun createVertex(coordinates: Offset): VertexViewModel? {
        val vertex = graph.addVertex(graph.vertices.last().key + 1)

        if (vertex == null) return null

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
                        (listOf(1f, -1f).random() * (40..90).random().toFloat()) + from.x,
                        (listOf(1f, -1f).random() * (40..90).random().toFloat()) + from.y,
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