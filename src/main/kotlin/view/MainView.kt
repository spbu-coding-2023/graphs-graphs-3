package view

import Config
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import view.canvas.CanvasView
import viewModel.MainViewModel

val HEADER_HEIGHT = Config.headerHeight
val MENU_WIDTH = Config.menuWidth

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun MainView(mainViewModel: MainViewModel) {
    var isClustering by remember { mutableStateOf(false) }
    var isRanked by remember { mutableStateOf(false) }
    var isNodeCreatingMode by remember { mutableStateOf(false) }

    Row(Modifier.offset(0f.dp, Config.headerHeight.dp)) {
        MenuView(
            isNodeCreatingMode,
            { isNodeCreatingMode = !isNodeCreatingMode },
            isClustering,
            { isClustering = !isClustering },
            isRanked,
            { isRanked = !isRanked })
        
        CanvasView(
            mainViewModel.canvasViewModel,
            Modifier.fillMaxSize()
        )
    }

    SettingsView(mainViewModel.settingsViewModel)
}