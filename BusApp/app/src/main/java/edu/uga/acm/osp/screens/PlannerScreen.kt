package edu.uga.acm.osp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
<<<<<<< HEAD:BusApp/app/src/main/java/edu/uga/acm/osp/nav/PlannerScreen.kt
import edu.uga.acm.osp.composables.DisplayBox
import edu.uga.acm.osp.composables.Header
import edu.uga.acm.osp.composables.NotificationComposable
import edu.uga.acm.osp.composables.displayNavBar
=======
import edu.uga.acm.osp.components.Header
import edu.uga.acm.osp.components.displayNavBar
>>>>>>> JQB-Front-End-Development:BusApp/app/src/main/java/edu/uga/acm/osp/screens/PlannerScreen.kt


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