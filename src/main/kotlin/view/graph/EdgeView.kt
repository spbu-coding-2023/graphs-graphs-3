package view.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import viewModel.graph.EdgeViewModel

@Composable
fun EdgeView(
    viewModel: EdgeViewModel
) {
    Canvas(Modifier.fillMaxSize()) {
        drawLine(
            start = Offset(
                viewModel.first.x.dp.toPx() + viewModel.first.radius.toPx(),
                viewModel.first.y.dp.toPx() + viewModel.first.radius.toPx(),
            ),
            end = Offset(
                viewModel.second.x.dp.toPx() + viewModel.second.radius.toPx(),
                viewModel.second.y.dp.toPx() + viewModel.second.radius.toPx(),
            ),
            color = Color.Black,
            strokeWidth = 1f.dp.toPx()
        )
    }
}