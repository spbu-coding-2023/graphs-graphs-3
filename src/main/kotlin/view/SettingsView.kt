package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex


@Composable
fun MySlider(text: String, state: MutableState<Float>, range: ClosedFloatingPointRange<Float> = (0f..1f)) {
    Row(
        Modifier.padding(start = 5f.dp, end = 5f.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        MyText(text = text)
        Slider(
            modifier = Modifier.padding(0f.dp),
            value = state.value, onValueChange = { change -> state.value = change },
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White,
                inactiveTrackColor = Color.White,
            ),
            valueRange = range
        )
    }
}


@Composable
fun SettingsView(onColorChange: (Color) -> Unit, onSizeChange: (Float) -> Unit, onOrientatedChange: () -> Unit) {
    val redSlider = remember { mutableStateOf(1f / (0xFF / 0x8F)) }
    val greenSlider = remember { mutableStateOf(0f) }
    val blueSlider = remember { mutableStateOf(1f) }
    val sizeSlider = remember { mutableStateOf(35f) }
    val orientatedCheckBox = remember { mutableStateOf(false) }

    onColorChange(Color(red = redSlider.value, green = greenSlider.value, blue = blueSlider.value))
    onSizeChange(sizeSlider.value)

    Box(Modifier.fillMaxSize().padding(top = 80f.dp, end = 20f.dp).zIndex(10f), contentAlignment = Alignment.TopEnd) {
        Box(
            Modifier.size(270f.dp, 320f.dp).background(Color(0xFF3D3D3D), RoundedCornerShape(10))
        ) {
            Column {
                Row(Modifier.fillMaxWidth().padding(top = 10f.dp), horizontalArrangement = Arrangement.Center) {
                    MyText("Node")
                }
                Row(Modifier.fillMaxWidth().padding(top = 10f.dp, start = 20f.dp)) {
                    Column {
                        MyText("Color:")
                        MySlider("R: ", redSlider)
                        MySlider("G: ", greenSlider)
                        MySlider("B: ", blueSlider)
                        MySlider("Size: ", sizeSlider, (5f..80f))
                    }
                }

                Row(
                    Modifier.fillMaxWidth().padding(start = 20f.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyText("Orientated")
                    Checkbox(orientatedCheckBox.value, onCheckedChange = {
                        onOrientatedChange()
                        orientatedCheckBox.value = !orientatedCheckBox.value
                    })
                }
            }
        }
    }
}