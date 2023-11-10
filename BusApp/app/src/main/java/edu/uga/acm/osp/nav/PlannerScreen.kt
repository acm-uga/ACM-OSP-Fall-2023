package edu.uga.acm.osp.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.uga.acm.osp.composables.DisplayBox
import edu.uga.acm.osp.composables.Header
import edu.uga.acm.osp.composables.NotificationComposable
import edu.uga.acm.osp.composables.displayNavBar


@Composable
fun PlannerScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Header(text = "Trip Planner")
        },
        bottomBar = {
            displayNavBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Put composables here!
        }
    }
}