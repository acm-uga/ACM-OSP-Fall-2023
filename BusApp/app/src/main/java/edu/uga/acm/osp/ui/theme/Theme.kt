package edu.uga.acm.osp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

// Theme-specific color palettes as defined in the Figma Mockup
// Uses a custom color palette class to avoid adhering to the Material You Palette's limitations
private val darkColorPalette = ColorPalette(
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
    onStatus = AsphaltBlack
)

private val lightColorPalette = ColorPalette(
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

private val LocalReplacementDarkColors = staticCompositionLocalOf { darkColorPalette }
private val LocalReplacementLightColors = staticCompositionLocalOf { lightColorPalette }

// TODO presumably the BusAppTheme colors aren't changing because CurrentLocalReplacementColors
// isn't accessible below (HERE!!!). Look into how buttons can change variables outside of a composable
// and consider implementing that approach here so that CurrentLocalReplacementColors is set
// by the function below and is therefore correct in the BusAppTheme object
@Composable
fun BusAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    localReplacementDarkColors: ProvidableCompositionLocal<ColorPalette> = LocalReplacementDarkColors,
    localReplacementLightcolors: ProvidableCompositionLocal<ColorPalette> = LocalReplacementLightColors,
    content: @Composable () -> Unit
) {
    val localReplacementColors = if (darkTheme) localReplacementDarkColors else localReplacementLightcolors
    val replacementColors = if (darkTheme) darkColorPalette else lightColorPalette

    // Allows us to override the Material You color palette class' limitations by implementing our
    // own ColorPalette class as the color system for BusTheme
    CompositionLocalProvider(
        localReplacementColors provides replacementColors
    ) {
        MaterialTheme(
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}

@Composable
fun CurrentColorPalette(
    darkTheme: Boolean = isSystemInDarkTheme(),
    localReplacementDarkColors: ProvidableCompositionLocal<ColorPalette> = LocalReplacementDarkColors,
    localReplacementLightcolors: ProvidableCompositionLocal<ColorPalette> = LocalReplacementLightColors,
): ProvidableCompositionLocal<ColorPalette> {
    return if (darkTheme) localReplacementDarkColors else localReplacementLightcolors
}

// Use with eg. BusAppTheme.colors.primary
object BusAppTheme {
    val colors: ColorPalette
        @Composable
        get() = CurrentColorPalette().current

    val typography = Typography
    val shapes = Shapes
}