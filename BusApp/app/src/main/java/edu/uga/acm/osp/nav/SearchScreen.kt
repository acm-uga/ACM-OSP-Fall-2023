package edu.uga.acm.osp.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MenuOpen
import androidx.compose.material.icons.outlined.Sort
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.uga.acm.osp.components.FilterChip
import edu.uga.acm.osp.components.Header
import edu.uga.acm.osp.components.IconButton
import edu.uga.acm.osp.components.TextButton
import edu.uga.acm.osp.components.displayNavBar
import edu.uga.acm.osp.ui.theme.BusAppTheme

@Composable
fun SearchScreen(navController: NavController) {
    Header(screenName = "Search All")
    displayNavBar(navController = navController, enableSearch = true)

    // Render query response here
    LazyColumn {

    }

    // Search box and navbar
    Column {
        // Filters and Sort
        Row(verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .height(730.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            FilterChip(filterText = "Bus Routes", onClick = {})
            FilterChip(filterText = "Bus Stops", onClick = {})
            IconButton(
                icon = Icons.Outlined.Sort,
                backgroundColor = BusAppTheme.colors.container,
                description = "Sort Search",
                onClick = {}
            )
        }

        // Add search box and nav bar here
    }
}