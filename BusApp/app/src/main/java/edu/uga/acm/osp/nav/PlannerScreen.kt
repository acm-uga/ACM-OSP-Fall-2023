package edu.uga.acm.osp.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import edu.uga.acm.osp.composables.Header
import edu.uga.acm.osp.composables.displayNavBar


@Composable
fun PlannerScreen(navController: NavController) {
    Header("Trip Planner")
    displayNavBar(navController = navController)
}