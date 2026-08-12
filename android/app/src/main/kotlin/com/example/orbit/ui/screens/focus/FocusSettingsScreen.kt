package com.example.orbit.ui.screens.focus

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orbit.ui.components.OrbitGradientBackground
import com.example.orbit.ui.theme.OrbitAccent
import com.example.orbit.ui.theme.OrbitTextPrimary
import com.example.orbit.ui.theme.OrbitTextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusSettingsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToAllowedApps: () -> Unit
) {
    var isStrictModeEnabled by remember { mutableStateOf(true) }
    var isFocusSoundEnabled by remember { mutableStateOf(false) }

    OrbitGradientBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Focus Settings", color = OrbitTextPrimary) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = OrbitTextPrimary)
                        }
                    },
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
                // Allowed Apps Setting
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToAllowedApps() }
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Allowed Apps", color = OrbitTextPrimary, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Choose up to 3 apps accessible during focus mode",
                            color = OrbitTextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Icon(Icons.Filled.ChevronRight, contentDescription = "Navigate", tint = OrbitTextSecondary)
                }
                Divider(color = Color.LightGray.copy(alpha = 0.2f))

                // Strict Mode Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Strict Mode", color = OrbitTextPrimary, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Prevents exiting focus mode once started",
                            color = OrbitTextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = isStrictModeEnabled,
                        onCheckedChange = { isStrictModeEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = OrbitAccent, checkedTrackColor = OrbitAccent.copy(alpha = 0.5f))
                    )
                }
                Divider(color = Color.LightGray.copy(alpha = 0.2f))
                
                // Focus Sounds Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Ambient Sounds", color = OrbitTextPrimary, style = MaterialTheme.typography.titleMedium)
                        Text(
                            "Play white noise during focus sessions",
                            color = OrbitTextSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Switch(
                        checked = isFocusSoundEnabled,
                        onCheckedChange = { isFocusSoundEnabled = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = OrbitAccent, checkedTrackColor = OrbitAccent.copy(alpha = 0.5f))
                    )
                }
                Divider(color = Color.LightGray.copy(alpha = 0.2f))

                Spacer(modifier = Modifier.height(32.dp))
                
                Surface(
                    color = OrbitAccent.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Filled.Warning, contentDescription = "Note", tint = OrbitAccent, modifier = Modifier.size(24.dp))
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            "Incoming phone calls are always allowed during Focus Mode for your safety.",
                            color = OrbitTextPrimary,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 18.sp
                        )
                    }
                }
            }
        }
    }
}
