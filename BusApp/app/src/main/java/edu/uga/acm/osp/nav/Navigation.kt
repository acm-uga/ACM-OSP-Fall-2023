package edu.uga.acm.osp.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import edu.uga.acm.osp.nav.Screen

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
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(navController: NavController) {
    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 50.dp, vertical = 50.dp)
    ) {
        Button(onClick = {
            navController.navigate(Screen.SearchScreen.route)
        },
            modifier = Modifier.align(Alignment.End)) {
            Text(text = "To SearchScreen")
        }
    }
}

@Composable
fun SearchScreen(navController: NavController) {
    Text(text = "Howdy there",
    modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 10.dp)
    )
}