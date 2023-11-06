package edu.uga.acm.osp.nav

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import edu.uga.acm.osp.composables.Header
import edu.uga.acm.osp.composables.TestComposable
import edu.uga.acm.osp.composables.displayNavBar

@Composable
fun SearchScreen(navController: NavController) {
    Header(text = "Search Screen")
    displayNavBar(navController = navController)
}