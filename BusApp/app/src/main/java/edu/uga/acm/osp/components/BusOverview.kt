package edu.uga.acm.osp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import edu.uga.acm.osp.data.baseClasses.Bus
import edu.uga.acm.osp.data.baseClasses.Route
import edu.uga.acm.osp.data.sources.ExampleGenerator
import edu.uga.acm.osp.data.sources.StaticExampleData

@Preview
@Composable
fun BusOverviewPreview() {
    val route: Route = StaticExampleData.getRoute()
    BusOverview(route)
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
fun BusOverview(route: Route) {
    val busOverview: HashMap<Long, Array<Bus>> = route.activeBuses

    // Isolate the "inner" stops (non-first/last stops)
    val innerStops: HashMap<Long, Array<Bus>> = HashMap<Long, Array<Bus>>()
    var topStopEntry: Map.Entry<Long, Array<Bus>>? = null
    var bottomStopEntry: Map.Entry<Long, Array<Bus>>? = null

    var i: Int = 0
    for (item: MutableMap.MutableEntry<Long, Array<Bus>> in busOverview) {
        if (i == 0) {
            topStopEntry = item
        } else if (i != 0 && i != (busOverview.size - 1)) {
            innerStops[item.key] = item.value
        } else if (i == (busOverview.size - 1)) {
            bottomStopEntry = item
        }
        i++
    }

    // Now that the info is correctly separated, render it
    BasicContainer(
        containerHeader = "Live Overview",
        context = {
            ContextInfo(
                contextText = route.,
                contextIcon = ,
                contextDesc = )
        }) {
            Column {
                TopStop(topStopEntry)

                // Inner stops
                innerStops.forEach {innerStopEntry ->
                    InnerStop(innerStopEntry)
                }

                BottomStop(bottomStopEntry)
            }
    }
}

@Composable
private fun TopStop(pairing: Map.Entry<Long, Array<Bus>>?) {
    var text: String = "Top: Nonexistent"
    if (pairing != null) {
        text = "Top: " + StaticExampleData.getStop(pairing.key).name
    }
    Text(text)
}

@Composable
private fun BottomStop(pairing: Map.Entry<Long, Array<Bus>>?) {
    var text: String = "Bottom: Nonexistent"
    if (pairing != null) {
        text = "Bottom: " + StaticExampleData.getStop(pairing.key).name
    }
    Text(text)
}

@Composable
private fun InnerStop(pairing: Map.Entry<Long, Array<Bus>>?) {
    var text: String = "Inner: Nonexistent"
    if (pairing != null) {
        text = "Inner: " + StaticExampleData.getStop(pairing.key).name
    }
    Text(text)
}