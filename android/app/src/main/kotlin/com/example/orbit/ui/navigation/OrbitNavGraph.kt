package com.example.orbit.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.orbit.ui.screens.analytics.AnalyticsScreen
import com.example.orbit.ui.screens.focus.FocusScreen
import com.example.orbit.ui.screens.settings.SettingsScreen
import com.example.orbit.ui.screens.home.HomeScreen
import com.example.orbit.ui.screens.tasks.TasksScreen
import com.example.orbit.ui.screens.tasks.AddTaskScreen
import com.example.orbit.ui.screens.alarm.AddAlarmScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Home : Screen("home", "Home", Icons.Filled.Alarm)
    object Tasks : Screen("tasks", "Tasks", Icons.Filled.List)
    object Focus : Screen("focus", "Focus", Icons.Filled.Timer)
    object Analytics : Screen("analytics", "Stats", Icons.Filled.Analytics)
    object Settings : Screen("settings", "Settings", Icons.Filled.Settings)
    object AddAlarm : Screen("add_alarm", "Add Alarm", Icons.Filled.Add)
    object AddTask : Screen("add_task", "Add Task", Icons.Filled.Add)
    object FocusSettings : Screen("focus_settings", "Focus Settings", Icons.Filled.Settings)
    object AllowedApps : Screen("allowed_apps", "Allowed Apps", Icons.Filled.List)
}

val items = listOf(
    Screen.Home,
    Screen.Tasks,
    Screen.Focus,
    Screen.Analytics,
    Screen.Settings
)

@Composable
fun OrbitNavGraph() {
    val navController = rememberNavController()
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .background(
                        color = com.example.orbit.ui.theme.OrbitSurface,
                        shape = RoundedCornerShape(32.dp)
                    )
                    .padding(vertical = 12.dp, horizontal = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    items.forEachIndexed { index, screen ->
                        val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true
                        
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .weight(1f)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) {
                                    navController.navigate(screen.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(36.dp)
                                    .width(56.dp)
                                    .then(
                                        if (selected) {
                                            Modifier
                                                .background(com.example.orbit.ui.theme.OrbitAccent.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                                                .border(1.dp, com.example.orbit.ui.theme.OrbitAccent, RoundedCornerShape(20.dp))
                                        } else {
                                            Modifier
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title,
                                    tint = if (selected) com.example.orbit.ui.theme.OrbitAccent else com.example.orbit.ui.theme.OrbitTextSecondary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Text(
                                text = screen.title,
                                color = if (selected) com.example.orbit.ui.theme.OrbitAccent else com.example.orbit.ui.theme.OrbitTextSecondary,
                                fontSize = 12.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                            )
                            
                            if (selected) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(20.dp)
                                        .height(4.dp)
                                        .background(com.example.orbit.ui.theme.OrbitAccent, RoundedCornerShape(2.dp))
                                )
                            } else {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        
                        if (index < items.size - 1) {
                            Box(
                                modifier = Modifier
                                    .height(40.dp)
                                    .width(1.dp)
                                    .background(Color.White.copy(alpha = 0.05f))
                            )
                        }
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(navController, startDestination = Screen.Home.route, Modifier.padding(innerPadding)) {
            composable(Screen.Home.route) { 
                HomeScreen(
                    onNavigateToAddAlarm = { navController.navigate(Screen.AddAlarm.route) },
                    onNavigateToEditAlarm = { alarmId -> navController.navigate(Screen.AddAlarm.route + "?alarmId=$alarmId") }
                ) 
            }
            composable(Screen.Tasks.route) { 
                TasksScreen(
                    onNavigateToAddTask = { navController.navigate(Screen.AddTask.route) },
                    onNavigateToEditTask = { taskId -> navController.navigate(Screen.AddTask.route + "?taskId=$taskId") }
                ) 
            }
            composable(
                route = Screen.AddTask.route + "?taskId={taskId}",
                arguments = listOf(androidx.navigation.navArgument("taskId") {
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                })
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId")
                AddTaskScreen(
                    taskId = taskId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
            composable(Screen.Focus.route) { 
                FocusScreen(
                    onNavigateToSettings = { navController.navigate(Screen.FocusSettings.route) }
                ) 
            }
            composable(Screen.Analytics.route) { AnalyticsScreen() }
            composable(Screen.Settings.route) { 
                SettingsScreen(
                    onNavigateToFocusSettings = { navController.navigate(Screen.FocusSettings.route) }
                ) 
            }
            composable(Screen.FocusSettings.route) {
                com.example.orbit.ui.screens.focus.FocusSettingsScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToAllowedApps = { navController.navigate(Screen.AllowedApps.route) }
                )
            }
            composable(Screen.AllowedApps.route) { 
                com.example.orbit.ui.screens.focus.AllowedAppsScreen(
                    onNavigateBack = { navController.popBackStack() }
                ) 
            }
            composable(
                route = Screen.AddAlarm.route + "?alarmId={alarmId}",
                arguments = listOf(androidx.navigation.navArgument("alarmId") { 
                    type = androidx.navigation.NavType.StringType
                    nullable = true
                })
            ) { backStackEntry ->
                val alarmId = backStackEntry.arguments?.getString("alarmId")
                AddAlarmScreen(
                    alarmId = alarmId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }
    }
}
