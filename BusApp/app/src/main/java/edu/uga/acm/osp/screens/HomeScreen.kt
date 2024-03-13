package edu.uga.acm.osp.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import edu.uga.acm.osp.components.BusOverviewPreview
import edu.uga.acm.osp.components.Header
import edu.uga.acm.osp.components.displayNavBar

@Composable
fun HomeScreen(navController: NavController) {
    Header(screenName = "Home Screen")
    displayNavBar(navController = navController)
}