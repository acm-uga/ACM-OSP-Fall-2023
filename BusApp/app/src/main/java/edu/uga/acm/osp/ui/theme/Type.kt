package edu.uga.acm.osp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/*val robotoFamily = FontFamily(
    Font(R.font.Roboto_Regular, FontWeight.Default),
    Font(R.font.Roboto_Italic, FontWeight.Defualt, FontStyle.Italic),
    Font(R.font.Roboto_Medium, FontWeight.SemiBold),
    Font(R.font.Roboto_Bold, FontWeight.Default, FontStyle.Bold),
    Font(R.font.Roboto_BoldItalic, FontWeight.Default, FontStyle.BoldItalic) // TODO check fontstyles and correct typography references
) */

// Typography styles as defined in the Figma Mockup
val Typography = Typography(
    // Container headers
    h1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),

    // Element headers
    h2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),

    // Page headers
    h3 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 22.sp
    ),

    // Buttons and calls to action (important text)
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),

    // Element subheaders
    subtitle1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp
    ),

    // Regular body text
    body2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontSize = 11.sp
    ),

    // Context
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp
    )
)