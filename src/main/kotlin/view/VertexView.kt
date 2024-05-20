package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import viewModel.VertexViewModel

@Composable
fun <V> VertexView(
    viewModel: VertexViewModel<V>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(viewModel.radius * 2)
            .offset(viewModel.x.dp, viewModel.y.dp)
            .background(color = viewModel.color, shape = CircleShape)
    ) {
        if (viewModel.labelVisibility) {
            Text(
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(0f.dp, -viewModel.radius - 10f.dp),
                text = viewModel.label
            )
        }
    }
}