package com.myprivateagent.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.myprivateagent.data.AppSettings
import com.myprivateagent.net.ApiFactory
import com.myprivateagent.net.ChatRequest
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    settings: AppSettings,
    baseUrlState: String,
    onBaseUrlSaved: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    var input by remember { mutableStateOf(TextFieldValue("")) }
    var log by remember { mutableStateOf("היי! כתוב לי הודעה.\n") }
    var loading by remember { mutableStateOf(false) }

    if (baseUrlState.isBlank()) {
        Text("חסר Base URL. כנס להגדרות.")
        return
    }

    val api = remember(baseUrlState) { ApiFactory.create(baseUrlState) }

    Column(Modifier.fillMaxSize().padding(12.dp)) {
        Text("Server: $baseUrlState", style = MaterialTheme.typography.labelSmall)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = log,
            onValueChange = {},
            modifier = Modifier.weight(1f).fillMaxWidth(),
            readOnly = true,
            label = { Text("שיחה") }
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("הודעה") }
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                val msg = input.text.trim()
                if (msg.isEmpty() || loading) return@Button
                loading = true
                log += "\nאתה: $msg\n"
                input = TextFieldValue("")
                scope.launch {
                    try {
                        val res = api.chat(ChatRequest(msg))
                        log += "עוזר: ${res.reply}\n"
                        if (res.approval_id != null) {
                            log += "⚠️ נוצרה בקשת אישור: #${res.approval_id}\n"
                        }
                    } catch (e: Exception) {
                        log += "שגיאה: ${e.message}\n"
                    } finally {
                        loading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (loading) "שולח..." else "שלח")
        }
    }
}
