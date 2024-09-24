package view

import Config
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
fun DisplayAlgorithmMenu(name: String, viewModel: MenuViewModel, onClick: () -> Unit = {}) {

    val imageResources = listOf(
        "FindBridge.svg",
        "Dijkstra.svg",
        "Bellman-Ford.svg",
        "IslandTree.svg",
        "StrongConnectivityComponent.svg",
        "FindCycle.svg"
    )
    Box(
        modifier = Modifier.padding(top = 240.dp, start = 80.dp)
    ) {

        // Изображение
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
            items(imageResources) { image ->
                ImageButton(imageResourceId = image, onClick = onClick, viewModel)
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
            "FindBridge.svg" -> Modifier.glowRec(viewModel.isFinded).onClick(onClick = onClick)
            else -> Modifier.alpha(0.2f)
        }

        Image(
            painter = painterResource(imageResourceId),
            contentDescription = "Button Image",
            modifier = modifier.size(445.dp, 59.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun MainView(mainViewModel: MainViewModel) {

    var isBridgeFinded by mainViewModel.menuViewModel::isFinded

    Row(Modifier.offset(0f.dp, Config.headerHeight.dp)) {
        MenuView(mainViewModel.menuViewModel)

        CanvasView(
            mainViewModel.canvasViewModel,
            Modifier.fillMaxSize()
        )
    }

    if (mainViewModel.menuViewModel.isAlgorithmMenuOpen) {
        DisplayAlgorithmMenu(
            "DownMenuAlgorithm.svg",
            mainViewModel.menuViewModel
        ) { isBridgeFinded = !isBridgeFinded }
    }

    SettingsView(mainViewModel.settingsViewModel)
}

fun Modifier.glowRec(flag: Boolean): Modifier {
    if (!flag) return Modifier

    return Modifier.border(1f.dp, color = Color(0xFFFF00FF), shape = RectangleShape)
}