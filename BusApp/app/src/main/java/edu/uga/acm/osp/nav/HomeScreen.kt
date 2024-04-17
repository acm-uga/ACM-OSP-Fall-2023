package edu.uga.acm.osp.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.uga.acm.osp.composables.Header
import edu.uga.acm.osp.composables.NavBar
import edu.uga.acm.osp.composables.NotificationComposable
import edu.uga.acm.osp.composables.TestComposable
import edu.uga.acm.osp.composables.displayNavBar
import edu.uga.acm.osp.composables.myButton
import edu.uga.acm.osp.ui.theme.BulldogRed

@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Header(screenName = "Home")
        },
        bottomBar = {
            displayNavBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    HomeCard(
                        cardName = "Recently Viewed",
                        stop = listOf("Bus Stop 1", "Bus Route 1", "Bus Route 2"),
                        desc = listOf(
                            "Serves R1, R2, +2 more",
                            "M-F 12:00am-12:00pm",
                            "Weekends 0:00am - 0:00pm"
                        ),
                        badge = listOf("Stop", "R1", "R2"),
                        routes = listOf(false, true, true)
                    )
                }

                item {
                    HomeCard(
                        cardName = "Favorites",
                        stop = listOf("Bus Stop 2", "Bus Stop 3", "Bus Stop 4"),
                        desc = listOf(
                            "At Building Name",
                            "Across from Building Name",
                            "Behind Building Name"
                        ),
                        badge = listOf("Stop", "Stop", "Stop"),
                        routes = listOf(false, false, false)
                    )
                }

                item {
                    HomeCard(
                        cardName = "Nearby Stops",
                        stop = listOf("Bus Stop 2", "Bus Stop 3", "Bus Stop 4"),
                        desc = listOf(
                            "At Building Name",
                            "Across from Building Name",
                            "Behind Building Name"
                        ),
                        badge = listOf("Stop", "Stop", "Stop"),
                        routes = listOf(false, false, false)
                    )
                }
            }
        }
    }
}