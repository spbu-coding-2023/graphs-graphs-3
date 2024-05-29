package view.canvas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import view.MyText
import viewModel.canvas.VertexCanvasViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VertexCanvasView(
    viewModel: VertexCanvasViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .size(viewModel.radius * 2)
            .offset(viewModel.offset.x.dp, viewModel.offset.y.dp)
            .border(color = viewModel.color, width = viewModel.strokeWidth.dp, shape = CircleShape)
            .background(color = Color(0xFF242424), shape = CircleShape)
            .onDrag(onDrag = viewModel::onDrag),
        contentAlignment = Alignment.Center
    ) {
        MyText(viewModel.viewModel.label, viewModel.textSize.value)
    }
}