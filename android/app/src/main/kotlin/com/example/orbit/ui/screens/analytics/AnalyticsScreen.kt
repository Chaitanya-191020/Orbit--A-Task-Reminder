package com.example.orbit.ui.screens.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.orbit.ui.components.OrbitGradientBackground
import com.example.orbit.ui.theme.OrbitAccent
import com.example.orbit.ui.theme.OrbitTeal
import com.example.orbit.ui.theme.OrbitTextPrimary
import com.example.orbit.ui.theme.OrbitTextSecondary
import com.example.orbit.ui.viewmodels.AnalyticsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel = hiltViewModel()) {
    val analyticsData by viewModel.analyticsData.collectAsState()

    OrbitGradientBackground {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Analytics", fontSize = 24.sp, fontWeight = FontWeight.Medium, color = OrbitTextPrimary)
                        Text("Productivity in numbers", fontSize = 14.sp, color = OrbitTextSecondary)
                    }
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                item { Spacer(modifier = Modifier.height(24.dp)) }
                
                item {
                    if (analyticsData == null) {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = OrbitAccent, strokeWidth = 2.dp)
                        }
                    } else {
                        val data = analyticsData!!
                        
                        // Stat Row 1
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Timer, contentDescription = null, tint = OrbitAccent, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Focus Time", fontSize = 14.sp, color = OrbitTextSecondary)
                                val hours = data.focus_time_30_days / 60
                                val mins = data.focus_time_30_days % 60
                                Text("${hours}h ${mins}m", fontSize = 32.sp, fontWeight = FontWeight.Light, color = OrbitTextPrimary)
                            }
                        }
                        Divider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(bottom = 32.dp))

                        // Stat Row 2
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = OrbitTeal, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Tasks Completed", fontSize = 14.sp, color = OrbitTextSecondary)
                                val completionStr = String.format("%.0f%%", data.tasks_completion_rate_30_days * 100)
                                Text(completionStr, fontSize = 32.sp, fontWeight = FontWeight.Light, color = OrbitTextPrimary)
                            }
                        }
                        Divider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(bottom = 32.dp))

                        // Stat Row 3
                        Row(modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Analytics, contentDescription = null, tint = OrbitAccent, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Total Habit Streaks", fontSize = 14.sp, color = OrbitTextSecondary)
                                Text("${data.total_habit_streaks}", fontSize = 32.sp, fontWeight = FontWeight.Light, color = OrbitTextPrimary)
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}
