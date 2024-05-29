package view

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
            .onClick(onClick = onClick)
    )
    Spacer(Modifier.height(10f.dp))
}

@Composable
fun MenuView(
    isNodeCreating: Boolean,
    onNodeCreatingChange: () -> Unit,
    isClustering: Boolean,
    onClusteringChange: () -> Unit
) {
    Column(
        Modifier.fillMaxHeight().width(Config.menuWidth.dp).background(color = Color(0xFF3D3D3D)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(25f.dp))
        MenuIcon(
            "Nodes.svg", "Add Node", Modifier.glow(isNodeCreating)
        ) {
            onNodeCreatingChange()
        }
        MenuIcon("Ribs.svg", "Add Edge")
        MenuIcon("Clustering.svg", "Clustering", Modifier.glow(isClustering)) {
            onClusteringChange()
        }
        MenuIcon("PageRank.svg", "Analysis graph")
    }
}

fun Modifier.glow(flag: Boolean): Modifier {
    if (!flag) return Modifier

    return Modifier.border(4f.dp, color = Color(0xFFFF00FF), shape = CircleShape)
}