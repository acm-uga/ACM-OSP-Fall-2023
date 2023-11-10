package edu.uga.acm.osp.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import edu.uga.acm.osp.composables.*


@Composable
fun SettingScreen(navController: NavController) {
    Header("Settings")
    DisplayBox()
}