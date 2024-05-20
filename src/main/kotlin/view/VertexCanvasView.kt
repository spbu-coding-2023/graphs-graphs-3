package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewModel.VertexCanvasViewModel

@Composable
fun <V> VertexCanvasView(
    viewModel: VertexCanvasViewModel<V>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .size(viewModel.radius * 2)
            .offset(viewModel.offset.x.dp, viewModel.offset.y.dp)
            .background(color = viewModel.color, shape = CircleShape)
    )
}