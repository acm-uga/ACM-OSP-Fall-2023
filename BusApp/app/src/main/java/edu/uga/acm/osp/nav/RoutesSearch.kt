package edu.uga.acm.osp.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sort
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.uga.acm.osp.components.FilterChip
import edu.uga.acm.osp.components.Header
import edu.uga.acm.osp.components.IconButton
import edu.uga.acm.osp.components.LabelListItem
import edu.uga.acm.osp.components.displayNavBar
import edu.uga.acm.osp.data.baseClasses.Route
import edu.uga.acm.osp.data.sources.StaticExampleData
import edu.uga.acm.osp.ui.theme.BusAppTheme




@Composable
fun RoutesSearch(navController: NavController) {

    val route: Route = StaticExampleData.getRoute();
    val route2: Route = StaticExampleData.getRoute();
    val route3: Route = StaticExampleData.getRoute();
    val route4: Route = StaticExampleData.getRoute();
    val route5: Route = StaticExampleData.getRoute();

    val routes: List<Route> = listOf(route,route2,route3,route4,route5);

    Header(screenName = "Search All")
    displayNavBar(navController = navController, enableSearch = true)


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

    }
}