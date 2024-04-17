package edu.uga.acm.osp.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Route
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
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
                unselectedContentColor = BusAppTheme.colors.onBackgroundSecondary,
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


//Displaying the navbar. Creates the navbar to be used in the app
@Composable
fun displayNavBar(navController: NavController, enableSearch: Boolean = false){
// For navigation
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxSize()
    ) {
        Row() {
            if (enableSearch) {
                searchBar()
            }
        }
        NavBar(items = listOf(
            NavBarItem(
                name = "Home",
                route = "home_screen",
                icon = Icons.Outlined.Home
            ),
            /*
            NavBarItem(
                name = "Alerts",
                route = "alert_screen",
                icon = Icons.Outlined.Notifications
            ),
            */
            NavBarItem(
                name = "Search",
                route = "search_screen",
                icon = Icons.Outlined.Search
            ),
            /*
            NavBarItem(
                name = "Planner",
                route = "planner_screen",
                icon = Icons.Outlined.Route
            ),
            */
            NavBarItem(
                name = "Settings",
                route = "setting_screen",
                icon = Icons.Outlined.Settings
            ),
        ),
            navController = navController,
            onItemClick = {
                navController.navigate(it.route)
            }
        )
    }
}