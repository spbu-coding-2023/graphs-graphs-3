package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import components.MySlider
import components.MyText
import viewModel.SettingsViewModel


@Composable
fun SettingsView(viewModel: SettingsViewModel) {
    val redSlider = remember { mutableStateOf(1f / (0xFF / 0x8F)) }
    val greenSlider = remember { mutableStateOf(0f) }
    val blueSlider = remember { mutableStateOf(1f) }
    val sizeSlider = remember { mutableStateOf(35f) }
    val orientatedCheckBox = remember { mutableStateOf(false) }

    viewModel.onColorChange(Color(red = redSlider.value, green = greenSlider.value, blue = blueSlider.value))
    viewModel.onSizeChange(sizeSlider.value)

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
                        viewModel.onOrientatedChange(it)
                        orientatedCheckBox.value = !orientatedCheckBox.value
                    })
                }
            }
        }
    }
}