package viewModel.canvas

import viewModel.graph.EdgeViewModel

class EdgeCanvasViewModel(
    val first: VertexCanvasViewModel,
    val second: VertexCanvasViewModel,
    val edgeViewModel: EdgeViewModel,
    private val canvasViewModel: CanvasViewModel,
) {
    var showOrientation by canvasViewModel::isOrientated
    var color by edgeViewModel::color

    val strokeWidth
        get() = edgeViewModel.strokeWidth * canvasViewModel.zoom
}