package edu.uga.acm.osp.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.Sort
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.uga.acm.osp.components.FilterChip
import edu.uga.acm.osp.components.Header
import edu.uga.acm.osp.components.IconButton
import edu.uga.acm.osp.components.IconListItem
import edu.uga.acm.osp.components.LabelListItem
import edu.uga.acm.osp.components.displayNavBar
import edu.uga.acm.osp.data.baseClasses.Route
import edu.uga.acm.osp.data.baseClasses.Stop
import edu.uga.acm.osp.data.sources.StaticExampleData
import edu.uga.acm.osp.ui.theme.BusAppTheme




@Composable
fun StopsSearch(navController: NavController) {

    val stop: Stop = StaticExampleData.getStop();
    val stop2: Stop = StaticExampleData.getStop();
    val stop3: Stop = StaticExampleData.getStop();
    val stop4: Stop = StaticExampleData.getStop();
    val stop5: Stop = StaticExampleData.getStop();


    val stops: List<Stop> = listOf(stop,stop2,stop3,stop4,stop5);

    Header(screenName = "Search All")
    displayNavBar(navController = navController, enableSearch = true)

    LazyColumn {
        items(stops.size) { index ->
            Row {
                IconListItem(
                    header = stops[index].name,
                    subheader = stops[index].stopId.toString(),
                    badgeIcon = Icons.Default.LocationOn,
                    badgeIconDesc = "Location"
                )
            }
        }
    }

    // Search box and navbar
    Column {
        // Filters and Sort
        Row(verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .height(730.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            FilterChip(filterText = "Bus Routes", onClick = {})
            FilterChip(filterText = "Bus Stops", onClick = {})
            IconButton(
                icon = Icons.Outlined.Sort,
                backgroundColor = BusAppTheme.colors.container,
                description = "Sort Search",
                onClick = {}
            )
        }

        // Add search box and nav bar here
    }
}




// -1 = error, 0 = routes, 1 = stops, 2 = all
@Composable
fun RenderList(indicator : Int, routes : List<Route>, stops : List<Stop>) {
    if (indicator == -1) {
        Text(text = "No Stops Match Your Search")
    } else if (indicator == 0) {
        LazyColumn (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(routes.size) { index ->
                Row {
                    LabelListItem(
                        header = routes[index].name,
                        subheader = routes[index].routeId.toString(),
                        badgeLabel = routes[index].abbName
                    )
                }
            }
        }
    } else if (indicator == 1) {
        LazyColumn {
            items(stops.size) { index ->
                Row {
                    IconListItem(
                        header = stops[index].name,
                        subheader = stops[index].stopId.toString(),
                        badgeIcon = Icons.Default.LocationOn,
                        badgeIconDesc = "Location"
                    )
                }
            }
        }
    } else if (indicator == 2) {
        LazyColumn {
            items(routes.size) { index ->
                Row {
                    LabelListItem(
                        header = routes[index].name,
                        subheader = routes[index].routeId.toString(),
                        badgeLabel = routes[index].abbName
                    )
                }
            }

            items(stops.size) { index ->
                Row {
                    IconListItem(
                        header = stops[index].name,
                        subheader = stops[index].stopId.toString(),
                        badgeIcon = Icons.Default.LocationOn,
                        badgeIconDesc = "Location"
                    )
                }
            }

        }
    }
}