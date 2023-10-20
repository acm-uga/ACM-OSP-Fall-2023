package edu.uga.acm.osp.composables

import androidx.compose.ui.graphics.vector.ImageVector

data class NavBarItem(val name: String, val route: String, val icon: ImageVector, val badgeCount: Int = 0) {

}
