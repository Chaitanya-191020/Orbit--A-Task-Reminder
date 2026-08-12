package com.example.orbit.ui.screens.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Circle
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
import com.example.orbit.ui.theme.OrbitError
import com.example.orbit.ui.theme.OrbitSurface
import com.example.orbit.ui.theme.OrbitSurfaceVariant
import com.example.orbit.ui.theme.OrbitTextPrimary
import com.example.orbit.ui.theme.OrbitTextSecondary
import com.example.orbit.ui.theme.OrbitWarning
import com.example.orbit.ui.viewmodels.TasksViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel(),
    onNavigateToAddTask: () -> Unit = {},
    onNavigateToEditTask: (String) -> Unit = {}
) {
    val tasks by viewModel.tasks.collectAsState()
    val dateFormat = SimpleDateFormat("MMMM d, yyyy", Locale.US)
    val todayString = dateFormat.format(Date())

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            OrbitTopAppBar()
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToAddTask,
                containerColor = OrbitAccent,
                contentColor = com.example.orbit.ui.theme.OrbitBackgroundDark,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add Task")
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Today's Tasks", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = OrbitTextPrimary)
                            Text(todayString, fontSize = 14.sp, color = OrbitTextSecondary, modifier = Modifier.padding(top = 4.dp))
                        }
                        IconButton(onClick = { }) {
                            Icon(Icons.Filled.FilterList, contentDescription = "Filter", tint = OrbitTextSecondary)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                }

                val firstUncompletedIndex = tasks.indexOfFirst { !it.isCompleted }

                itemsIndexed(tasks) { index, task ->
                    val isCompleted = task.isCompleted
                    val isActive = index == firstUncompletedIndex
                    val isFuture = !isCompleted && !isActive

                    val isPartOfLoop = !task.loopId.isNullOrEmpty()
                    val prevIsSameLoop = index > 0 && tasks[index - 1].loopId == task.loopId
                    val nextIsSameLoop = index < tasks.size - 1 && tasks[index + 1].loopId == task.loopId

                    val prevIsCompleted = if (prevIsSameLoop) tasks[index - 1].isCompleted else false
                    val prevIsActive = if (prevIsSameLoop) (index - 1 == firstUncompletedIndex) else false

                    val timeString = task.dueDate?.let {
                        SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(it))
                    } ?: ""
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(bottom = 16.dp)
                    ) {
                        if (isPartOfLoop && (prevIsSameLoop || nextIsSameLoop)) {
                            TimelineNode(
                                isCompleted = isCompleted,
                                isActive = isActive,
                                isFuture = isFuture,
                                isFirst = !prevIsSameLoop,
                                isLast = !nextIsSameLoop,
                                prevIsCompleted = prevIsCompleted,
                                prevIsActive = prevIsActive
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        
                        Box(modifier = Modifier.weight(1f)) {
                            TaskCard(
                                title = task.title,
                                timeString = timeString,
                                isCompleted = isCompleted,
                                isActive = isActive,
                                onToggle = { viewModel.toggleTaskCompletion(task) },
                                onClick = { onNavigateToEditTask(task.id) }
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
fun TimelineNode(
    isCompleted: Boolean,
    isActive: Boolean,
    isFuture: Boolean,
    isFirst: Boolean,
    isLast: Boolean,
    prevIsCompleted: Boolean,
    prevIsActive: Boolean
) {
    val currentColor = when {
        isCompleted -> OrbitAccentSecondary
        isActive -> OrbitAccent
        else -> OrbitTextSecondary.copy(alpha = 0.5f)
    }

    val topColor = when {
        isFirst -> Color.Transparent
        prevIsCompleted -> OrbitAccentSecondary
        prevIsActive -> OrbitAccent
        else -> OrbitTextSecondary.copy(alpha = 0.5f)
    }
    
    val bottomColor = when {
        isLast -> Color.Transparent
        isCompleted -> OrbitAccentSecondary
        isActive -> OrbitAccent
        else -> OrbitTextSecondary.copy(alpha = 0.5f)
    }

    val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    val bottomPathEffect = if (isFuture || (isActive && !isLast)) dashEffect else null
    val topPathEffect = if (!prevIsCompleted && !isFirst) dashEffect else null

    Canvas(modifier = Modifier.width(32.dp).fillMaxHeight()) {
        val circleRadius = 10.dp.toPx()
        val circleCenterY = 48.dp.toPx() // aligns approximately with card center
        val centerX = size.width / 2f

        // Top line
        if (!isFirst) {
            drawLine(
                color = topColor,
                start = Offset(centerX, 0f),
                end = Offset(centerX, circleCenterY - circleRadius - 4.dp.toPx()),
                strokeWidth = 2.dp.toPx(),
                pathEffect = topPathEffect
            )
        }

        // Bottom line
        if (!isLast) {
            drawLine(
                color = bottomColor,
                start = Offset(centerX, circleCenterY + circleRadius + 4.dp.toPx()),
                end = Offset(centerX, size.height),
                strokeWidth = 2.dp.toPx(),
                pathEffect = bottomPathEffect
            )
        }

        // Horizontal connecting line
        drawLine(
            color = OrbitSurfaceVariant,
            start = Offset(centerX + circleRadius + 4.dp.toPx(), circleCenterY),
            end = Offset(size.width, circleCenterY),
            strokeWidth = 1.dp.toPx()
        )

        // Status Circle
        drawCircle(
            color = currentColor,
            radius = circleRadius,
            style = Stroke(width = 2.dp.toPx())
        )

        if (isCompleted) {
            val checkPath = androidx.compose.ui.graphics.Path().apply {
                moveTo(centerX - 4.dp.toPx(), circleCenterY)
                lineTo(centerX - 1.dp.toPx(), circleCenterY + 3.dp.toPx())
                lineTo(centerX + 4.dp.toPx(), circleCenterY - 4.dp.toPx())
            }
            drawPath(
                path = checkPath,
                color = currentColor,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = androidx.compose.ui.graphics.StrokeJoin.Round)
            )
        } else if (isActive) {
            drawCircle(
                color = currentColor,
                radius = circleRadius / 2f
            )
        }
    }
}

@Composable
fun TaskCard(
    title: String,
    timeString: String,
    isCompleted: Boolean,
    isActive: Boolean,
    onToggle: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    val contentColor = when {
        isCompleted -> OrbitAccentSecondary
        isActive -> OrbitAccent
        else -> OrbitTextSecondary.copy(alpha = 0.5f)
    }

    GlassmorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrbitTextPrimary
                )
                
                if (timeString.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Schedule, contentDescription = null, tint = contentColor, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(timeString, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = contentColor)
                    }
                }
            }
            
            IconButton(onClick = { onToggle(!isCompleted) }) {
                Icon(
                    imageVector = if (isCompleted) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                    contentDescription = "Complete",
                    tint = contentColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    }
}
