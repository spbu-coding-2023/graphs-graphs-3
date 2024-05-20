package view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import viewModel.CanvasViewModel
import viewModel.UndirectedViewModel

val HEADER_HEIGHT = 50f
val MENU_WIDTH = 100f

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun <V> MainView(undirectedViewModel: UndirectedViewModel<V>) {
    var zoom by remember { mutableFloatStateOf(1f) }
    var center by remember { mutableStateOf(Offset(0f, 0f)) }
    var canvasSize by remember { mutableStateOf(Offset(400f, 400f)) }

    val canvasViewModel = CanvasViewModel(undirectedViewModel, zoom, center, canvasSize)

    Row(
        Modifier.fillMaxWidth().height(50f.dp).background(color = Color.Gray),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Some Header", color = Color.White, fontSize = 20f.sp)
    }

    Row(Modifier.offset(0f.dp, 50f.dp)) {
        Column(
            Modifier.fillMaxHeight().width(100f.dp).background(color = Color.Black),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Some menu", color = Color.White, fontSize = 20f.sp)
        }

        CanvasView(
            canvasViewModel,
            Modifier
                .fillMaxSize()
                .border(1f.dp, Color.Red)
                .onPointerEvent(PointerEventType.Scroll) {
                    if (it.changes.first().scrollDelta.y > 0) {
                        zoom -= zoom / 8
                    } else {
                        zoom += zoom / 8

                        val awtEvent = it.awtEventOrNull
                        if (awtEvent != null) {
                            val xPosition = awtEvent.x.toFloat() - MENU_WIDTH
                            val yPosition = awtEvent.y.toFloat() - HEADER_HEIGHT
                            val pointerVector = (Offset(xPosition, yPosition) - (canvasSize / 2f)) * (1 / zoom)
                            center += pointerVector * 0.15f
                        }
                    }
                }.pointerInput(Unit) {
                    detectDragGestures(
                        matcher = PointerMatcher.Primary
                    ) {
                        center -= it * (1 / zoom)
                    }
                }.pointerHoverIcon(PointerIcon.Hand)
                .onSizeChanged {
                    canvasSize = Offset(it.width.toFloat(), it.height.toFloat())
                }
                .clipToBounds()
        )
    }
}