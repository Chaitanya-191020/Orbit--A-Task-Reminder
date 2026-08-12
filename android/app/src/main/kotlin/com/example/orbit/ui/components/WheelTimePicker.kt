package com.example.orbit.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.orbit.ui.theme.OrbitAccent
import com.example.orbit.ui.theme.OrbitTextPrimary
import com.example.orbit.ui.theme.OrbitTextSecondary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WheelTimePicker(
    modifier: Modifier = Modifier,
    initialHour: Int = 8,
    initialMinute: Int = 0,
    onTimeSelected: (Int, Int) -> Unit // Returns 24-hour time
) {
    val hours = (0..23).toList()
    val minutes = (0..59).toList()
    
    val bufferCount = 2 
    
    val hourState = rememberLazyListState(initialFirstVisibleItemIndex = initialHour)
    val minuteState = rememberLazyListState(initialFirstVisibleItemIndex = initialMinute)
    
    val itemHeight = 100.dp

    LaunchedEffect(hourState.firstVisibleItemIndex, minuteState.firstVisibleItemIndex) {
        val selectedHour = hours.getOrNull(hourState.firstVisibleItemIndex) ?: 0
        val selectedMinute = minutes.getOrNull(minuteState.firstVisibleItemIndex) ?: 0
        onTimeSelected(selectedHour, selectedMinute)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight * 5)
            .background(Color.Transparent),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Hours Wheel
        LazyColumn(
            state = hourState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = hourState),
            modifier = Modifier
                .width(120.dp)
                .height(itemHeight * 5),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(bufferCount) { Spacer(modifier = Modifier.height(itemHeight)) }
            items(hours.size) { index ->
                val isSelected = index == hourState.firstVisibleItemIndex
                val alpha = if (isSelected) 1f else 0.2f
                Box(
                    modifier = Modifier.height(itemHeight).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d", hours[index]),
                        fontSize = if (isSelected) 96.sp else 80.sp,
                        fontWeight = FontWeight.Light,
                        color = if (isSelected) OrbitTextPrimary else OrbitTextSecondary,
                        modifier = Modifier.alpha(alpha),
                        letterSpacing = (-2).sp
                    )
                }
            }
            items(bufferCount) { Spacer(modifier = Modifier.height(itemHeight)) }
        }

        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = ":",
            fontSize = 80.sp,
            fontWeight = FontWeight.Light,
            color = OrbitTextPrimary,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Spacer(modifier = Modifier.width(8.dp))

        // Minutes Wheel
        LazyColumn(
            state = minuteState,
            flingBehavior = rememberSnapFlingBehavior(lazyListState = minuteState),
            modifier = Modifier
                .width(120.dp)
                .height(itemHeight * 5),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(bufferCount) { Spacer(modifier = Modifier.height(itemHeight)) }
            items(minutes.size) { index ->
                val isSelected = index == minuteState.firstVisibleItemIndex
                val alpha = if (isSelected) 1f else 0.2f
                Box(
                    modifier = Modifier.height(itemHeight).fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = String.format("%02d", minutes[index]),
                        fontSize = if (isSelected) 96.sp else 80.sp,
                        fontWeight = FontWeight.Light,
                        color = if (isSelected) OrbitTextPrimary else OrbitTextSecondary,
                        modifier = Modifier.alpha(alpha),
                        letterSpacing = (-2).sp
                    )
                }
            }
            items(bufferCount) { Spacer(modifier = Modifier.height(itemHeight)) }
        }
    }
}
