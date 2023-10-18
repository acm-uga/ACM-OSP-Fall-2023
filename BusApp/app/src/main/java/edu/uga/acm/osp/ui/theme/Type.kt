package edu.uga.acm.osp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// TODO: ADD ROBOTO FONT FAMILY
// Typography styles as defined in the Figma Mockup
val Typography = Typography(
    // Container headers
    header1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),

    // Element headers
    header2 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    ),

    // Page headers
    header3 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 20.sp
    ),

    // Container subheaders
    subheader1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp
    ),

    // Element subheaders
    subheader1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp
    ),

    // Context
    context = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp
    ),

    // Container subheaders
    body = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Regular,
        fontSize = 11.sp
    ),
)