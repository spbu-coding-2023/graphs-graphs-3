package view

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import viewModel.CanvasViewModel

@Composable
fun <V> CanvasView(
    viewModel: CanvasViewModel<V>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        viewModel.edges.forEach {
            EdgeCanvasView(it)
        }

        viewModel.vertices.forEach {
            VertexCanvasView(it)
        }
    }
}