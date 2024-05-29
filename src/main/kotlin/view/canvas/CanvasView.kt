package view.canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import viewModel.canvas.CanvasViewModel

@Composable
fun CanvasView(
    viewModel: CanvasViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(Color(0xFF242424))
    ) {
        viewModel.edges.forEach {
            EdgeCanvasView(it)
        }

        viewModel.getViews().forEach {
            VertexCanvasView(it)
        }
    }
}