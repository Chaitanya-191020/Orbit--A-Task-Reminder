package com.example.orbit.ui.screens.habits

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.orbit.ui.viewmodels.HabitsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitsGoalsScreen(viewModel: HabitsViewModel = hiltViewModel()) {
    val habits by viewModel.habits.collectAsState()
    val goals by viewModel.goals.collectAsState()

    OrbitGradientBackground {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Habits & Goals", fontSize = 24.sp, fontWeight = FontWeight.Medium, color = OrbitTextPrimary)
                        Text("Track your targets", fontSize = 14.sp, color = OrbitTextSecondary)
                    }
                    IconButton(onClick = { /* Add Habit */ }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add", tint = OrbitTextSecondary)
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
                item { Spacer(modifier = Modifier.height(16.dp)) }

                item {
                    Text("Your Habits", fontSize = 12.sp, color = OrbitTextSecondary, letterSpacing = 2.sp, modifier = Modifier.padding(bottom = 16.dp))
                }
                
                if (habits.isEmpty()) {
                    item {
                        Text("No habits found.", color = OrbitTextSecondary, fontWeight = FontWeight.Light, modifier = Modifier.padding(bottom = 32.dp))
                    }
                } else {
                    items(habits) { habit ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.LocalFireDepartment, contentDescription = "Streak", tint = OrbitAccent, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(habit.title, fontSize = 18.sp, fontWeight = FontWeight.Light, color = OrbitTextPrimary)
                                Text("${habit.streak_count} Days", fontSize = 14.sp, color = OrbitTextSecondary)
                            }
                        }
                        Divider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(bottom = 16.dp))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text("Your Goals", fontSize = 12.sp, color = OrbitTextSecondary, letterSpacing = 2.sp, modifier = Modifier.padding(bottom = 16.dp))
                }

                if (goals.isEmpty()) {
                    item {
                        Text("No goals found.", color = OrbitTextSecondary, fontWeight = FontWeight.Light, modifier = Modifier.padding(bottom = 16.dp))
                    }
                } else {
                    items(goals) { goal ->
                        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.Star, contentDescription = "Goal", tint = OrbitTeal, modifier = Modifier.size(24.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(goal.title, fontSize = 18.sp, fontWeight = FontWeight.Light, color = OrbitTextPrimary)
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            LinearProgressIndicator(
                                progress = goal.progress_percentage,
                                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                                color = OrbitTeal,
                                trackColor = Color.LightGray.copy(alpha = 0.3f)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val perc = (goal.progress_percentage * 100).toInt()
                            Text("${perc}% Completed", fontSize = 12.sp, color = OrbitTextSecondary, modifier = Modifier.align(Alignment.End))
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}
