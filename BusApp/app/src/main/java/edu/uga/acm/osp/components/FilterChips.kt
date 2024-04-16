package edu.uga.acm.osp.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.uga.acm.osp.ui.theme.BusAppTheme

@Preview
@Composable
fun FilterChipPreview() {
    FilterChip(filterText = "Filter Text", onClick = {})
}

@Composable
fun FilterChip(
    filterText: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = BusAppTheme.colors.container
        ),
        modifier = Modifier.height(30.dp)
    ) {
        Text(
            text = filterText,
            color = BusAppTheme.colors.onContainerPrimary,
            style = BusAppTheme.typography.caption)
    }
}