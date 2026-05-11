package com.platformdash.settings

import android.content.Context

object ThemePreferences {
    private const val PREFS_NAME = "platform_dash_prefs"
    private const val KEY_THEME_MODE = "theme_mode"

    fun getThemeMode(context: Context): ThemeMode {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val raw = prefs.getString(KEY_THEME_MODE, ThemeMode.LIGHT.name)
        return raw?.let {
            ThemeMode.entries.firstOrNull { mode -> mode.name == it }
        } ?: ThemeMode.LIGHT
    }

    fun setThemeMode(context: Context, mode: ThemeMode) {
        context
            .getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_THEME_MODE, mode.name)
            .apply()
    }
}
