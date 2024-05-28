package view.graph

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import viewModel.graph.UndirectedViewModel

@Composable
fun <V> UndirectedGraphView(
    viewModel: UndirectedViewModel<V>
) {
    Box(Modifier.fillMaxSize()) {
        viewModel.vertices.forEach { v ->
            VertexView(v)
        }

        viewModel.vertices.forEach { v ->
            viewModel.adjacencyList[v]?.forEach { e ->
                EdgeView(e)
            }
        }
    }
}