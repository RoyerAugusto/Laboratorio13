package com.example.ejerciciosadicionales.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Seashell = Color(0xFFFFF5EE)
val GreenLight = Color(0xFFD7EBD9)
val Green = Color(0xFF2E7D32)
val PaleDogwood = Color(0xFFE8B4A0)
val Dogwood = Color(0xFFB5543A)

private val Colors = lightColorScheme(
    primary = Green,
    secondary = Dogwood,
    background = Seashell,
    surface = Color.White
)

@Composable
fun AnimacionesTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Colors, content = content)
}
