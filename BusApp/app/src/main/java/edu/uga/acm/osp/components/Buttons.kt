package edu.uga.acm.osp.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


//Pass String and color references to make a button with text inside
@Composable
fun TextButton(onClickFunction: () -> Unit , textButton: String, colorBackground: Color, colorText: Color) {
    Button(
        onClick = onClickFunction,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = colorBackground)
    ) {
        Text(textButton, color = colorText)
    }
}


//Pass image vector and color references to make a button with an icon inside
@Composable
fun IconButton(onClickFunction: () -> Unit ,iconName : ImageVector, colorBackground : Color, description : String) {
    Button(onClick = onClickFunction, shape = CircleShape, colors = ButtonDefaults.buttonColors(backgroundColor = colorBackground)) {
        Icon(
            iconName,
            contentDescription = description
        )
    }
}