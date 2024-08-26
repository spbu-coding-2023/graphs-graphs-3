package components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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