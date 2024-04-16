package edu.uga.acm.osp.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.uga.acm.osp.ui.theme.BusAppTheme

@Preview
@Composable
private fun TextButtonPreview() {
    TextButton(buttonText = "Test button!!!", buttonType = ButtonType.PRIMARY, {})
}

/**
 * Basic, pill-shaped button containing text and adjusting color to theme and button type.
 *
 * @param buttonText the text to display in the button
 * @param buttonType the type of button
 * @param onClick the method to invoke on click
 * @param modifier optional Modifier
 *
 * @see ButtonType
 */
@Composable
fun TextButton(
    buttonText: String,
    buttonType: ButtonType,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var backgroundColor: Color
    var textColor: Color
    when (buttonType) {
        ButtonType.PRIMARY -> {
            backgroundColor = BusAppTheme.colors.primaryAction
            textColor = BusAppTheme.colors.onPrimaryAction
        }

        ButtonType.SECONDARY -> {
            backgroundColor = BusAppTheme.colors.secondaryAction
            textColor = BusAppTheme.colors.onSecondaryAction
        }
    }

    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor),
        modifier = modifier.height(40.dp)
    ) {
        Text(text = buttonText, color = textColor, style = BusAppTheme.typography.button)
    }
}

/**
 * A clickable icon with circular background.
 *
 * @param icon the icon resource to render
 * @param backgroundColor the color of the circular shape behind the icon
 * @param description a brief description of the icon or the button's functionality, for accesibility
 * @param onClick the method to invoke on click
 */
@Composable
fun IconButton(icon: ImageVector, backgroundColor: Color, description: String, onClick: () -> Unit) {
    Button(onClick = onClick, shape = CircleShape, colors = ButtonDefaults.buttonColors(backgroundColor = backgroundColor)) {
        Icon(
            icon,
            contentDescription = description
        )
    }
}