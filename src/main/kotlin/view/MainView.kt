package view

import Config
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.PointerMatcher
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.awtEventOrNull
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.dp
import view.canvas.CanvasView
import viewModel.canvas.CanvasViewModel
import viewModel.graph.UndirectedViewModel

val HEADER_HEIGHT = Config.headerHeight
val MENU_WIDTH = Config.menuWidth

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun <V> MainView(undirectedViewModel: UndirectedViewModel<V>) {
    var zoom by remember { mutableFloatStateOf(1f) }
    val zoomAnimate by animateFloatAsState(zoom, tween(200, 0, LinearOutSlowInEasing))
    var center by remember { mutableStateOf(Offset(0f, 0f)) }
    val centerAnimate by animateOffsetAsState(center, tween(200, 0, LinearOutSlowInEasing))
    var canvasSize by remember { mutableStateOf(Offset(400f, 400f)) }

    var isOrientated by remember { mutableStateOf(false) }
    var isClustering by remember { mutableStateOf(false) }
    undirectedViewModel.clustering = isClustering

    val canvasViewModel =
        CanvasViewModel(undirectedViewModel, zoomAnimate, centerAnimate, canvasSize, isOrientated)

    Row(Modifier.offset(0f.dp, Config.headerHeight.dp)) {
        MenuView { isClustering = !isClustering }
        CanvasView(
            canvasViewModel,
            Modifier
                .fillMaxSize()
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

    SettingsView(
        undirectedViewModel::onColorChange,
        undirectedViewModel::onSizeChange,
        { isOrientated = !isOrientated }
    )
}