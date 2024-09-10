package view.canvas

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onSizeChanged
import viewModel.canvas.CanvasViewModel

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun CanvasView(
    viewModel: CanvasViewModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(Color(0xFF242424))
            .onPointerEvent(PointerEventType.Scroll, onEvent = viewModel.onScroll)
            .pointerInput(Unit, viewModel.onDrag)
            .pointerInput(Unit) {
                detectTapGestures {
                    viewModel.createNode(it)
                }
            }
            .pointerHoverIcon(PointerIcon.Hand)
            .onSizeChanged {
                viewModel.canvasSize = Offset(it.width.toFloat(), it.height.toFloat())
            }
            .clipToBounds()
    ) {
        viewModel.edges.forEach {
            EdgeCanvasView(it)
        }

        viewModel.vertices.forEach {
            VertexCanvasView(it)
        }
    }
}