package edu.uga.acm.osp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.uga.acm.osp.ui.theme.BusAppTheme

@Composable
fun IconBadge (badgeColor: Color, badgeIcon: ImageVector, badgeIconDesc: String) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(55.dp)
            .clip(CircleShape)
            .background(badgeColor)
            .padding(horizontal = 10.dp)
    ){
        Icon(imageVector = badgeIcon, badgeIconDesc, tint = BusAppTheme.colors.onAccent,
            modifier = Modifier
                .size(35.dp)
        )
    }
}

@Composable
fun TextBadge (badgeColor: Color, badgeText: String) {
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(55.dp)
            .clip(CircleShape)
            .background(badgeColor)
            .padding(horizontal = 10.dp)
    ){
        Text(text = badgeText,
            color = BusAppTheme.colors.onAccent,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Clip)
    }
}