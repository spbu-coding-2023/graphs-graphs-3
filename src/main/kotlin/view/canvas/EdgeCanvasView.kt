package view.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import viewModel.canvas.EdgeCanvasViewModel

@Composable
fun EdgeCanvasView(
    viewModel: EdgeCanvasViewModel,
    modifier: Modifier = Modifier
) {

    Canvas(Modifier.fillMaxSize()) {
        // something hard thing for drawing edge from border of node, not from center
        val firstCenter =
            viewModel.first.offset + Offset(
                viewModel.first.radius.value,
                viewModel.first.radius.value
            )
        val secondCenter =
            viewModel.second.offset + Offset(
                viewModel.second.radius.value,
                viewModel.second.radius.value
            )

        val vector = (secondCenter - firstCenter)
        val vectorNorm = vector / vector.getDistance()
        val radiusVectorFirst = vectorNorm * viewModel.first.radius.value
        val radiusVectorSecond = vectorNorm * viewModel.second.radius.value

        val start = firstCenter + radiusVectorFirst
        val end = secondCenter - radiusVectorSecond

        if ((secondCenter - firstCenter).getDistance() > viewModel.first.radius.value + viewModel.second.radius.value) {
            drawLine(
                start = start,
                end = end,
                color = viewModel.color,
                strokeWidth = viewModel.strokeWidth.dp.toPx()
            )
        }

        if (viewModel.showOrientation) {
            drawLine(
                start = end,
                end = end - rotateVector(radiusVectorSecond * 0.8f, 30.0),
                color = viewModel.color,
                strokeWidth = viewModel.strokeWidth * 0.8f
            )

            drawLine(
                start = end,
                end = end - rotateVector(radiusVectorSecond * 0.8f, -30.0),
                color = viewModel.color,
                strokeWidth = viewModel.strokeWidth * 0.8f
            )
        }
    }
}

fun rotateVector(vec: Offset, angle: Double): Offset {
    val radians = Math.toRadians(angle)
    val cos = Math.cos(radians).toFloat()
    val sin = Math.sin(radians).toFloat()
    return Offset(vec.x * cos - sin * vec.y, sin * vec.x + cos * vec.y)
}