package com.example.orbit.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.orbit.ui.components.OrbitGradientBackground
import com.example.orbit.ui.components.OrbitTopAppBar
import com.example.orbit.ui.components.GlassmorphicCard
import com.example.orbit.ui.theme.OrbitAccent
import com.example.orbit.ui.theme.OrbitAccentSecondary
import com.example.orbit.ui.theme.OrbitBackgroundDark
import com.example.orbit.ui.theme.OrbitSurface
import com.example.orbit.ui.theme.OrbitSurfaceVariant
import com.example.orbit.ui.theme.OrbitTextPrimary
import com.example.orbit.ui.theme.OrbitTextSecondary
import com.example.orbit.ui.viewmodels.HomeViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToAddAlarm: () -> Unit,
    onNavigateToEditAlarm: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val alarms by viewModel.alarms.collectAsState()

    var currentDate by remember { mutableStateOf(SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(Date())) }
    var currentTime by remember { mutableStateOf(SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())) }
    
    LaunchedEffect(Unit) {
        while(true) {
            val date = Date()
            currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date)
            currentDate = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault()).format(date)
            kotlinx.coroutines.delay(1000)
        }
    }
    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            OrbitTopAppBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddAlarm,
                containerColor = OrbitAccent,
                contentColor = com.example.orbit.ui.theme.OrbitBackgroundDark,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Alarm")
            }
        }
    ) { paddingValues ->
        OrbitGradientBackground {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Live Digital Clock Area
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val timeParts = currentTime.split(":")
                        if (timeParts.size == 3) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(timeParts[0], color = OrbitTextPrimary, fontSize = 64.sp, fontWeight = FontWeight.Black)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("HOURS", color = OrbitTextSecondary, fontSize = 10.sp, letterSpacing = 1.sp)
                            }
                            Text(":", color = OrbitTextPrimary, fontSize = 64.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(bottom = 20.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(timeParts[1], color = OrbitTextPrimary, fontSize = 64.sp, fontWeight = FontWeight.Black)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("MINUTES", color = OrbitTextSecondary, fontSize = 10.sp, letterSpacing = 1.sp)
                            }
                            Text(":", color = OrbitTextPrimary, fontSize = 64.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(bottom = 20.dp))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(timeParts[2], color = OrbitTextPrimary, fontSize = 64.sp, fontWeight = FontWeight.Black)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("SECONDS", color = OrbitTextSecondary, fontSize = 10.sp, letterSpacing = 1.sp)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Good morning, Chaitanya",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = OrbitTextPrimary
                    )
                    Text(
                        text = currentDate,
                        fontSize = 14.sp,
                        color = OrbitTextSecondary,
                        modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(alarms) { alarm ->
                    GlassmorphicCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp)
                            .clickable { onNavigateToEditAlarm(alarm.id) }
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Filled.Alarm, contentDescription = null, tint = OrbitAccent, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("ALARM", color = OrbitAccent, fontSize = 12.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                                }
                                Switch(
                                    checked = alarm.isEnabled,
                                    onCheckedChange = { isChecked ->
                                        viewModel.toggleAlarm(alarm, isChecked)
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color.White,
                                        checkedTrackColor = OrbitAccent,
                                        uncheckedThumbColor = OrbitTextSecondary,
                                        uncheckedTrackColor = OrbitSurfaceVariant
                                    ),
                                    modifier = Modifier.height(24.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        val timeParts = alarm.alarmTime.split(" ")
                                        val time = timeParts.getOrNull(0) ?: alarm.alarmTime
                                        val amPm = timeParts.getOrNull(1) ?: ""
                                        
                                        Text(
                                            text = time,
                                            fontSize = 56.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = OrbitTextPrimary
                                        )
                                        if (amPm.isNotEmpty()) {
                                            Text(
                                                text = amPm,
                                                fontSize = 24.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = OrbitAccent,
                                                modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
                                            )
                                        }
                                    }
                                    Text(
                                        text = "Today",
                                        fontSize = 14.sp,
                                        color = OrbitTextSecondary
                                    )
                                }
                                Icon(Icons.Filled.ChevronRight, contentDescription = "Edit", tint = OrbitTextSecondary)
                            }
                        }
                    }
                }
                
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}
