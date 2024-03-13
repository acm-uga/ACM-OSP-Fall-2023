package edu.uga.acm.osp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import edu.uga.acm.osp.data.baseClasses.Bus
import edu.uga.acm.osp.data.baseClasses.Route
import edu.uga.acm.osp.data.sources.ExampleGenerator

@Preview
@Composable
fun BusOverviewPreview() {
    BusOverview()
}

//@Preview
@Composable
private fun StopTopPreview() {

}

//@Preview
@Composable
private fun StopBottomPreview() {

}

@Composable
fun BusOverview() {
    val exampleGen = ExampleGenerator()
    val route: Route = exampleGen.route
    val busOverview: HashMap<Long, Array<Bus>> = route.activeBuses
    BasicContainer("Live Overview") {
        Column {
            busOverview.forEach {stop ->
                Text(exampleGen.getStop(stop.key).name)
            }
        }
    }
}

@Composable
private fun StopTop(stopId: Long, buses: Array<Bus>) {

}

@Composable
private fun StopBottom(stopId: Long, buses: Array<Bus>) {

}