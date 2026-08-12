package com.example.orbit.ui.screens.tasks

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChevronRight
import com.example.orbit.ui.components.GlassmorphicCard
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.background
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.clip
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.orbit.ui.components.OrbitGradientBackground
import com.example.orbit.ui.components.WheelTimePicker
import com.example.orbit.ui.theme.OrbitAccent
import com.example.orbit.ui.theme.OrbitSurface
import com.example.orbit.ui.theme.OrbitSurfaceVariant
import com.example.orbit.ui.theme.OrbitTextPrimary
import com.example.orbit.ui.theme.OrbitTextSecondary
import com.example.orbit.ui.viewmodels.TasksViewModel
import java.util.Locale
import androidx.compose.foundation.shape.RoundedCornerShape
import java.text.SimpleDateFormat
import java.util.Calendar

class TaskEntryState(title: String = "", startTime: String = "", endTime: String = "") {
    var title by mutableStateOf(title)
    var startTime by mutableStateOf(startTime)
    var endTime by mutableStateOf(endTime)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    taskId: String? = null,
    onNavigateBack: () -> Unit,
    viewModel: TasksViewModel = hiltViewModel()
) {
    val taskEntries = remember { mutableStateListOf(TaskEntryState()) }
    
    var activePicker by remember { mutableStateOf<Pair<Int, String>?>(null) } // index and "start" or "end"
    var tempHour by remember { mutableStateOf(8) }
    var tempMinute by remember { mutableStateOf(0) }

    LaunchedEffect(taskId) {
        if (taskId != null) {
            val task = viewModel.getTaskById(taskId)
            if (task != null) {
                taskEntries[0].title = task.title
                taskEntries[0].startTime = task.startTime ?: ""
                taskEntries[0].endTime = task.endTime ?: ""
            }
        }
    }

    OrbitGradientBackground {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .padding(top = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.background(OrbitSurfaceVariant, RoundedCornerShape(20.dp)).size(40.dp)
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = OrbitTextPrimary)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(if (taskId != null) "Edit Task" else "Add Task(s)", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = OrbitTextPrimary)
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    itemsIndexed(taskEntries) { index, entry ->
                        Column(modifier = Modifier.padding(bottom = 24.dp)) {
                            // Title Card
                            GlassmorphicCard(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    // Icon Box
                                    Box(
                                        modifier = Modifier.size(48.dp).background(OrbitAccent.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(androidx.compose.material.icons.Icons.Outlined.Edit, contentDescription = null, tint = OrbitAccent)
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(if (taskId != null) "Task Title" else "Task Title ${index + 1}", color = OrbitTextSecondary, fontSize = 12.sp)
                                        BasicTextField(
                                            value = entry.title,
                                            onValueChange = { entry.title = it },
                                            textStyle = androidx.compose.ui.text.TextStyle(color = OrbitTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold),
                                            cursorBrush = SolidColor(OrbitAccent),
                                            singleLine = true,
                                            decorationBox = { innerTextField ->
                                                if (entry.title.isEmpty()) {
                                                    Text("Enter title...", color = OrbitTextSecondary.copy(alpha=0.5f), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                                }
                                                innerTextField()
                                            }
                                        )
                                    }
                                    if (entry.title.isNotEmpty()) {
                                        IconButton(onClick = { entry.title = "" }, modifier = Modifier.size(24.dp)) {
                                            Icon(androidx.compose.material.icons.Icons.Filled.Cancel, contentDescription = "Clear", tint = OrbitTextSecondary)
                                        }
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            // Time Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                // Start Time
                                GlassmorphicCard(
                                    modifier = Modifier.weight(1f).clickable { activePicker = Pair(index, "start") }
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier.size(36.dp).background(OrbitAccent.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(androidx.compose.material.icons.Icons.Outlined.Schedule, contentDescription = null, tint = OrbitAccent, modifier = Modifier.size(20.dp))
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("Start Time", color = OrbitAccent, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                            Text(entry.startTime.ifEmpty { "00:00" }, color = OrbitTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Icon(androidx.compose.material.icons.Icons.Filled.ChevronRight, contentDescription = null, tint = OrbitTextSecondary, modifier = Modifier.size(16.dp))
                                    }
                                }
                                
                                // End Time
                                GlassmorphicCard(
                                    modifier = Modifier.weight(1f).clickable { activePicker = Pair(index, "end") }
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier.size(36.dp).background(OrbitAccent.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(androidx.compose.material.icons.Icons.Outlined.Flag, contentDescription = null, tint = OrbitAccent, modifier = Modifier.size(20.dp))
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text("End Time", color = OrbitAccent, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                                            Text(entry.endTime.ifEmpty { "00:00" }, color = OrbitTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                        }
                                        Icon(androidx.compose.material.icons.Icons.Filled.ChevronRight, contentDescription = null, tint = OrbitTextSecondary, modifier = Modifier.size(16.dp))
                                    }
                                }
                            }
                        }
                    }

                    if (taskId == null) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(100.dp)
                                    .drawBehind {
                                        drawRoundRect(
                                            color = OrbitAccent.copy(alpha = 0.5f),
                                            style = androidx.compose.ui.graphics.drawscope.Stroke(
                                                width = 2.dp.toPx(),
                                                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                                            ),
                                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(24.dp.toPx())
                                        )
                                    }
                                    .background(OrbitSurfaceVariant.copy(alpha=0.3f), RoundedCornerShape(24.dp))
                                    .clickable {
                                        val lastEntry = taskEntries.last()
                                        var newStartTime = ""
                                        var newEndTime = ""
                                        try {
                                            val sdf = SimpleDateFormat("hh:mm a", Locale.US)
                                            if (lastEntry.endTime.isNotBlank() && lastEntry.startTime.isNotBlank()) {
                                                val lastStart = sdf.parse(lastEntry.startTime)
                                                val lastEnd = sdf.parse(lastEntry.endTime)
                                                if (lastStart != null && lastEnd != null) {
                                                    val duration = lastEnd.time - lastStart.time
                                                    val cal = Calendar.getInstance()
                                                    cal.time = lastEnd
                                                    cal.add(Calendar.MINUTE, 10) // 10 minute break
                                                    newStartTime = sdf.format(cal.time)
                                                    
                                                    cal.timeInMillis = cal.timeInMillis + duration
                                                    newEndTime = sdf.format(cal.time)
                                                }
                                            }
                                        } catch (e: Exception) {
                                            // Ignore parse errors
                                        }
                                        taskEntries.add(TaskEntryState(startTime = newStartTime, endTime = newEndTime))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(androidx.compose.material.icons.Icons.Filled.Add, contentDescription = "Add", tint = OrbitAccent, modifier = Modifier.size(24.dp))
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("Add task to loop", color = OrbitTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Repeat this task multiple times", color = OrbitTextSecondary, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
                
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                        .height(64.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                                colors = listOf(Color(0xFF1E3A8A), OrbitAccent)
                            )
                        )
                        .clickable(enabled = taskEntries.any { it.title.isNotBlank() }) {
                            val validTasks = taskEntries.filter { it.title.isNotBlank() }
                            if (validTasks.isNotEmpty()) {
                                if (taskId != null) {
                                    viewModel.updateTask(taskId, validTasks[0].title, validTasks[0].startTime, validTasks[0].endTime)
                                } else {
                                    viewModel.addLoopedTasks(validTasks.map { TasksViewModel.TaskData(it.title, it.startTime, it.endTime) })
                                }
                                onNavigateBack()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(androidx.compose.material.icons.Icons.Outlined.Save, contentDescription = null, tint = OrbitTextPrimary, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (taskId != null) "Save Task" else "Save Tasks", 
                            fontSize = 16.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = OrbitTextPrimary
                        )
                    }
                } // closes Box
            } // closes Column
        } // closes Scaffold
        
        if (activePicker != null) {
            val (pickerIndex, pickerType) = activePicker!!
            ModalBottomSheet(
                onDismissRequest = { activePicker = null },
                containerColor = OrbitSurface,
                dragHandle = { BottomSheetDefaults.DragHandle(color = OrbitTextSecondary) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (pickerType == "start") "Select Start Time" else "Select End Time",
                        style = MaterialTheme.typography.titleLarge,
                        color = OrbitTextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    WheelTimePicker(
                        initialHour = tempHour,
                        initialMinute = tempMinute,
                        onTimeSelected = { h, m -> 
                            tempHour = h
                            tempMinute = m
                        }
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = {
                            val ampm = if (tempHour >= 12) "PM" else "AM"
                            val h12 = if (tempHour % 12 == 0) 12 else tempHour % 12
                            val timeString = String.format(Locale.US, "%02d:%02d %s", if(h12==0) 12 else h12, tempMinute, ampm)
                            if (pickerType == "start") {
                                taskEntries[pickerIndex].startTime = timeString
                            } else {
                                taskEntries[pickerIndex].endTime = timeString
                            }
                            activePicker = null
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrbitAccent)
                    ) {
                        Text("OK", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = com.example.orbit.ui.theme.OrbitBackgroundDark)
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
