package com.example.orbit.ui.screens.focus

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.addCallback
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adjust
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Security
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.draw.clip
import com.example.orbit.ui.theme.OrbitAccent
import com.example.orbit.data.local.FocusPreferencesManager
import com.example.orbit.ui.components.OrbitGradientBackground
import com.example.orbit.ui.theme.OrbitTextPrimary
import com.example.orbit.ui.theme.OrbitTextSecondary
import com.example.orbit.ui.theme.OrbitTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class FocusLockActivity : ComponentActivity() {

    @Inject
    lateinit var focusPrefs: FocusPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        onBackPressedDispatcher.addCallback(this) {
            // Do nothing, effectively disabling the back button
        }
        
        val endTimeMillis = intent.getLongExtra("END_TIME_MILLIS", System.currentTimeMillis() + 60000)
        val allowedApps = focusPrefs.getAllowedApps()
        focusPrefs.setFocusActive(true)

        setContent {
            OrbitTheme {
                FocusLockScreen(
                    endTimeMillis = endTimeMillis,
                    allowedApps = allowedApps
                ) {
                    focusPrefs.setFocusActive(false)
                    finish()
                }
            }
        }
    }
}

@Composable
fun FocusLockScreen(endTimeMillis: Long, allowedApps: Set<String>, onFinish: () -> Unit) {
    val initialTimeLeft = remember { (endTimeMillis - System.currentTimeMillis()).coerceAtLeast(1) }
    var timeLeft by remember { mutableStateOf(initialTimeLeft) }
    val context = androidx.compose.ui.platform.LocalContext.current
    val pm = context.packageManager
    
    val appList = remember(allowedApps) {
        allowedApps.mapNotNull { packageName ->
            try {
                val appInfo = pm.getApplicationInfo(packageName, 0)
                val label = pm.getApplicationLabel(appInfo).toString()
                Pair(packageName, label)
            } catch (e: Exception) {
                null
            }
        }.sortedBy { it.second }
    }

    LaunchedEffect(Unit) {
        while (timeLeft > 0) {
            delay(1000)
            timeLeft = endTimeMillis - System.currentTimeMillis()
        }
        onFinish()
    }

    val hours = TimeUnit.MILLISECONDS.toHours(timeLeft.coerceAtLeast(0))
    val minutes = TimeUnit.MILLISECONDS.toMinutes(timeLeft.coerceAtLeast(0)) % 60
    val seconds = TimeUnit.MILLISECONDS.toSeconds(timeLeft.coerceAtLeast(0)) % 60
    
    val timeString = if (hours > 0) {
        String.format(java.util.Locale.US, "%02d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format(java.util.Locale.US, "%02d:%02d", minutes, seconds)
    }
    
    val targetProgress = (timeLeft.toFloat() / initialTimeLeft.toFloat()).coerceIn(0f, 1f)
    val progress by androidx.compose.animation.core.animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = androidx.compose.animation.core.tween(durationMillis = 1000, easing = androidx.compose.animation.core.LinearEasing),
        label = "progress"
    )

    OrbitGradientBackground {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FOCUS MODE",
                    color = OrbitTextSecondary,
                    letterSpacing = 2.sp,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Timer
            Box(
                modifier = Modifier.size(320.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 3.dp.toPx()
                    val radius = (size.minDimension - strokeWidth * 4) / 2
                    val center = Offset(size.width / 2, size.height / 2)
                    
                    // Background track
                    drawArc(
                        color = Color(0xFF161B22),
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    
                    // Progress track
                    val sweep = 360f * progress
                    
                    // Outer Glow
                    drawArc(
                        color = OrbitAccent.copy(alpha = 0.2f),
                        startAngle = -90f,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                    )
                    
                    // Inner bright track
                    drawArc(
                        color = OrbitAccent,
                        startAngle = -90f,
                        sweepAngle = sweep,
                        useCenter = false,
                        topLeft = Offset(center.x - radius, center.y - radius),
                        size = Size(radius * 2, radius * 2),
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                    )
                    
                    // Thumb
                    val angle = -90f + sweep
                    val angleRad = Math.toRadians(angle.toDouble())
                    val thumbX = center.x + radius * Math.cos(angleRad).toFloat()
                    val thumbY = center.y + radius * Math.sin(angleRad).toFloat()
                    
                    drawCircle(
                        color = OrbitAccent,
                        radius = 5.dp.toPx(),
                        center = Offset(thumbX, thumbY),
                        style = Fill
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 2.dp.toPx(),
                        center = Offset(thumbX, thumbY),
                        style = Fill
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Adjust,
                        contentDescription = null,
                        tint = OrbitAccent,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "FOCUS MODE ACTIVE",
                        color = OrbitTextSecondary,
                        letterSpacing = 1.sp,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = timeString,
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Light,
                        color = Color.White
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(48.dp),
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Text(if (hours > 0) "HR" else "MIN", color = OrbitTextSecondary, fontSize = 12.sp, letterSpacing = 1.sp)
                        Text(if (hours > 0) "MIN" else "SEC", color = OrbitTextSecondary, fontSize = 12.sp, letterSpacing = 1.sp)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Info Card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .background(Color(0xFF161B22), RoundedCornerShape(24.dp))
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Security,
                    contentDescription = null,
                    tint = OrbitAccent,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = "You can only use\nallowed apps.",
                    color = OrbitTextSecondary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Allowed Apps List
            if (appList.isNotEmpty()) {
                androidx.compose.foundation.lazy.LazyColumn(
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp)
                ) {
                    items(appList.size) { index ->
                        val (packageName, appName) = appList[index]
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFF161B22), RoundedCornerShape(20.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(20.dp))
                                .clip(RoundedCornerShape(20.dp))
                                .clickable {
                                    val launchIntent = pm.getLaunchIntentForPackage(packageName)
                                    if (launchIntent != null) {
                                        context.startActivity(launchIntent)
                                    } else {
                                        android.widget.Toast.makeText(context, "Cannot launch $appName", android.widget.Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .padding(horizontal = 20.dp, vertical = 16.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.Apps,
                                    contentDescription = null,
                                    tint = OrbitAccent,
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = appName.uppercase(),
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = packageName,
                                        color = OrbitTextSecondary,
                                        fontSize = 12.sp
                                    )
                                }
                                Icon(
                                    imageVector = Icons.Default.ChevronRight,
                                    contentDescription = null,
                                    tint = OrbitAccent
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
