package edu.uga.acm.osp.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.uga.acm.osp.composables.Header
import edu.uga.acm.osp.composables.HomeCard
import edu.uga.acm.osp.composables.ReminderCard
import edu.uga.acm.osp.composables.displayNavBar


@Composable
fun AlertScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Header(text = "Your Alerts")
        },
        bottomBar = {
            displayNavBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
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
                            cardName = "Status Updates",
                            stop = listOf("Rerouting Notice", "Routes Ending"),
                            desc = listOf(
                                "Bus Route 3",
                                "Bus Route 4"
                            ),
                            badge = listOf("R3", "R4"),
                            routes = listOf(true, true)
                        )
                    }

                    item {
                        ReminderCard(
                            cardName = "Your Reminders",
                            stop = listOf("Custom Name 1", "Custom Name 2"),
                            desc = listOf(
                                "Arrive to Xxx by 0:00am",
                                "Catch next Xxx Bus"
                            ),
                            badge = listOf("Stop", "R2"),
                            routes = listOf(false, true)
                        )
                    }
                }
            }
        }
    }
}