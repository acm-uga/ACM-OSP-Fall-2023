package edu.uga.acm.osp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.uga.acm.osp.ui.theme.BusAppTheme

// List item with an icon badge
@Composable
fun IconListItem(
    header: String, // List item header
    subheader: String, // List item subheader
    onListElementClick: () -> Unit = {}, // Optional on list element click action
    badgeColor: Color = BusAppTheme.colors.accent, // Badge color (default is accent)
    badgeIcon: ImageVector, // Badge icon (pass as Icons.StyleName.IconName)
    badgeIconDesc: String, // Badge icon description
    context1: @Composable() (Modifier) -> Unit = {}, // Optional first context composable
    context2: @Composable() (Modifier) -> Unit = {}, // Optional second context composable
    modifier: Modifier = Modifier // Optional Modifier
){
    Row(modifier = modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth()
        .clickable(
            onClick = onListElementClick,
            onClickLabel = "Open List Element"
        ))
    {
        // Badge with icon
        IconBadge(badgeColor = badgeColor, badgeIcon = badgeIcon, badgeIconDesc = badgeIconDesc)

        // Text and context
        Row(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterVertically)
        ){
            // List element header and subheader
            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(weight = 1f, fill = true)
            ){
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .wrapContentSize()
                ) {
                    Text(
                        text = header,
                        style = BusAppTheme.typography.h2,
                        color = BusAppTheme.colors.onBackgroundPrimary,
                        textAlign = TextAlign.Left,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subheader,
                        style = BusAppTheme.typography.subtitle1,
                        color = BusAppTheme.colors.onBackgroundSecondary,
                        textAlign = TextAlign.Left,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .graphicsLayer(translationY = -10f)
                    )
                }
            }

            // Context composables
            Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(weight = .75f, fill = true) // Change weight arg to change primary/context priority
            ){
                Column(
                    horizontalAlignment = Alignment.End, modifier = Modifier
                        .padding(start = 10.dp)
                        .wrapContentSize()
                ) {
                    // Context 1 slot
                    Row(
                        modifier = Modifier
                            .height(22.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(weight = 1f))
                        context1(Modifier)
                    }

                    // Context 2 slot
                    Row(
                        modifier = Modifier
                            .height(22.dp)
                            .padding(top = 2.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        context2(Modifier.padding(top = 2.dp))
                    }
                }
            }
        }
    }
}

// List item with a text label badge
@Composable
fun LabelListItem(
    header: String, // List item header
    subheader: String, // List item subheader
    onListElementClick: () -> Unit = {}, // Optional on list element click action
    badgeColor: Color = BusAppTheme.colors.accent, // Badge color (default is accent)
    badgeLabel: String, // Badge label text
    context1: @Composable() (Modifier) -> Unit = {}, // Optional first context composable
    context2: @Composable() (Modifier) -> Unit = {}, // Optional second context composable
    modifier: Modifier = Modifier // Optional Modifier
){
    Row(modifier = modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth()
        .clickable(
            onClick = onListElementClick,
            onClickLabel = "Open List Element"
        ))
    {
        // Badge with icon
        TextBadge(badgeColor = badgeColor, badgeText = badgeLabel)

        // Text and context
        Row(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterVertically)
        ){
            // List element header and subheader
            Box(contentAlignment = Alignment.CenterStart, modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(weight = 1f, fill = true)
            ){
                Column(
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .wrapContentSize()
                ) {
                    Text(
                        text = header,
                        style = BusAppTheme.typography.h2,
                        color = BusAppTheme.colors.onBackgroundPrimary,
                        textAlign = TextAlign.Left,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subheader,
                        style = BusAppTheme.typography.subtitle1,
                        color = BusAppTheme.colors.onBackgroundSecondary,
                        textAlign = TextAlign.Left,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .graphicsLayer(translationY = -10f)
                    )
                }
            }

            // Context composables
            Box(contentAlignment = Alignment.CenterEnd, modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(weight = .75f, fill = true) // Change weight arg to change primary/context priority
            ){
                Column(
                    horizontalAlignment = Alignment.End, modifier = Modifier
                        .padding(start = 10.dp)
                        .wrapContentSize()
                ) {
                    // Context 1 slot
                    Row(
                        modifier = Modifier
                            .height(22.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(weight = 1f))
                        context1(Modifier)
                    }

                    // Context 2 slot
                    Row(
                        modifier = Modifier
                            .height(22.dp)
                            .padding(top = 2.dp)
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        context2(Modifier.padding(top = 2.dp))
                    }
                }
            }
        }
    }
}