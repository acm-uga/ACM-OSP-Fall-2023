package edu.uga.acm.osp.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

// Custom color palette class to store our app's required colors without the limitations of
// Material You's predefined color names and use-cases
@Immutable
data class ColorPalette(
    var background: Color,// Page backgrounds
    var onBackgroundPrimary: Color, // Primary info (e.g. headers)
    var onBackgroundSecondary: Color,// Secondary info (e.g. context)

    var accent: Color, // Accented components
    var onAccent: Color,

    var primaryAction: Color, // Primary actions
    var onPrimaryAction: Color,

    var secondaryAction: Color, // Secondary actions
    var onSecondaryAction: Color,

    var tertiaryAction: Color, // Tertiary actions
    var onTertiaryAction: Color,

    var container: Color, // Containers on background
    var onContainerPrimary: Color, // Primary info (e.g. headers)
    var onContainerSecondary: Color, // Secondary info (e.g. context)

    var negativeStatus: Color, // Setting is disabled
    var positiveStatus: Color, // Enabled settings
    var onStatus: Color
)