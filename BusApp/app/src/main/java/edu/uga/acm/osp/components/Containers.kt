package edu.uga.acm.osp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.uga.acm.osp.ui.theme.BusAppTheme

@Composable
fun ViewAllContainer(
    containerHeader: String,
    viewAllDestination: () -> Unit,
    content: @Composable () -> Unit) {
    Box(modifier = Modifier
        .padding(5.dp)
        .background(BusAppTheme.colors.container)
        .wrapContentHeight())
    {
        Column(){
            Row(){
                Text(
                    text = containerHeader,
                    style = BusAppTheme.typography.h2,
                    color = BusAppTheme.colors.onContainerPrimary,
                    textAlign = TextAlign.Left,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
            }
            content
        }
    }
}