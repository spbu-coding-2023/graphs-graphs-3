package view

import Config
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import viewModel.MenuViewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun MenuIcon(name: String, description: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    var isHovered by remember { mutableStateOf(false) }
    val popupOffset: Offset = Offset(80f, -180f)
    var iconPosition by remember { mutableStateOf(IntOffset.Zero) }
    var iconSize by remember { mutableStateOf(IntSize.Zero) }

    Image(
        painter = painterResource(name),
        contentDescription = description,
        modifier = modifier
            .onClick(onClick = onClick)
            .pointerMoveFilter(
                onEnter = {
                    isHovered = true
                    true
                },
                onExit = {
                    isHovered = false
                    false
                }
            )
            .onGloballyPositioned { layoutCoordinates ->
                iconPosition = layoutCoordinates.positionInRoot().run {
                    IntOffset(x.roundToInt(), y.roundToInt())
                }
                iconSize = layoutCoordinates.size
            }
    )
    Spacer(Modifier.height(10f.dp))
    if (isHovered) {
        Popup(
            offset = with(LocalDensity.current) {
                IntOffset(
                    (iconPosition.x + popupOffset.x.toDp().roundToPx()),
                    (iconPosition.y + iconSize.height + popupOffset.y.toDp().roundToPx())
                )
            },
            alignment = Alignment.TopStart,
            properties = PopupProperties(focusable = false)
        ) {
            DisplayDescription(description)
        }
    }
}

@Composable
fun DisplayDescription(name: String) {
    Image(
        painter = painterResource(name),
        contentDescription = "Descript",
        modifier = Modifier.size(350f.dp),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun MenuView(
    viewModel: MenuViewModel
) {
    Column(
        Modifier.fillMaxHeight().width(Config.menuWidth.dp).background(color = Color(0xFF3D3D3D)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(25f.dp))
        MenuIcon(
            "Nodes.svg", "AddNode.svg", Modifier.glow(viewModel.isNodeCreating)
        ) {
            viewModel.onNodeCreatingChange()
        }
        MenuIcon("Ribs.svg", "AddEdge.svg", modifier = Modifier.alpha(0.2f))
        MenuIcon("Clustering.svg", "ClusterD.svg", Modifier.glow(viewModel.isClustering)) {
            viewModel.onClusteringChange()
        }
        MenuIcon("PageRank.svg", "AnalysisGraph.svg", Modifier.glow(viewModel.isRanked)) {
            viewModel.onRankedChange()
        }
        MenuIcon("Algorithm.svg", "Algorithms....svg", Modifier.glow(viewModel.isAlgorithmMenuOpen)) {
            viewModel.onAlgorithmMenuOpenChange()
        }
    }
}

fun Modifier.glow(flag: Boolean): Modifier {
    if (!flag) return Modifier

    return Modifier.border(4f.dp, color = Color(0xFFFF00FF), shape = CircleShape)
}