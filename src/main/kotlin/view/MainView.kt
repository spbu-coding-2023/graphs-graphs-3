package view

import Config
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import view.canvas.CanvasView
import viewModel.MainViewModel
import viewModel.MenuViewModel

val HEADER_HEIGHT = Config.headerHeight
val MENU_WIDTH = Config.menuWidth

@Composable
fun displayAlgorithmMenu(name: String, viewModel: MenuViewModel) {
    var isBridgeFinded by viewModel::isFinded

    data class ImageResource(val icon: String, val onClick: () -> Unit)

    val imageResources = listOf(
        ImageResource("FindBridge.svg") { isBridgeFinded = !isBridgeFinded },
        ImageResource("Dijkstra.svg") {},
        ImageResource("Bellman-Ford.svg") {},
        ImageResource("IslandTree.svg") {},
        ImageResource("StrongConnectivityComponent.svg") {},
        ImageResource("FindCycle.svg") {}
    )

    Box(
        modifier = Modifier.padding(top = 240.dp, start = 80.dp)
    ) {
        Image(
            painter = painterResource(name),
            contentDescription = "Padded Image",
            modifier = Modifier.size(452.dp),
            contentScale = ContentScale.Fit
        )

        LazyColumn(
            modifier = Modifier
                .size(450.dp, 300.dp)
                .background(Color.Transparent)
                .padding(top = 150.dp, start = 30.dp)
        ) {
            items(imageResources) { imageResource ->
                ImageButton(imageResourceId = imageResource.icon, onClick = imageResource.onClick, viewModel)
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageButton(imageResourceId: String, onClick: () -> Unit, viewModel: MenuViewModel) {
    Box(
        modifier = Modifier
            .size(440.dp, 60.dp)
            .padding(1.dp)
            .background(Color(0x00))
    ) {
        val modifier = when (imageResourceId) {
            "FindBridge.svg" -> Modifier.glowRec(viewModel.isFinded)
            else -> Modifier.alpha(0.2f)
        }

        Image(
            painter = painterResource(imageResourceId),
            contentDescription = "Button Image",
            modifier = modifier.size(445.dp, 59.dp).onClick(onClick = onClick),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun MainView(mainViewModel: MainViewModel) {

    Row(Modifier.offset(0f.dp, Config.headerHeight.dp)) {
        MenuView(mainViewModel.menuViewModel)

        CanvasView(
            mainViewModel.canvasViewModel,
            Modifier.fillMaxSize()
        )
    }

    if (mainViewModel.menuViewModel.isAlgorithmMenuOpen) {
        displayAlgorithmMenu(
            "DownMenuAlgorithm.svg",
            mainViewModel.menuViewModel
        )
    }

    SettingsView(mainViewModel.settingsViewModel)
}

fun Modifier.glowRec(flag: Boolean): Modifier {
    if (!flag) return Modifier

    return Modifier.border(1f.dp, color = Color(0xFFFF00FF), shape = RectangleShape)
}