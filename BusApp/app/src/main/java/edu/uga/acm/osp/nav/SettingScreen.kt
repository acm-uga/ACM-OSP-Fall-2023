package edu.uga.acm.osp.nav

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.uga.acm.osp.composables.*

@Composable
fun SettingScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Header(text = "Settings")
        },
        bottomBar = {
            displayNavBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            DisplayBox()
            NotificationComposable()
        }
    }
}