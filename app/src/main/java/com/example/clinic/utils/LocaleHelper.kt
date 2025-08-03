package com.example.clinic.utils

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.preference.PreferenceManager
import java.util.Locale


object LocaleHelper {
    private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"

    fun onAttach(context: Context): Context {
        val lang = getPersistedData(context, Locale.getDefault().getLanguage())
        return setLocale(context, lang)
    }

    fun onAttach(context: Context, defaultLanguage: String?): Context {
        val lang = getPersistedData(context, defaultLanguage)
        return setLocale(context, lang)
    }

    fun getLanguage(context: Context?): String? {
        return getPersistedData(context, Locale.getDefault().getLanguage())
    }

    fun setLocale(context: Context, language: String?): Context {
        persist(context, language)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return updateResources(context, language)
        }

        return updateResourcesLegacy(context, language)
    }

    private fun getPersistedData(context: Context?, defaultLanguage: String?): String? {
        return PreferenceManager.getDefaultSharedPreferences(context)
            .getString(SELECTED_LANGUAGE, defaultLanguage)
    }

    private fun persist(context: Context?, language: String?) {
        PreferenceManager.getDefaultSharedPreferences(context).edit()
            .putString(SELECTED_LANGUAGE, language).apply()
    }

    private fun updateResources(context: Context, language: String?): Context {
        val locale: Locale = Locale(language)
        Locale.setDefault(locale)

        val configuration: Configuration = context.getResources().getConfiguration()
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale) // Important for RTL languages

        return context.createConfigurationContext(configuration)
    }

    @Suppress("deprecation")
    private fun updateResourcesLegacy(context: Context, language: String?): Context {
        val locale: Locale = Locale(language)
        Locale.setDefault(locale)

        val resources: Resources = context.getResources()
        val configuration: Configuration = resources.getConfiguration()
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale) // Important for RTL languages
        }
        resources.updateConfiguration(configuration, resources.getDisplayMetrics())
        return context
    }
}