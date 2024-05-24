package viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import model.graph.UndirectedGraph
import model.graph.Vertex

class UndirectedViewModel<V>(
    private val graph: UndirectedGraph<V>,
    showVerticesLabels: Boolean,
    val groups: HashMap<Vertex<V>, Int> = hashMapOf()
) {
    private val _vertices = hashMapOf<Vertex<V>, VertexViewModel<V>>()
    private val _adjacencyList = hashMapOf<VertexViewModel<V>, ArrayList<EdgeViewModel<V>>>()
    private val groupColors = hashMapOf<Int, Color>(0 to Color.Black)

    val vertices
        get() = _vertices.values

    val adjacencyList
        get() = _adjacencyList

    private fun getColor(group: Int): Color {
        val color = groupColors[group]

        if (color == null) {
            val newColor = Color((0..255).random(), (0..255).random(), (0..255).random())
            groupColors[group] = newColor
            return newColor
        }

        return color
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
            )
            _vertices[vertex] = vertexViewModel

            fun setOffsetEdges(vertex: Vertex<V>, from: Offset) {
                val edges = graph.adjacencyList[vertex] ?: return
                edges.forEach { edge ->
                    val second = edge.second
                    if (_vertices[second] != null) return@forEach

                    val secondVertexViewModel = VertexViewModel(
                        showVerticesLabels,
                        vertex,
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
            val arrayList = arrayListOf<EdgeViewModel<V>>()
            val vertexVM1 = _vertices[vertex] ?: throw IllegalStateException()

            graph.adjacencyList[vertex]?.forEach { edge ->
                val vertexVM2 = _vertices[edge.second] ?: throw IllegalStateException()

                arrayList.add(EdgeViewModel(vertexVM1, vertexVM2, edge, mutableStateOf(false)))
            }

            _adjacencyList[vertexVM1] = arrayList
        }
    }
}