package edu.uga.acm.osp.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.uga.acm.osp.components.TextButton
import edu.uga.acm.osp.composables.DisplayBox
import edu.uga.acm.osp.composables.Header
import edu.uga.acm.osp.composables.NotificationComposable
import edu.uga.acm.osp.composables.TestComposable
import edu.uga.acm.osp.composables.displayNavBar
import edu.uga.acm.osp.ui.theme.BusAppTheme

@Composable
fun SearchScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Header(text = "Search All")
        },
        bottomBar = {
            displayNavBar(navController = navController, enableSearch = true)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Put composables here!
            // NOTE: Composables put here do not display for some reason.
        }
        // Put composables here instead
        Row(verticalAlignment = Alignment.Bottom) {
            TextButton(textButton = "test", colorBackground = Color.Black, colorText = Color.Red)
            TextButton(textButton = "test2", colorBackground = Color.Black, colorText = Color.Red)
        }
    }
}