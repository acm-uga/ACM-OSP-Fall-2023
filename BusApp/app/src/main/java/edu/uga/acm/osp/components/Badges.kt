package edu.uga.acm.osp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uga.acm.osp.ui.theme.BusAppTheme

@Preview
@Composable
fun BadgesPreview() {
    IconBadge(badgeIcon = Icons.Default.DirectionsBus, badgeIconDesc = "Alarm")
}

@Preview
@Composable
private fun TextBadgePreview() {
    TextBadge(badgeText = "CCC")
}

/**
 * A circular, color-able badge with icon.
 *
 * @param badgeColor optional recolor of the badge itself (default is accent)
 * @param badgeIcon the icon resource to render inside the badge
 * @param badgeIconDesc a brief description of the badge's meaning for accessibility
 */
@Composable
fun IconBadge (badgeColor: Color = BusAppTheme.colors.accent, badgeIcon: ImageVector, badgeIconDesc: String) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(badgeColor)
            .padding(horizontal = 12.dp)
    ){
        Icon(imageVector = badgeIcon, badgeIconDesc, tint = BusAppTheme.colors.onAccent,
            modifier = Modifier
                .size(37.dp)
        )
    }
}

/**
 * A circular, color-able badge with text.
 *
 * @param badgeColor optional recolor of the badge itself (default is accent)
 * @param badgeText short text to render inside the badge
 */
@Composable
fun TextBadge (badgeColor: Color = BusAppTheme.colors.accent, badgeText: String) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(60.dp)
            .clip(CircleShape)
            .background(badgeColor)
            .padding(horizontal = 2.dp)
    ){
        Text(text = badgeText,
            color = BusAppTheme.colors.onAccent,
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Clip)
    }
}