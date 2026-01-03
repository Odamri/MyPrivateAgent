package com.myprivateagent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myprivateagent.data.AppSettings
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    settings: AppSettings,
    initialBaseUrl: String,
    onBaseUrlSaved: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var baseUrl by remember { mutableStateOf(initialBaseUrl) }
    var pin by remember { mutableStateOf("") }
    var pinEnabled by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        settings.pinEnabledFlow.collect { pinEnabled = it }
    }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("הגדרות", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = baseUrl,
            onValueChange = { baseUrl = it },
            label = { Text("Base URL של Cloud Run") },
            placeholder = { Text("https://agent-api-xxxxx.run.app") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                scope.launch {
                    settings.setBaseUrl(baseUrl)
                    onBaseUrlSaved(baseUrl.trim().removeSuffix("/"))
                    status = "נשמר Base URL"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("שמור") }

        Spacer(Modifier.height(18.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("PIN לאישורים", style = MaterialTheme.typography.titleSmall)
            Switch(
                checked = pinEnabled,
                onCheckedChange = { enabled ->
                    scope.launch { settings.setPinEnabled(enabled) }
                }
            )
        }

        if (pinEnabled) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = pin,
                onValueChange = { pin = it },
                label = { Text("הגדר PIN (ספרות/טקסט)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            Button(
                onClick = {
                    scope.launch {
                        settings.setPin(pin)
                        status = "נשמר PIN"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("שמור PIN") }
        }

        Spacer(Modifier.height(12.dp))
        Text(status, style = MaterialTheme.typography.labelSmall)
    }
}
