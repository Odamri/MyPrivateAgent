package com.myprivateagent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myprivateagent.data.AppSettings
import com.myprivateagent.net.ApiFactory
import com.myprivateagent.net.DecideRequest
import kotlinx.coroutines.launch

@Composable
fun ApprovalsScreen(
    settings: AppSettings,
    baseUrl: String
) {
    if (baseUrl.isBlank()) {
        Text("חסר Base URL. כנס להגדרות.")
        return
    }

    val api = remember(baseUrl) { ApiFactory.create(baseUrl) }
    val scope = rememberCoroutineScope()

    var pending by remember { mutableStateOf(listOf<List<Any>>()) }
    var status by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var storedPin by remember { mutableStateOf("") }
    var pinEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        settings.pinFlow.collect { storedPin = it }
    }
    LaunchedEffect(Unit) {
        settings.pinEnabledFlow.collect { pinEnabled = it }
    }

    fun refresh() {
        scope.launch {
            status = "טוען..."
            try {
                val res = api.approvals()
                // expected: {"pending": [[id, created_at, status, title, risk_level, action_type], ...]}
                val p = res["pending"] as? List<*> ?: emptyList<Any>()
                pending = p.mapNotNull { it as? List<Any> }
                status = "נמצאו ${pending.size} בקשות"
            } catch (e: Exception) {
                status = "שגיאה: ${e.message}"
            }
        }
    }

    LaunchedEffect(Unit) { refresh() }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("אישורים ממתינים", style = MaterialTheme.typography.titleMedium)
            TextButton(onClick = { refresh() }) { Text("רענן") }
        }
        Spacer(Modifier.height(6.dp))
        Text(status, style = MaterialTheme.typography.labelSmall)
        Spacer(Modifier.height(10.dp))

        if (pinEnabled) {
            OutlinedTextField(
                value = pin,
                onValueChange = { pin = it },
                label = { Text("PIN לאישור") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(10.dp))
        }

        LazyColumn(Modifier.weight(1f)) {
            items(pending) { row ->
                val id = (row.getOrNull(0) as? Number)?.toInt() ?: -1
                val title = row.getOrNull(3)?.toString() ?: ""
                val risk = row.getOrNull(4)?.toString() ?: ""
                val type = row.getOrNull(5)?.toString() ?: ""

                Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("#$id — $title", style = MaterialTheme.typography.titleSmall)
                        Text("סיכון: $risk | סוג: $type", style = MaterialTheme.typography.labelSmall)
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Button(
                                onClick = {
                                    if (pinEnabled && storedPin.isNotBlank() && pin != storedPin) {
                                        status = "PIN שגוי"
                                        return@Button
                                    }
                                    scope.launch {
                                        try {
                                            api.decide(id, DecideRequest(true, "Approved from APK"))
                                            status = "אושר #$id"
                                            refresh()
                                        } catch (e: Exception) {
                                            status = "שגיאה: ${e.message}"
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) { Text("אשר") }

                            OutlinedButton(
                                onClick = {
                                    if (pinEnabled && storedPin.isNotBlank() && pin != storedPin) {
                                        status = "PIN שגוי"
                                        return@OutlinedButton
                                    }
                                    scope.launch {
                                        try {
                                            api.decide(id, DecideRequest(false, "Rejected from APK"))
                                            status = "נדחה #$id"
                                            refresh()
                                        } catch (e: Exception) {
                                            status = "שגיאה: ${e.message}"
                                        }
                                    }
                                },
                                modifier = Modifier.weight(1f)
                            ) { Text("דחה") }
                        }
                    }
                }
            }
        }
    }
}
