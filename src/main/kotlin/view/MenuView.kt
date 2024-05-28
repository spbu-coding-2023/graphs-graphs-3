package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun MenuIcon(name: String, description: String, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(name),
        contentDescription = description,
        modifier = modifier.padding(bottom = 10f.dp)
    )
}

@Composable
fun MenuView() {
    println("MenuView")
    Column(
        Modifier.fillMaxHeight().width(Config.menuWidth.dp).background(color = Color(0xFF3D3D3D)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        MenuIcon("Nodes.svg", "Add Node", Modifier.padding(top = 25f.dp))
        MenuIcon("Ribs.svg", "Add Edge")
        MenuIcon("Clustering.svg", "Clustering")
        MenuIcon("PageRank.svg", "Analysis graph")
    }
}