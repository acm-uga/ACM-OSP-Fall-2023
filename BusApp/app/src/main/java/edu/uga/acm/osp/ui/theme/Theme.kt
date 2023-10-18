package edu.uga.acm.osp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

// Theme-specific color palettes as defined in the Figma Mockup
private val DarkColorPalette = darkColors(
    background = AsphaltBlack, // Page backgrounds
    onBackgroundPrimary = ChapelBellWhite, // Primary info (e.g. headers)
    onBackgroundSecondary = BusGray,// Secondary info (e.g. context)

    accent = BulldogRed, // Accented components
    onAccent = ChapelBellWhite,

    primaryAction = GloryGloryRed, // Primary actions
    onPrimaryAction = ChapelBellWhite,

    secondaryAction = BoydGray, // Secondary actions
    onSecondaryAction = AsphaltBlack,

    tertiaryAction = AsphaltBlack, // Tertiary actions
    onTertiaryAction = BusGray,

    container = ArchBlack, // Containers on background
    onContainerPrimary = ChapelBellWhite, // Primary info (e.g. headers)
    onContainerSecondary = BusGray, // Secondary info (e.g. context)

    negativeStatus = TripleGray, // Setting is disabled
    positiveStatus = BulldogRed, // Enabled settings
    onStatus = AsphaltGray
)

private val LightColorPalette = lightColors(
    background = BoydGray, // Page backgrounds
    onBackgroundPrimary = ArchBlack, // Primary info (e.g. headers)
    onBackgroundSecondary = TripleGray,// Secondary info (e.g. context)

    accent = BulldogRed, // Accented components
    onAccent = ChapelBellWhite,

    primaryAction = GloryGloryRed, // Primary actions
    onPrimaryAction = ChapelBellWhite,

    secondaryAction = TripleGray, // Secondary actions
    onSecondaryAction = BoydGray,

    tertiaryAction = BoydGray, // Tertiary actions
    onTertiaryAction = TripleGray,

    container = ChapelBellWhite, // Containers on background
    onContainerPrimary = ArchBlack, // Primary info (e.g. headers)
    onContainerSecondary = TripleGray, // Secondary info (e.g. context)

    negativeStatus = BusGray, // Setting is disabled
    positiveStatus = BulldogRed, // Enabled settings
    onStatus = ChapelBellWhite
)

@Composable
fun BusAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}