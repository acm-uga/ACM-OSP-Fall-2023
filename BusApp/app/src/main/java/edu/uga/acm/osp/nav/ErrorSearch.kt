package edu.uga.acm.osp.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Sort
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.uga.acm.osp.components.FilterChip
import edu.uga.acm.osp.components.Header
import edu.uga.acm.osp.components.IconButton
import edu.uga.acm.osp.components.displayNavBar
import edu.uga.acm.osp.data.baseClasses.Stop
import edu.uga.acm.osp.data.sources.StaticExampleData
import edu.uga.acm.osp.ui.theme.BusAppTheme




@Composable
fun ErrorSearch(navController: NavController) {
    Header(screenName = "Search All")
    displayNavBar(navController = navController, enableSearch = true)

    Text(text = "No Stops Match Your Search")

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
    }
}