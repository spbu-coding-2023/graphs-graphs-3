package viewModel.canvas

import androidx.compose.ui.graphics.Color
import viewModel.graph.EdgeViewModel

class EdgeCanvasViewModel(
    val first: VertexCanvasViewModel,
    val second: VertexCanvasViewModel,
    val color: Color,
    val edgeViewModel: EdgeViewModel,
    private val canvasViewModel: CanvasViewModel,
) {
    var showOrientation by canvasViewModel::isOrientated

    val strokeWidth
        get() = edgeViewModel.strokeWidth * canvasViewModel.zoom
}