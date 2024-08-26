package components

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp

@Composable
fun MyText(text: String, fontSize: Float = 20f) {
    val fontFamily = FontFamily(Font(resource = "Inter-Regular.ttf"))
    Text(text = text, color = Color.White, fontFamily = fontFamily, fontSize = fontSize.sp)
}