package com.jay86.minibox

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * Created by Jay68 on 2017/11/20.
 */
class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}