package edu.uga.acm.osp.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import edu.uga.acm.osp.components.Header
import edu.uga.acm.osp.components.displayNavBar


@Composable
fun AlertScreen(navController: NavController) {
    Header("Your Alerts")
    displayNavBar(navController = navController)
}