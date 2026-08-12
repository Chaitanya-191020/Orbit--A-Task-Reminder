package com.example.orbit.ui.screens.focus

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.orbit.ui.components.OrbitGradientBackground
import com.example.orbit.ui.theme.OrbitAccent
import com.example.orbit.ui.theme.OrbitTextPrimary
import com.example.orbit.ui.theme.OrbitTextSecondary
import com.example.orbit.ui.viewmodels.AllowedAppsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllowedAppsScreen(
    onNavigateBack: () -> Unit,
    viewModel: AllowedAppsViewModel = hiltViewModel()
) {
    val installedApps by viewModel.installedApps.collectAsState()
    val allowedApps by viewModel.allowedApps.collectAsState()
    val context = LocalContext.current

    OrbitGradientBackground {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Allowed Apps", color = OrbitTextPrimary) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = OrbitTextPrimary)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        "Select apps you can use during Focus Mode.",
                        color = OrbitTextSecondary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }
                
                items(installedApps) { app ->
                    val isAllowed = allowedApps.contains(app.packageName)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (!viewModel.toggleAppAllowed(app.packageName)) {
                                    Toast.makeText(context, "Maximum 3 apps allowed", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(app.appName, color = OrbitTextPrimary, style = MaterialTheme.typography.bodyLarge)
                            Text(app.packageName, color = OrbitTextSecondary, style = MaterialTheme.typography.bodySmall)
                        }
                        Switch(
                            checked = isAllowed,
                            onCheckedChange = { 
                                if (!viewModel.toggleAppAllowed(app.packageName)) {
                                    Toast.makeText(context, "Maximum 3 apps allowed", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = OrbitAccent, checkedTrackColor = OrbitAccent.copy(alpha = 0.5f))
                        )
                    }
                    Divider(color = Color.LightGray.copy(alpha = 0.2f))
                }
            }
        }
    }
}
