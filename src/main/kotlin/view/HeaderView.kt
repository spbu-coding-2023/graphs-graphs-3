package view

import Config
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeaderView(name: String, close: () -> Unit, maximize: () -> Unit, isMaximize: Boolean, minimize: () -> Unit) {
    println("HeaderView")
    Row(
        Modifier.fillMaxWidth().height(Config.headerHeight.dp)
            .background(color = Color(0xFF3D3D3D))
            .onDrag {
                if (isMaximize) {
                    maximize()
                }
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(Modifier.padding(start = Config.menuWidth.dp)) {
            MyText("File")
        }
        Row {
            Text(text = "$name", color = Color.White, fontSize = 20f.sp)
        }
        Row {
            Box(Modifier.fillMaxHeight().width(Config.headerHeight.dp).background(Color(0xFF5A5959)).onClick {
                minimize()
            }, contentAlignment = Alignment.Center) {
                Box(Modifier.size(10f.dp, 2f.dp).border(2f.dp, Color.White))
            }
            Box(Modifier.fillMaxHeight().width(Config.headerHeight.dp).background(Color(0xFF5A5959)).onClick {
                maximize()
            }, contentAlignment = Alignment.Center) {
                Box(Modifier.size(10f.dp, 8f.dp).border(2f.dp, Color.White))
            }
            Box(Modifier.fillMaxHeight().width(Config.headerHeight.dp).background(Color(0xFFC80000)).onClick {
                close()
            }, contentAlignment = Alignment.Center) {

                Icon(imageVector = Icons.Filled.Close, "Done", tint = Color.White)
            }
        }
    }
}