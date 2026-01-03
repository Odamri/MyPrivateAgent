package com.myprivateagent.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.*
import com.myprivateagent.data.AppSettings
import com.myprivateagent.ui.screens.*

@Composable
fun AgentApp(
    settings: AppSettings,
    initialBaseUrl: String,
    pinEnabled: Boolean
) {
    var baseUrl by remember { mutableStateOf(initialBaseUrl) }
    val navController = rememberNavController()

    MaterialTheme {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("MyPrivateAgent") }
                )
            },
            bottomBar = {
                NavigationBar {
                    val route = navController.currentBackStackEntryAsState().value?.destination?.route
                    NavigationBarItem(
                        selected = route == "chat",
                        onClick = { navController.navigate("chat") { launchSingleTop = true } },
                        label = { Text("צ'אט") },
                        icon = { }
                    )
                    NavigationBarItem(
                        selected = route == "approvals",
                        onClick = { navController.navigate("approvals") { launchSingleTop = true } },
                        label = { Text("אישורים") },
                        icon = { }
                    )
                    NavigationBarItem(
                        selected = route == "daily",
                        onClick = { navController.navigate("daily") { launchSingleTop = true } },
                        label = { Text("יומי") },
                        icon = { }
                    )
                    NavigationBarItem(
                        selected = route == "settings",
                        onClick = { navController.navigate("settings") { launchSingleTop = true } },
                        label = { Text("הגדרות") },
                        icon = { }
                    )
                }
            }
        ) { padding ->
            NavHost(
                navController = navController,
                startDestination = if (baseUrl.isBlank()) "settings" else "chat",
                modifier = Modifier.padding(padding)
            ) {
                composable("chat") {
                    ChatScreen(settings = settings, baseUrlState = baseUrl) { baseUrl = it }
                }
                composable("approvals") {
                    ApprovalsScreen(settings = settings, baseUrl = baseUrl)
                }
                composable("daily") {
                    DailyScreen(settings = settings, baseUrl = baseUrl)
                }
                composable("settings") {
                    SettingsScreen(
                        settings = settings,
                        initialBaseUrl = baseUrl,
                        onBaseUrlSaved = { baseUrl = it }
                    )
                }
            }
        }
    }
}
