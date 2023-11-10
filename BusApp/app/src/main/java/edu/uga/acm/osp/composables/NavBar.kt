package edu.uga.acm.osp.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material.Badge
import androidx.compose.material.BadgedBox
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
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

/*
    Based off of Phillip Lackner's tutorial video.
    I will have to through this multiple more times to do the following:
     * Make sure I actually understand what each line does
     * Make sure everything is up to date and adjust for any changes
     * Make the style consistent
 */
@Composable
fun NavBar(items: List<NavBarItem>, navController: NavController, modifier: Modifier = Modifier, onItemClick: (NavBarItem) -> Unit) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = BusAppTheme.colors.onBackgroundPrimary, // Based off of the tutorial video. Will probably change the color to match the UGA theme later
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                selectedContentColor = Color.Green,
                unselectedContentColor = Color.Gray,
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

/*
@Preview(showBackground = true)
@Composable
fun NavBarPreview() { // Previews the navbar
    // Create the paramaters
    var previewList = List<NavBarItem>(3) {
        NavBarItem("test1", "route", );
        NavBarItem("test1", "route", )
    } //list of 2 NavBarItems

}

 */
@Composable
fun displayNavBar(navController: NavController) {

    NavBar(items = listOf(
        NavBarItem(
            name = "Home",
            route = "home_screen",
            icon = Icons.Default.Home
        ),
        NavBarItem(
            name = "Search",
            route = "search_screen",
            icon = Icons.Default.Search
        ),
        NavBarItem(
            name = "Alert",
            route = "alert_screen",
            icon = Icons.Default.Notifications
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