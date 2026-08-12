package com.example.orbit.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.orbit.ui.components.OrbitGradientBackground
import com.example.orbit.ui.theme.OrbitTextPrimary
import com.example.orbit.ui.theme.OrbitTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToFocusSettings: () -> Unit
) {
    var themeState by remember { mutableStateOf("System Default") }

    OrbitGradientBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Settings", color = OrbitTextPrimary) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Theme Setting
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp)
                        .clickable {
                            // Toggle placeholder for theme
                            themeState = if (themeState == "System Default") "Dark Mode" else "System Default"
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Palette, contentDescription = "Theme", tint = OrbitTextPrimary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Theme", color = OrbitTextPrimary, style = MaterialTheme.typography.titleMedium)
                            Text(
                                themeState,
                                color = OrbitTextSecondary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Icon(Icons.Filled.ChevronRight, contentDescription = "Change Theme", tint = OrbitTextSecondary)
                }
                Divider(color = Color.LightGray.copy(alpha = 0.2f))

                // Focus Mode Setting
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToFocusSettings() }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Timer, contentDescription = "Focus", tint = OrbitTextPrimary, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Focus Mode Settings", color = OrbitTextPrimary, style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Configure focus duration, strict mode, and sounds",
                                color = OrbitTextSecondary,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                    Icon(Icons.Filled.ChevronRight, contentDescription = "Navigate", tint = OrbitTextSecondary)
                }
                Divider(color = Color.LightGray.copy(alpha = 0.2f))
            }
        }
    }
}
