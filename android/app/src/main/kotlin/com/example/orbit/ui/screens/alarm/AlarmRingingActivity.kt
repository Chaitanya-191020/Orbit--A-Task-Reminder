package com.example.orbit.ui.screens.alarm

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.orbit.ui.theme.OrbitAccent
import com.example.orbit.ui.theme.OrbitAccentSecondary
import com.example.orbit.ui.theme.OrbitSurface
import com.example.orbit.ui.theme.OrbitSurfaceVariant
import com.example.orbit.ui.theme.OrbitTextPrimary
import com.example.orbit.ui.theme.OrbitTextSecondary
import com.example.orbit.ui.theme.OrbitTheme
import com.example.orbit.ui.theme.OrbitWarning
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AlarmRingingActivity : ComponentActivity() {

    private var currentAlarmId: String = "Unknown"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setShowWhenLocked(true)
        setTurnScreenOn(true)
        
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        val filter = android.content.IntentFilter("ACTION_CLOSE_ALARM_ACTIVITY")
        val receiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(context: android.content.Context?, intent: android.content.Intent?) {
                finish()
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(receiver, filter, RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(receiver, filter)
        }

        lifecycle.addObserver(object : androidx.lifecycle.DefaultLifecycleObserver {
            override fun onDestroy(owner: androidx.lifecycle.LifecycleOwner) {
                unregisterReceiver(receiver)
            }
        })

        currentAlarmId = intent.getStringExtra("ALARM_ID") ?: "Unknown"
        val alarmLabel = intent.getStringExtra("LABEL")?.uppercase() ?: "ALARM"
        val snoozeLimitReached = intent.getBooleanExtra("SNOOZE_LIMIT_REACHED", false)
        val snoozeDuration = intent.getIntExtra("SNOOZE_DURATION", 9)

        setContent {
            OrbitTheme {
                var currentTime by remember { mutableStateOf(SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())) }
                
                LaunchedEffect(Unit) {
                    while(true) {
                        currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        kotlinx.coroutines.delay(1000)
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF030712)) // Deep dark background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(100.dp))
                        
                        // Header
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Alarm, contentDescription = null, tint = OrbitAccent.copy(alpha = 0.5f), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(alarmLabel, color = OrbitAccent.copy(alpha = 0.5f), fontSize = 14.sp, fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Huge Glow Clock
                        Text(
                            text = currentTime,
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Black,
                            color = OrbitAccent, // Blue glow like image
                            modifier = Modifier.shadow(
                                elevation = 40.dp,
                                shape = RoundedCornerShape(16.dp),
                                spotColor = OrbitAccent,
                                ambientColor = OrbitAccent
                            )
                        )

                        Spacer(modifier = Modifier.height(48.dp))
                        
                        // Mock Sound Wave
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.height(40.dp)
                        ) {
                            val heights = listOf(10.dp, 20.dp, 15.dp, 30.dp, 40.dp, 30.dp, 15.dp, 20.dp, 10.dp)
                            heights.forEach { h ->
                                Box(modifier = Modifier.width(6.dp).height(h).background(OrbitAccent.copy(alpha = 0.5f), RoundedCornerShape(50)))
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        Text("Reading Morning Briefing...", color = Color.Gray, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        
                        Spacer(modifier = Modifier.weight(1f))
                        
                        // Swipe Component
                        SwipeToActComponent(
                            onDismiss = {
                                val serviceIntent = Intent(this@AlarmRingingActivity, com.example.orbit.alarms.AlarmService::class.java).apply {
                                    action = com.example.orbit.alarms.AlarmService.ACTION_DISMISS
                                    putExtra(com.example.orbit.alarms.AlarmService.EXTRA_ALARM_ID, currentAlarmId)
                                }
                                startService(serviceIntent)
                                finish()
                            },
                            onSnooze = {
                                val serviceIntent = Intent(this@AlarmRingingActivity, com.example.orbit.alarms.AlarmService::class.java).apply {
                                    action = com.example.orbit.alarms.AlarmService.ACTION_SNOOZE
                                    putExtra(com.example.orbit.alarms.AlarmService.EXTRA_ALARM_ID, currentAlarmId)
                                }
                                startService(serviceIntent)
                                finish()
                            },
                            snoozeLimitReached = snoozeLimitReached,
                            snoozeDuration = snoozeDuration
                        )
                        
                        Spacer(modifier = Modifier.height(48.dp))
                    }
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            val serviceIntent = Intent(this, com.example.orbit.alarms.AlarmService::class.java).apply {
                action = com.example.orbit.alarms.AlarmService.ACTION_SILENCE
                putExtra(com.example.orbit.alarms.AlarmService.EXTRA_ALARM_ID, currentAlarmId)
            }
            startService(serviceIntent)
            // Do not finish() so screen stays active
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}

@Composable
fun SwipeToActComponent(
    onDismiss: () -> Unit,
    onSnooze: () -> Unit,
    snoozeLimitReached: Boolean,
    snoozeDuration: Int
) {
    val maxDrag = 180f // pixels to trigger action
    var offsetY by remember { mutableStateOf(0f) }
    val animatedOffsetY by androidx.compose.animation.core.animateFloatAsState(targetValue = offsetY)

    val progress = (animatedOffsetY / maxDrag).coerceIn(-1f, 1f)
    
    // Up swipe (Dismiss): progress is negative (0 to -1)
    val dismissScale = 1f + (if (progress < 0) -progress * 0.2f else 0f)
    val dismissAlpha = if (progress > 0) 1f - progress else 1f
    
    // Down swipe (Snooze): progress is positive (0 to 1)
    val snoozeScale = 1f + (if (progress > 0) progress * 0.2f else 0f)
    val snoozeAlpha = if (progress < 0) 1f + progress else 1f

    Box(
        modifier = Modifier
            .size(300.dp)
            .border(1.dp, OrbitAccent.copy(alpha = 0.3f), androidx.compose.foundation.shape.CircleShape),
        contentAlignment = Alignment.Center
    ) {
        // Text labels inside the circle
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section (Dismiss)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 32.dp).graphicsLayer {
                    scaleX = dismissScale
                    scaleY = dismissScale
                    alpha = dismissAlpha
                }
            ) {
                Icon(androidx.compose.material.icons.Icons.Filled.KeyboardArrowUp, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(32.dp))
                Text("Swipe up to", color = com.example.orbit.ui.theme.OrbitTextSecondary, fontSize = 12.sp)
                Text("DISMISS", color = Color(0xFF10B981), fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }

            // Bottom Section (Snooze)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp).graphicsLayer {
                    scaleX = snoozeScale
                    scaleY = snoozeScale
                    alpha = snoozeAlpha
                }
            ) {
                if (!snoozeLimitReached) {
                    Text("Swipe down to", color = com.example.orbit.ui.theme.OrbitTextSecondary, fontSize = 12.sp)
                    Text("SNOOZE", color = Color(0xFFF59E0B), fontSize = 16.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text("( $snoozeDuration min )", color = Color(0xFFF59E0B).copy(alpha = 0.7f), fontSize = 12.sp)
                    Icon(androidx.compose.material.icons.Icons.Filled.KeyboardArrowDown, contentDescription = null, tint = Color(0xFFF59E0B), modifier = Modifier.size(32.dp))
                } else {
                    Text("SNOOZE LIMIT REACHED", color = com.example.orbit.ui.theme.OrbitTextSecondary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Draggable Dot
        Box(
            modifier = Modifier
                .offset { androidx.compose.ui.unit.IntOffset(0, animatedOffsetY.toInt()) }
                .size(24.dp) // Small dot based on the image
                .shadow(8.dp, androidx.compose.foundation.shape.CircleShape)
                .background(OrbitAccent, androidx.compose.foundation.shape.CircleShape)
                .pointerInput(Unit) {
                    detectVerticalDragGestures(
                        onDragEnd = {
                            if (offsetY < -maxDrag) {
                                onDismiss()
                            } else if (offsetY > maxDrag && !snoozeLimitReached) {
                                onSnooze()
                            }
                            offsetY = 0f
                        },
                        onVerticalDrag = { change, dragAmount ->
                            change.consume()
                            val newY = offsetY + dragAmount
                            // constrain dragging
                            if (newY < 0 || (!snoozeLimitReached)) {
                                offsetY = newY.coerceIn(-maxDrag - 40f, if (snoozeLimitReached) 0f else maxDrag + 40f)
                            }
                        }
                    )
                }
        )
    }
}
