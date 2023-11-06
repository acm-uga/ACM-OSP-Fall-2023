package edu.uga.acm.osp.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.uga.acm.osp.nav.HomeScreen
import edu.uga.acm.osp.nav.Navigation
import edu.uga.acm.osp.ui.theme.BusAppTheme

// The logic behind the navbar
@Composable
fun NavBar(items: List<NavBarItem>, navController: NavController, modifier: Modifier = Modifier, onItemClick: (NavBarItem) -> Unit) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = BusAppTheme.colors.container,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                selectedContentColor = BusAppTheme.colors.positiveStatus,
                unselectedContentColor = BusAppTheme.colors.negativeStatus,
                onClick = { onItemClick(item) },
                icon = {
                    Column(horizontalAlignment = CenterHorizontally) {
                        if (item.badgeCount > 0) {
                            BadgedBox(badge = { Badge { Text(item.badgeCount.toString()) } }) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name
                                )
                            }
                        } else {
                            Icon(imageVector = item.icon, contentDescription = item.name)
                        }
                        if (selected) {
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun searchBar() {
    var text by remember { mutableStateOf(" ") }
    TextField(
        value = text,
        onValueChange = { text = it },
        label = { Text("Search")},
        modifier = Modifier.fillMaxWidth()
    )
}

//Displaying the navbar. Creates the navbar to be used in the app
@Composable
fun displayNavBar(navController: NavController) {
// For navigation
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxSize()
    ) {
        searchBar()
        NavBar(items = listOf(
            NavBarItem(
                name = "Home",
                route = "home_screen",
                icon = Icons.Default.Home
            ),
            NavBarItem(
                name = "Alert",
                route = "alert_screen",
                icon = Icons.Default.Notifications
            ),
            NavBarItem(
                name = "Search",
                route = "search_screen",
                icon = Icons.Default.Search
            ),
            NavBarItem(
                name = "Planner",
                route = "planner_screen",
                icon = Icons.Default.DateRange
            ),
            NavBarItem(
                name = "Settings",
                route = "setting_screen",
                icon = Icons.Default.Settings
            ),
        ),
            navController = navController,
            onItemClick = {
                navController.navigate(it.route)
            }
        )
    }
}