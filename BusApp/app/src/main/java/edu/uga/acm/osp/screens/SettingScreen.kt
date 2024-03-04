package edu.uga.acm.osp.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import edu.uga.acm.osp.components.*


@Composable
fun SettingScreen(navController: NavController) {
    Header("Settings")
    DisplayBox()
    displayNavBar(navController = navController)
}