package view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuIcon(name: String, description: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Image(
        painter = painterResource(name),
        contentDescription = description,
        modifier = modifier
            .padding(bottom = 10f.dp)
            .onClick(onClick = onClick)
    )
}

@Composable
fun MenuView(onClusteringChange: () -> Unit) {
    println("MenuView")
    Column(
        Modifier.fillMaxHeight().width(Config.menuWidth.dp).background(color = Color(0xFF3D3D3D)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MenuIcon("Nodes.svg", "Add Node", Modifier.padding(top = 25f.dp))
        MenuIcon("Ribs.svg", "Add Edge")
        MenuIcon("Clustering.svg", "Clustering") {
            onClusteringChange()
        }
        MenuIcon("PageRank.svg", "Analysis graph")
    }
}