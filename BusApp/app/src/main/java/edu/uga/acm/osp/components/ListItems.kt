package edu.uga.acm.osp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import edu.uga.acm.osp.ui.theme.BusAppTheme

// List item with an icon badge, single context, and config button
@Composable
fun IconListItem(
    header: String, // List item header
    subheader: String, // List item subheader
    onListElementClick: () -> Unit = {}, // Optional on list element click action
    badgeColor: Color = BusAppTheme.colors.accent, // Badge color (default is accent)
    badgeIcon: ImageVector, // Badge icon (pass as Icons.StyleName.IconName)
    badgeIconDesc: String, // Badge icon description
    context1: @Composable() (Modifier) -> Unit = {}, // Optional first context composable
    context2: @Composable() (Modifier) -> Unit = {} // Optional second context composable
){
    Row(modifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth()
        .clickable(
            onClick = onListElementClick,
            onClickLabel = "Open List Element"))
    {
        // Badge with icon
        IconBadge(badgeColor, badgeIcon, badgeIconDesc)

        // Text and context
        Row(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterVertically)
        ){
            // List element header and subheader
            Column(modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
                .weight(weight = 1f, fill = false)
                .wrapContentSize()
            ){
                Text(
                    text = header,
                    style = BusAppTheme.typography.h2,
                    color = BusAppTheme.colors.onBackgroundPrimary,
                    textAlign = TextAlign.Left,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Text(
                    text = subheader,
                    style = BusAppTheme.typography.subtitle1,
                    color = BusAppTheme.colors.onBackgroundSecondary,
                    textAlign = TextAlign.Left,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .graphicsLayer(translationY = -10f))
            }

            // Context composables
            Column(modifier = Modifier
                .padding(start = 10.dp)
                .align(Alignment.CenterVertically)
                .weight(weight = .75f, fill = false) // Change weight arg to determine whether primary or context is truncated
                .wrapContentSize()
            ){
                // Context 1
                Row(modifier = Modifier
                    .height(18.dp)
                ){
                    Spacer(modifier = Modifier.weight(weight = 1f))
                    context1
                }

                // Context 2
                Row(modifier = Modifier
                    .height(18.dp)
                    .padding(top = 2.dp)
                ){
                    Spacer(modifier = Modifier.weight(1f))
                    context2//TODO try adding (modifier.padding(top = 2.dp)) back in to correct visibility
                }
            }
        }
    }
}