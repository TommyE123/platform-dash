package com.platformdash

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.glance.appwidget.updateAll
import androidx.lifecycle.lifecycleScope
import com.platformdash.settings.SettingsHostScreen
import com.platformdash.settings.AppTheme
import com.platformdash.settings.ThemeMode
import com.platformdash.settings.ThemePreferences
import com.platformdash.widget.CommuteWidget
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var themeMode by remember {
                mutableStateOf(ThemePreferences.getThemeMode(this@MainActivity))
            }

            AppTheme(themeMode = themeMode) {
                SettingsHostScreen(
                    themeMode = themeMode,
                    onThemeModeChange = { newMode: ThemeMode ->
                        themeMode = newMode
                        ThemePreferences.setThemeMode(this@MainActivity, newMode)
                        lifecycleScope.launch {
                            CommuteWidget().updateAll(this@MainActivity)
                        }
                    },
                )
            }
        }
    }
}