package view

import Config
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
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
    Row(Modifier.offset(0f.dp, Config.headerHeight.dp)) {
        MenuView(mainViewModel.menuViewModel)
        CanvasView(
            mainViewModel.canvasViewModel,
            Modifier.fillMaxSize()
        )
    }

    SettingsView(mainViewModel.settingsViewModel)
}