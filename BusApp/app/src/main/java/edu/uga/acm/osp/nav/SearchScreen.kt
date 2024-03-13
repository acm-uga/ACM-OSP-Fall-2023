package edu.uga.acm.osp.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuOpen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.uga.acm.osp.components.IconButton
import edu.uga.acm.osp.components.TextButton
import edu.uga.acm.osp.composables.Header
import edu.uga.acm.osp.composables.displayNavBar
import edu.uga.acm.osp.ui.theme.BusAppTheme

@Composable
fun SearchScreen(navController: NavController) {
    Header(text = "Search All")
    displayNavBar(navController = navController, enableSearch = true)

    LazyColumn {

    }

    Column {

        // Buttons
        Row(verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .height(730.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            TextButton({}, textButton = "Bus Routes",
                colorBackground = BusAppTheme.colors.container,
                colorText = BusAppTheme.colors.onBackgroundPrimary)
            TextButton({}, textButton = "Bus Stops",
                colorBackground = BusAppTheme.colors.container,
                colorText = BusAppTheme.colors.onBackgroundPrimary)
            IconButton({}, iconName = Icons.Outlined.MenuOpen,
                colorBackground = BusAppTheme.colors.container,
                description = "test")
        }
    }
}