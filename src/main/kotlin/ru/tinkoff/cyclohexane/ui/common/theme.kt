package ru.tinkoff.cyclohexane.ui.common

import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle

object AppTheme {
    val colors: Colors = Colors()

    class Colors(
        val backgroundDark: Color = Color(0xFF2B2B2B),
        val backgroundMedium: Color = Color(0xFF3C3F41),
        val backgroundLight: Color = Color(0xFF4E5254),

        val connected: Color = Color(0x49FF33),
        val disconnected: Color = Color(0x9E061F),

        val material: androidx.compose.material.Colors = darkColors(
            background = backgroundDark,
            surface = backgroundMedium,
            primary = Color.White
        ),
    )
}
