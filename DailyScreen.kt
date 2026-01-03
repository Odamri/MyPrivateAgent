package com.myprivateagent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.myprivateagent.data.AppSettings
import com.myprivateagent.net.ApiFactory
import kotlinx.coroutines.launch

@Composable
fun DailyScreen(
    settings: AppSettings,
    baseUrl: String
) {
    if (baseUrl.isBlank()) {
        Text("חסר Base URL. כנס להגדרות.")
        return
    }

    val api = remember(baseUrl) { ApiFactory.create(baseUrl) }
    val scope = rememberCoroutineScope()
    var output by remember { mutableStateOf("כאן תוכל להפעיל ריצה יומית ידנית (בנוסף ל-Scheduler בענן).\n") }
    var loading by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("KPI יומי", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = output,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.weight(1f).fillMaxWidth(),
            label = { Text("תוצאה") }
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                if (loading) return@Button
                loading = true
                scope.launch {
                    try {
                        val res = api.runDaily()
                        output += "\n---\n$res\n"
                    } catch (e: Exception) {
                        output += "\nשגיאה: ${e.message}\n"
                    } finally {
                        loading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (loading) "מריץ..." else "הפעל ריצה יומית עכשיו")
        }
    }
}
