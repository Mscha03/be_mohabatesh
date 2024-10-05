package com.example.myapplication.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

fun getLanguageCode(language: String): String {
    return when(language){
        "فارسی" -> "fa"
        "English" -> "En"
        else -> {
            "en"
        }
    }
}

fun setLocale(context: Context, language: String) {
    val locale = Locale(language)
    Locale.setDefault(locale)

    val config = Configuration(context.resources.configuration)
    config.setLocale(locale)

    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(language))
}

fun saveLanguageToPreferences(context: Context, language: String) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("app_language", language)
    editor.apply()
}

fun getLanguageFromPreferences(context: Context): String? {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getString("app_language", "en")
}