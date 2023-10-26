package edu.uga.acm.osp

import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.uga.acm.osp.components.ContextAction
import edu.uga.acm.osp.components.ContextInfo
import edu.uga.acm.osp.components.IconListItem
import edu.uga.acm.osp.ui.theme.BusAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Box(modifier = Modifier
                .background(BusAppTheme.colors.background)
                .fillMaxSize()){
                Box(modifier = Modifier
                    .padding(10.dp)
                    .background(BusAppTheme.colors.container)){
                    testListItem()
                }
            }
        }
    }
}

@Preview
@Composable
fun testListItem() {
    IconListItem(
        header = "Item Header",
        subheader = "Item Subheader",
        badgeIcon = Icons.Default.Apartment,
        badgeIconDesc = "Residence Hall",
        context1 = {
            ContextAction(
                actionIcon = Icons.Default.MoreHoriz,
                actionDesc = "View Quick Actions",
                action = {})
        },
        context2 = {
            ContextInfo(
                contextText = "Test Context",
                contextIcon = Icons.Default.DirectionsWalk,
                contextDesc = "Test Description"
            )
        }
    )
}