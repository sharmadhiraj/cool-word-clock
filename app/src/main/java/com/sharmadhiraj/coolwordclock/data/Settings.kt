package com.sharmadhiraj.coolwordclock.data

import androidx.compose.ui.graphics.Color

data class Settings(
    val chime: Boolean = false,
    val minuteDots: Boolean = true,
    val nightDim: Boolean = false,
    val accent: Accent = Accent.White,
)

enum class Accent(val color: Color) {
    White(Color(0xFFFFFFFF)),
    Amber(Color(0xFFFFC46B)),
    Mint(Color(0xFF7EE8C1)),
    Sky(Color(0xFF8CC8FF)),
    Rose(Color(0xFFFF8FA3)),
}
