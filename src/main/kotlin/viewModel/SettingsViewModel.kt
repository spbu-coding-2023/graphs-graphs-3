package viewModel

import androidx.compose.ui.graphics.Color

class SettingsViewModel(
    val onColorChange: (Color) -> Unit,
    val onSizeChange: (Float) -> Unit,
    val onOrientatedChange: (Boolean) -> Unit
) {
}