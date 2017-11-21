package com.jay86.minibox

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.jay86.minibox.bean.User

/**
 * Created by Jay68 on 2017/11/20.
 */
class App : Application() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        var user: User? = null

        val isLogin
            get() = user != null
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}