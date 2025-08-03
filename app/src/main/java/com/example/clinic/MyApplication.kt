package com.example.clinic

import android.app.Application
import android.content.Context
import com.example.clinic.utils.LocaleHelper

class MyApplication : Application() {
    protected override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.setLocale(base, "en"))
    }
}