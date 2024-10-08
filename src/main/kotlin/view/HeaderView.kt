package view

import Config
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import components.MyText

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeaderView(name: String, close: () -> Unit, maximize: () -> Unit, isMaximize: Boolean, minimize: () -> Unit) {
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
        Row(Modifier) {
            Row(Modifier.padding(start = 7f.dp, top = 7f.dp)) {
                Logo()
                FileButton()
            }
        }
        Row {
            MyText(name)
        }
        Row {
            MinimizeButton(minimize)
            MaximizeButton(maximize)
            CloseButton(close)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.menuButton(color: Color, onClick: () -> Unit): Modifier {
    return Modifier.fillMaxHeight().width(Config.headerHeight.dp).background(color).onClick {
        onClick()
    }
}

@Composable
private fun MinimizeButton(minimize: () -> Unit) {
    Box(Modifier.menuButton(Color(0xFF5A5959), minimize), contentAlignment = Alignment.Center) {
        Box(Modifier.size(10f.dp, 2f.dp).border(2f.dp, Color.White))
    }
}

@Composable
private fun MaximizeButton(maximize: () -> Unit) {
    Box(Modifier.menuButton(Color(0xFF5A5959), maximize), contentAlignment = Alignment.Center) {
        Box(Modifier.size(10f.dp, 8f.dp).border(2f.dp, Color.White))
    }
}

@Composable
private fun CloseButton(close: () -> Unit) {
    Box(Modifier.menuButton(Color(0xFFC80000), close), contentAlignment = Alignment.Center) {
        Icon(imageVector = Icons.Filled.Close, "Done", tint = Color.White)
    }
}

@Composable
private fun Logo() {
    Image(
        modifier = Modifier.padding(end = (Config.menuWidth - 30f - 7f).dp),
        painter = painterResource("Dima.svg"),
        contentDescription = "Icon"
    )
}

@Composable
fun ImageButtonFile(imageResourceId: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(300.dp, 50.dp)
            .padding(0.dp)
            .clickable { onClick() }
            .background(Color(0x00))
    ) {
        Image(
            painter = painterResource(imageResourceId),
            contentDescription = "Button Image",
            Modifier.size(400.dp, 50.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun FileMenu(){
    val imageResources = listOf(
        "DataBase.svg",
        "JSON.svg",
        "Neo4j.svg",
        "DataBaseLoad.svg",
        "JSONLoad.svg",
        "Neo4jLoad.svg"
    )

    Box(
            Modifier
                .size(300.dp, 300.dp)
                .shadow(
                    elevation = 5f.dp,
                    spotColor = Color.Black
                )
                .background(Color(0xFF3D3D3D)),

        ) {
        LazyColumn(
            modifier = Modifier
                .size(300.dp, 300.dp)

        ) {
            items(imageResources) { image ->
                ImageButtonFile(
                    imageResourceId = image,
                    onClick = {
                    }
                )
            }
        }
        }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FileButton() {
    var isImageVisible by remember { mutableStateOf(false) }
    Box(
        Modifier
            .clickable{ isImageVisible = !isImageVisible }
            .size(Config.headerHeight.dp)
            .shadow(
                elevation = 5f.dp,
                spotColor = Color.Black
            ).background(Color(0xFF3D3D3D)),
        contentAlignment = Alignment.Center
    ) {
        MyText("File", 16f)
        }
    if (isImageVisible) {
        Box(
            Modifier.padding(top = 33.dp)
        ) {
            Popup (
                properties = PopupProperties(focusable = false)
        ) {
            FileMenu()
        }
    }
    }
}