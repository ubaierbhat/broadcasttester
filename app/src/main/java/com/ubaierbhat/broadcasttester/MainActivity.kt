package com.ubaierbhat.broadcasttester

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ubaierbhat.broadcasttester.ui.theme.BroadCastTesterTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BroadCastTesterTheme {
                BroadcastTesterScreen(this)
            }
        }
    }
}

@Composable
fun BroadcastTesterScreen(context: Context) {
    var action by remember { mutableStateOf("") }
    var packageName by remember { mutableStateOf("") }
    var receiverName by remember { mutableStateOf("") }

    var extraKey by remember { mutableStateOf("") }
    var extraValue by remember { mutableStateOf("") }
    var selectedExtraType by remember { mutableStateOf(ExtraType.STRING) }
    var extrasList by remember { mutableStateOf(mutableListOf<ExtraData>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        TextField(
            value = action,
            onValueChange = { action = it },
            label = { Text(stringResource(R.string.broadcast_action)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = packageName,
            onValueChange = { packageName = it },
            label = { Text(stringResource(R.string.component_package_name)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = receiverName,
            onValueChange = { receiverName = it },
            label = { Text(stringResource(R.string.component_receiver_name)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = extraKey,
            onValueChange = { extraKey = it },
            label = { Text(stringResource(R.string.extra_key)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = extraValue,
            onValueChange = { extraValue = it },
            label = { Text(stringResource(R.string.extra_value)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(R.string.type))
            DropdownMenu(selectedExtraType, onSelectedType = { selectedExtraType = it })
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (extraKey.isNotBlank() && extraValue.isNotBlank()) {
                extrasList.add(ExtraData(extraKey, extraValue, selectedExtraType))
                extraKey = ""
                extraValue = ""
            }
        }) {
            Text(stringResource(R.string.add_extra))
        }
        Spacer(modifier = Modifier.height(16.dp))

        Column {
            extrasList.forEachIndexed { index, extra ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${extra.key} : ${extra.value} (${extra.type})")
                    IconButton(onClick = {
                        extrasList = extrasList.toMutableList()
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.remove_extra))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            sendBroadcast(context, action, packageName, receiverName, extrasList)
        }) {
            Text(stringResource(R.string.send_broadcast))
        }
    }
}

@Composable
fun DropdownMenu(selectedExtraType: ExtraType, onSelectedType: (ExtraType) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        TextButton(onClick = { expanded = true }) {
            Text(text = selectedExtraType.name)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ExtraType.entries.forEach { type ->
                DropdownMenuItem(
                    onClick = {
                        onSelectedType(type)
                        expanded = false
                    },
                    text = { Text(text = type.name) },
                )
            }
        }
    }
}

enum class ExtraType {
    STRING, LONG, BOOLEAN
}

data class ExtraData(val key: String, val value: String, val type: ExtraType)

fun sendBroadcast(
    context: Context,
    action: String,
    packageName: String,
    receiverName: String,
    extrasList: List<ExtraData>
) {
    val intent = Intent(action).apply {
        component = ComponentName(packageName, receiverName)
        extrasList.forEach { extra ->
            when (extra.type) {
                ExtraType.STRING -> putExtra(extra.key, extra.value)
                ExtraType.LONG -> putExtra(extra.key, extra.value.toLongOrNull() ?: 0L)
                ExtraType.BOOLEAN -> putExtra(extra.key, extra.value.toBoolean())
            }
        }
    }
    context.sendBroadcast(intent)
}