package edu.uga.acm.osp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import edu.uga.acm.osp.ui.theme.BusAppTheme

// Right-justified tiny button context module, primarily for use in a ListItem composable
@Composable
fun ContextAction(
    actionIcon: ImageVector, // Context action icon (Icons.StyleName.IconName)
    actionDesc: String, // Context action icon description
    action: () -> Unit, // When the context action button is clicked, do what?
    modifier: Modifier = Modifier // Optional Modifier
    ) {
    Box(contentAlignment = Alignment.Center,
        modifier = modifier
            .size(22.dp)
            .clip(CircleShape)
            .background(BusAppTheme.colors.tertiaryAction)
            .clickable(onClick = action)
    ){
        Icon(
            imageVector = actionIcon,
            contentDescription = actionDesc,
            tint = BusAppTheme.colors.onTertiaryAction,
            modifier = Modifier
                .size(18.dp)
        )
    }
}

// Right-justified info + icon context module, primarily for use in a ListItem composable
@Composable
fun ContextInfo(
    contextText: String, // Context text
    contextIcon: ImageVector, // Context icon
    contextDesc: String, // Context icon description
    modifier: Modifier = Modifier // Optional Modifier
) {
    Row(modifier = modifier){
        Text(text = contextText,
            style = BusAppTheme.typography.caption,
            color = BusAppTheme.colors.onBackgroundSecondary,
            maxLines = 1,
            textAlign = TextAlign.Right,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(weight = 1f, fill = true)
                .graphicsLayer(translationY = 7f)
                .padding(end = 2.dp))
        Icon(
            imageVector = contextIcon,
            contentDescription = contextDesc,
            tint = BusAppTheme.colors.onBackgroundSecondary,
            modifier = Modifier
                .size(22.dp)
        )
    }
}