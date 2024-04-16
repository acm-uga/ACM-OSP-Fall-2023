package edu.uga.acm.osp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
import edu.uga.acm.osp.data.sources.StaticExampleData
import edu.uga.acm.osp.ui.theme.BoydGray
import edu.uga.acm.osp.ui.theme.BusAppTheme




@Composable
fun RoutesScreen(navController: NavController) {

    val route: Route = StaticExampleData.getRoute();
    val route2: Route = StaticExampleData.getRoute();
    val route3: Route = StaticExampleData.getRoute();
    val route4: Route = StaticExampleData.getRoute();
    val route5: Route = StaticExampleData.getRoute();

    val routes: List<Route> = listOf(route, route2, route3, route4, route5);

    displayNavBar(navController = navController, enableSearch = true)

    Column {
        Header(screenName = "Search All")

        Row(
            modifier = Modifier
                .height(620.dp)
                .background(color = BoydGray)
                .padding(10.dp),
        ) {

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

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                filterText = "Bus Routes",
                onClick = { navController.navigate("routes_screen") })
            FilterChip(
                filterText = "Bus Stops",
                onClick = { navController.navigate("stops_screen") })
            FilterChip(filterText = "All", onClick = { navController.navigate("search_screen") })
        }
    }
}