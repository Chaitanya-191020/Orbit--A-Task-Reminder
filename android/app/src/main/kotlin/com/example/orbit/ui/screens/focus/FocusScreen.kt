package com.example.orbit.ui.screens.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orbit.ui.components.OrbitGradientBackground
import com.example.orbit.ui.components.OrbitTopAppBar
import com.example.orbit.ui.components.GlassmorphicCard
import com.example.orbit.ui.theme.OrbitAccent
import com.example.orbit.ui.theme.OrbitAccentSecondary
import com.example.orbit.ui.theme.OrbitSurface
import com.example.orbit.ui.theme.OrbitSurfaceVariant
import com.example.orbit.ui.theme.OrbitTextPrimary
import com.example.orbit.ui.theme.OrbitTextSecondary
import com.example.orbit.ui.theme.OrbitWarning
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FocusScreen(
    onNavigateToSettings: () -> Unit = {}
) {
    var isRunning by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableStateOf(25 * 60) } // 25 minutes
    var announceMilestones by remember { mutableStateOf(true) }

    LaunchedEffect(isRunning) {
        while (isRunning && timeLeft > 0) {
            delay(1000)
            timeLeft--
        }
        if (timeLeft == 0) {
            isRunning = false
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60
    val timeString = String.format("%02d:%02d", minutes, seconds)
    val progress = 1f - (timeLeft.toFloat() / (25 * 60).toFloat())

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            OrbitTopAppBar(onSettingsClick = onNavigateToSettings)
        }
    ) { paddingValues ->
        OrbitGradientBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(48.dp))
                
                // Timer Circle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(320.dp)
                ) {
                    CircularProgressIndicator(
                        progress = 1f,
                        modifier = Modifier.fillMaxSize(),
                        color = OrbitSurfaceVariant,
                        strokeWidth = 6.dp
                    )
                    CircularProgressIndicator(
                        progress = progress,
                        modifier = Modifier.fillMaxSize(),
                        color = OrbitAccent,
                        strokeWidth = 6.dp
                    )
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = timeString,
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Bold,
                            color = OrbitTextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(OrbitTextSecondary))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "DEEP WORK",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = OrbitTextSecondary,
                                letterSpacing = 2.sp
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(64.dp))
                
                // Playback Controls
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { timeLeft = 25 * 60; isRunning = false },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(OrbitSurface)
                    ) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Reset", tint = OrbitTextSecondary, modifier = Modifier.size(20.dp))
                    }
                    
                    IconButton(
                        onClick = { isRunning = !isRunning },
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(OrbitAccent)
                    ) {
                        Icon(
                            imageVector = if (isRunning) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                            contentDescription = "Play/Pause",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                    
                    IconButton(
                        onClick = { isRunning = false },
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(OrbitSurface)
                    ) {
                        Icon(Icons.Filled.Pause, contentDescription = "Stop", tint = OrbitTextSecondary, modifier = Modifier.size(20.dp)) // Using pause as stop for mockup accuracy
                    }
                }

                Spacer(modifier = Modifier.height(64.dp))

                // Milestones Card
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.NotificationsActive, contentDescription = null, tint = OrbitTextSecondary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Announce Focus Milestones", color = OrbitTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                        }
                        Switch(
                            checked = announceMilestones,
                            onCheckedChange = { announceMilestones = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = OrbitAccentSecondary,
                                uncheckedThumbColor = OrbitTextSecondary,
                                uncheckedTrackColor = OrbitSurfaceVariant
                            ),
                            modifier = Modifier.height(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("CURRENT STREAK", color = OrbitTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.LocalFireDepartment, contentDescription = null, tint = OrbitWarning, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("5 Days", color = OrbitTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("TOTAL FOCUS", color = OrbitTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Schedule, contentDescription = null, tint = OrbitTextSecondary, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("120m", color = OrbitTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}
