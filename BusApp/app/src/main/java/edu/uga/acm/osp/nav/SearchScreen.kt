package edu.uga.acm.osp.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuOpen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.uga.acm.osp.composables.DisplayBox
import edu.uga.acm.osp.composables.Header
import edu.uga.acm.osp.composables.NotificationComposable
import edu.uga.acm.osp.composables.TestComposable
import edu.uga.acm.osp.composables.displayNavBar

@Composable
fun SearchScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Header(text = "Search All")
        },
        bottomBar = {
            displayNavBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .height(590.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton(textButton = "Bus Routes",
                colorBackground = BusAppTheme.colors.container,
                colorText = BusAppTheme.colors.onBackgroundPrimary)
            TextButton(textButton = "Bus Stops",
                colorBackground = BusAppTheme.colors.container,
                colorText = BusAppTheme.colors.onBackgroundPrimary)
            IconButton(iconName = Icons.Outlined.MenuOpen,
                colorBackground = BusAppTheme.colors.container,
                description = "test")
        }
    }
}