package com.platformdash.settings

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val GbrNavy = Color(0xFF071D49)
private val GbrRed = Color(0xFFE60000)
private val GbrYellow = Color(0xFFFFD100)
private val GbrWhite = Color(0xFFFFFFFF)
private val AppBorderWidth = 3.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsHostScreen(
    themeMode: ThemeMode,
    onThemeModeChange: (ThemeMode) -> Unit,
) {
    var origin by rememberSaveable { mutableStateOf("LBG") }
    var destination by rememberSaveable { mutableStateOf("RDH") }
    var apiKey by rememberSaveable { mutableStateOf("") }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = { Text("Commute Settings") },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .border(AppBorderWidth, GbrYellow)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Configure your route and BYOK key. Default route is LBG to RDH.",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Theme",
                style = MaterialTheme.typography.titleMedium
            )

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ThemeModeButton(
                    label = "Light",
                    selected = themeMode == ThemeMode.LIGHT,
                    buttonColor = GbrNavy,
                    onClick = { onThemeModeChange(ThemeMode.LIGHT) },
                )
                ThemeModeButton(
                    label = "Dark",
                    selected = themeMode == ThemeMode.DARK,
                    buttonColor = GbrRed,
                    onClick = { onThemeModeChange(ThemeMode.DARK) },
                )
            }

            OutlinedTextField(
                value = origin,
                onValueChange = { origin = it.uppercase() },
                label = { Text("Origin station code") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GbrYellow,
                    unfocusedBorderColor = GbrYellow,
                ),
            )

            OutlinedTextField(
                value = destination,
                onValueChange = { destination = it.uppercase() },
                label = { Text("Destination station code") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GbrYellow,
                    unfocusedBorderColor = GbrYellow,
                ),
            )

            OutlinedTextField(
                value = apiKey,
                onValueChange = { apiKey = it.trim() },
                label = { Text("API key (BYOK)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GbrYellow,
                    unfocusedBorderColor = GbrYellow,
                ),
            )

            Button(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(AppBorderWidth, GbrYellow),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GbrRed,
                    contentColor = GbrWhite,
                ),
            ) {
                Text("Save settings")
            }
        }
    }
}

@Composable
private fun ThemeModeButton(
    label: String,
    selected: Boolean,
    buttonColor: Color,
    onClick: () -> Unit,
) {
    if (selected) {
        Button(
            onClick = onClick,
            modifier = Modifier.border(AppBorderWidth, GbrYellow),
            colors = ButtonDefaults.buttonColors(
                containerColor = buttonColor,
                contentColor = GbrWhite,
            ),
        ) {
            Text(label)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.border(AppBorderWidth, GbrYellow),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true).copy(width = AppBorderWidth),
        ) {
            Text(label)
        }
    }
}