package edu.uga.acm.osp.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.uga.acm.osp.screens.AlertScreen
import edu.uga.acm.osp.screens.HomeScreen
import edu.uga.acm.osp.screens.PlannerScreen
import edu.uga.acm.osp.screens.SearchScreen
import edu.uga.acm.osp.screens.SettingScreen

@Preview
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.HomeScreen.route) {
        composable(route = Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = Screen.SearchScreen.route) {
            SearchScreen(navController = navController)
        }
        composable(route = Screen.AlertScreen.route) {
            AlertScreen(navController = navController)
        }
        composable(route = Screen.PlannerScreen.route) {
            PlannerScreen(navController = navController)
        }
        composable(route = Screen.SettingsScreen.route) {
            SettingScreen(navController = navController)
        }
        //composable(route = Screen.ScreenName.route) {
    //  ScreenName(navController = navController)
    //
    // }
    }
}
