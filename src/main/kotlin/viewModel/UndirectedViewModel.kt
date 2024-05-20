package viewModel

import androidx.compose.runtime.mutableStateOf
import model.graph.UndirectedGraph
import model.graph.Vertex

class UndirectedViewModel<V>(
    private val graph: UndirectedGraph<V>,
    showVerticesLabels: Boolean,
) {
    private val _vertices = hashMapOf<Vertex<V>, VertexViewModel<V>>()
    private val _adjacencyList = hashMapOf<VertexViewModel<V>, ArrayList<EdgeViewModel<V>>>()

    val vertices
        get() = _vertices.values

    val adjacencyList
        get() = _adjacencyList

    init {
        graph.vertices.forEach { vertex ->
            val vertexViewModel =
                VertexViewModel(
                    showVerticesLabels,
                    vertex,
                    x = (-1000..1000).random().toFloat(),
                    y = (-1000..1000).random().toFloat()
                )

            _vertices[vertex] = vertexViewModel
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