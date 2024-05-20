package view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import viewModel.EdgeCanvasViewModel

@Composable
fun <V> EdgeCanvasView(
    viewModel: EdgeCanvasViewModel<V>,
    modifier: Modifier = Modifier
) {
    Canvas(Modifier.fillMaxSize()) {
        drawLine(
            start = Offset(
                viewModel.first.offset.x.dp.toPx() + viewModel.first.radius.toPx(),
                viewModel.first.offset.y.dp.toPx() + viewModel.first.radius.toPx(),
            ),
            end = Offset(
                viewModel.second.offset.x.dp.toPx() + viewModel.second.radius.toPx(),
                viewModel.second.offset.y.dp.toPx() + viewModel.second.radius.toPx(),
            ),
            color = Color.Black,
            strokeWidth = 1f.dp.toPx()
        )
    }
}