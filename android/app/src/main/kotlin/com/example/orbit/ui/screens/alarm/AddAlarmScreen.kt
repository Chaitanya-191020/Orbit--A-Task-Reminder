package com.example.orbit.ui.screens.alarm

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.Vibration
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.filled.ChevronRight
import com.example.orbit.ui.components.GlassmorphicCard
import androidx.compose.foundation.border
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.orbit.ui.theme.*
import com.example.orbit.ui.viewmodels.HomeViewModel
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import androidx.compose.foundation.clickable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAlarmScreen(
    alarmId: String? = null,
    onNavigateBack: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    var selectedHour by remember { mutableStateOf(6) }
    var selectedMinute by remember { mutableStateOf(30) }
    
    // States for toggles in settings
    // 0 = Ring Once, 1 = Repeat, 2 = Schedule
    var alarmType by remember { mutableIntStateOf(0) }
    var repeatDays by remember { mutableStateOf(setOf<Int>()) }
    var alarmDate by remember { mutableStateOf("Tomorrow") }
    var alarmName by remember { mutableStateOf("") }
    var ringtoneName by remember { mutableStateOf("Default") }
    var ringtoneUri by remember { mutableStateOf<String?>(null) }
    var vibrate by remember { mutableStateOf(true) }
    var snoozeInterval by remember { mutableStateOf(5) }
    var snoozeTimes by remember { mutableStateOf(3) }
    var showSnoozeSheet by remember { mutableStateOf(false) }

    val alarms by viewModel.alarms.collectAsState()

    LaunchedEffect(alarmId, alarms) {
        if (alarmId != null && alarms.isNotEmpty()) {
            val existing = alarms.find { it.id == alarmId }
            if (existing != null) {
                try {
                    val parts = existing.alarmTime.split(":", " ")
                    var h = parts[0].toInt()
                    val m = parts[1].toInt()
                    val amPm = parts.getOrNull(2) ?: "AM"
                    if (amPm == "PM" && h < 12) h += 12
                    if (amPm == "AM" && h == 12) h = 0
                    selectedHour = h
                    selectedMinute = m
                    alarmName = existing.label
                    ringtoneUri = existing.soundUri
                    ringtoneName = existing.ringtoneName ?: "Default"
                    vibrate = existing.vibrate
                    snoozeInterval = existing.snoozeDurationMinutes
                    snoozeTimes = existing.snoozeTimes
                } catch(e: Exception) {}
            }
        }
    }

    val context = LocalContext.current
    val ringtoneLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.getParcelableExtra<Uri>(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            if (uri != null) {
                ringtoneUri = uri.toString()
                val ringtone = RingtoneManager.getRingtone(context, uri)
                ringtoneName = ringtone.getTitle(context) ?: "Custom"
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(OrbitBackgroundDark)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.background(OrbitSurfaceVariant, RoundedCornerShape(20.dp)).size(40.dp)
                ) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = OrbitAccent)
                }
                TextButton(
                    onClick = {
                        val amPm = if (selectedHour >= 12) "PM" else "AM"
                        val h12 = if (selectedHour == 0) 12 else if (selectedHour > 12) selectedHour - 12 else selectedHour
                        val timeStr = String.format("%02d:%02d %s", h12, selectedMinute, amPm)
                        val labelToSave = alarmName.ifBlank { "New Alarm" }
                        if (alarmId != null) {
                            viewModel.updateAlarm(
                                alarmId = alarmId,
                                timeString = timeStr,
                                label = labelToSave,
                                soundUri = ringtoneUri,
                                ringtoneName = ringtoneName,
                                vibrate = vibrate,
                                snoozeDurationMinutes = snoozeInterval,
                                snoozeTimes = snoozeTimes
                            )
                        } else {
                            viewModel.addAlarm(
                                timeString = timeStr,
                                label = labelToSave,
                                soundUri = ringtoneUri,
                                ringtoneName = ringtoneName,
                                vibrate = vibrate,
                                snoozeDurationMinutes = snoozeInterval,
                                snoozeTimes = snoozeTimes
                            )
                        }
                        onNavigateBack()
                    }
                ) {
                    Text("Done", color = OrbitAccent, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Column(modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)) {
                Text(
                    text = if (alarmId != null) "Edit Alarm" else "New Alarm",
                    color = OrbitTextPrimary,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "Set the time for your alarm",
                    color = OrbitTextSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Wheel Time Picker
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentAlignment = Alignment.Center
            ) {
                WheelTimePicker(
                    hour = selectedHour,
                    minute = selectedMinute,
                    onTimeChanged = { h, m ->
                        selectedHour = h
                        selectedMinute = m
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Settings Area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                // Alarm Type Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(OrbitSurfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .then(
                                if (alarmType == 0) Modifier.border(1.dp, OrbitAccent, RoundedCornerShape(20.dp))
                                else Modifier
                            )
                            .clickable { alarmType = 0 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Ring Once", color = if (alarmType == 0) OrbitAccent else OrbitTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .then(
                                if (alarmType == 1) Modifier.border(1.dp, OrbitAccent, RoundedCornerShape(20.dp))
                                else Modifier
                            )
                            .clickable { alarmType = 1 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Repeat", color = if (alarmType == 1) OrbitAccent else OrbitTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .then(
                                if (alarmType == 2) Modifier.border(1.dp, OrbitAccent, RoundedCornerShape(20.dp))
                                else Modifier
                            )
                            .clickable { alarmType = 2 },
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Schedule", color = if (alarmType == 2) OrbitAccent else OrbitTextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                // Settings Card
                GlassmorphicCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        // Label
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.Edit, contentDescription = null, tint = OrbitAccent, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Label", color = OrbitTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.weight(1f))
                            BasicTextField(
                                value = alarmName,
                                onValueChange = { alarmName = it },
                                textStyle = androidx.compose.ui.text.TextStyle(color = OrbitTextSecondary, fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.End),
                                cursorBrush = SolidColor(OrbitAccent),
                                singleLine = true,
                                decorationBox = { innerTextField ->
                                    if (alarmName.isEmpty()) {
                                        Text("New Alarm", color = OrbitTextSecondary.copy(alpha = 0.5f), fontSize = 14.sp, textAlign = androidx.compose.ui.text.style.TextAlign.End, modifier = Modifier.fillMaxWidth())
                                    }
                                    innerTextField()
                                },
                                modifier = Modifier.width(150.dp)
                            )
                        }
                        
                        HorizontalDivider(color = OrbitSurfaceVariant, thickness = 1.dp)

                        // Ringtone
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).clickable { 
                                val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
                                    putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM)
                                    putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
                                    putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
                                }
                                ringtoneLauncher.launch(intent)
                            },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.MusicNote, contentDescription = null, tint = Color(0xFFa855f7), modifier = Modifier.size(20.dp)) // Purple
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Ringtone", color = OrbitTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.weight(1f))
                            Text(ringtoneName, color = OrbitTextSecondary, fontSize = 14.sp, maxLines = 1, modifier = Modifier.widthIn(max = 120.dp), overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = OrbitTextSecondary, modifier = Modifier.size(16.dp))
                        }
                        
                        HorizontalDivider(color = OrbitSurfaceVariant, thickness = 1.dp)

                        // Vibrate
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.Vibration, contentDescription = null, tint = Color(0xFFf59e0b), modifier = Modifier.size(20.dp)) // Amber
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Vibrate", color = OrbitTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.weight(1f))
                            Switch(
                                checked = vibrate,
                                onCheckedChange = { vibrate = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = OrbitAccent,
                                    checkedTrackColor = OrbitAccent.copy(alpha = 0.3f),
                                    uncheckedThumbColor = OrbitTextSecondary,
                                    uncheckedTrackColor = OrbitSurfaceVariant
                                ),
                                modifier = Modifier.height(24.dp)
                            )
                        }
                        
                        HorizontalDivider(color = OrbitSurfaceVariant, thickness = 1.dp)

                        // Snooze
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp).clickable { showSnoozeSheet = true },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Outlined.Alarm, contentDescription = null, tint = Color(0xFF10b981), modifier = Modifier.size(20.dp)) // Emerald
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("Snooze", color = OrbitTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.weight(1f))
                            Text("$snoozeTimes times / $snoozeInterval min", color = OrbitTextSecondary, fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(Icons.Filled.ChevronRight, contentDescription = null, tint = OrbitTextSecondary, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
        
        if (showSnoozeSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSnoozeSheet = false },
                containerColor = OrbitSurfaceVariant
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Snooze Settings", color = OrbitTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Interval Picker
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Interval (mins)", color = OrbitTextSecondary, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            val intervals = listOf(3, 5, 10, 15)
                            WheelColumn(
                                items = intervals,
                                selectedIndex = intervals.indexOf(snoozeInterval).coerceAtLeast(0),
                                onItemSelected = { snoozeInterval = it },
                                format = { "$it min" },
                                label = ""
                            )
                        }
                        
                        // Times Picker
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Max Times", color = OrbitTextSecondary, fontSize = 14.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            val times = listOf(1, 2, 3, 4, 5)
                            WheelColumn(
                                items = times,
                                selectedIndex = times.indexOf(snoozeTimes).coerceAtLeast(0),
                                onItemSelected = { snoozeTimes = it },
                                format = { "$it times" },
                                label = ""
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { showSnoozeSheet = false },
                        colors = ButtonDefaults.buttonColors(containerColor = OrbitAccent),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Confirm", color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun WheelTimePicker(
    hour: Int,
    minute: Int,
    onTimeChanged: (Int, Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth().height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        // Highlight Box in the center
        Box(
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(48.dp)
                .background(OrbitSurfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
        )
        
        Text(":", color = OrbitTextSecondary, fontSize = 24.sp, modifier = Modifier.align(Alignment.Center).padding(bottom = 8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hour Wheel
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(200.dp)
                    .background(OrbitSurfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            ) {
                WheelColumn(
                    items = (0..23).toList(),
                    selectedIndex = hour,
                    onItemSelected = { h -> onTimeChanged(h, minute) },
                    format = { String.format("%02d", it) },
                    label = "hr"
                )
            }
            
            Spacer(modifier = Modifier.width(32.dp))
            
            // Minute Wheel
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(200.dp)
                    .background(OrbitSurfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            ) {
                WheelColumn(
                    items = (0..59).toList(),
                    selectedIndex = minute,
                    onItemSelected = { m -> onTimeChanged(hour, m) },
                    format = { String.format("%02d", it) },
                    label = "min"
                )
            }
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun WheelColumn(
    items: List<Int>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    format: (Int) -> String,
    label: String
) {
    val listState = androidx.compose.foundation.lazy.rememberLazyListState(initialFirstVisibleItemIndex = selectedIndex)
    val flingBehavior = androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior(lazyListState = listState)
    
    var centeredIndex by remember { mutableStateOf(selectedIndex + 2) }
    val currentOnItemSelected by androidx.compose.runtime.rememberUpdatedState(onItemSelected)

    LaunchedEffect(selectedIndex) {
        if (centeredIndex - 2 != selectedIndex) {
            listState.scrollToItem(selectedIndex)
        }
    }

    LaunchedEffect(listState) {
        androidx.compose.runtime.snapshotFlow {
            val layoutInfo = listState.layoutInfo
            val viewportCenter = layoutInfo.viewportEndOffset / 2
            layoutInfo.visibleItemsInfo.minByOrNull { 
                kotlin.math.abs((it.offset + it.size / 2) - viewportCenter) 
            }?.index
        }.collect { index ->
            if (index != null) {
                centeredIndex = index
                val actualIndex = index - 2
                if (actualIndex in items.indices) {
                    currentOnItemSelected(items[actualIndex])
                }
            }
        }
    }
    
    val extendedItems = listOf(null, null) + items + listOf(null, null)

    androidx.compose.foundation.lazy.LazyColumn(
        state = listState,
        flingBehavior = flingBehavior,
        modifier = Modifier.width(100.dp).height(200.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(extendedItems.size) { index ->
            val item = extendedItems[index]
            val isSelected = index == centeredIndex
            Box(
                modifier = Modifier.height(40.dp).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (item != null) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = format(item),
                            fontSize = if (isSelected) 36.sp else 24.sp,
                            fontWeight = if (isSelected) FontWeight.Normal else FontWeight.Normal,
                            color = if (isSelected) OrbitAccent else OrbitTextSecondary.copy(alpha = 0.5f)
                        )
                        if (isSelected) {
                            Text(
                                text = label,
                                fontSize = 14.sp,
                                color = OrbitAccent,
                                modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
