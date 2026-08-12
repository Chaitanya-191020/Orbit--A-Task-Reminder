package com.example.orbit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.example.orbit.ui.theme.OrbitSurface

@Composable
fun GlassmorphicCard(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp), // Increased rounding for extra sleekness
    backgroundColor: Color = OrbitSurface,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(backgroundColor.copy(alpha = 0.35f)) // More transparent for better glass effect on black
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.15f),
                shape = shape
            ),
        content = content
    )
}
