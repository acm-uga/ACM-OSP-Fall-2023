package edu.uga.acm.osp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.uga.acm.osp.components.DisplayBox
import edu.uga.acm.osp.components.Header
import edu.uga.acm.osp.components.NotificationComposable
import edu.uga.acm.osp.components.displayNavBar

@Composable
fun SettingScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Header(screenName = "Settings")
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