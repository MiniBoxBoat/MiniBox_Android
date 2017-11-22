package com.jay86.minibox.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.WindowManager
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.user.LoginActivity
import com.jay86.minibox.utils.LogUtils
import org.jetbrains.anko.startActivity

/**
 * Created by Jay on 2017/10/10.
 */

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {
    companion object {
        fun activityStart(context: Context, finishBefore: Boolean = true) {
            context.startActivity<LoginActivity>()
            if (finishBefore && context is Activity) context.finish()
        }
    }

    open protected val title: String
        get() = resources.getString(R.string.app_name)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.addActivity(this)

        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            //window.navigationBarColor = Color.TRANSPARENT
            window.statusBarColor = Color.TRANSPARENT
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
        LogUtils.v("activity_name", javaClass.name)
    }

    fun Toolbar.init(listener: View.OnClickListener?) {
        setSupportActionBar(this)
        this@BaseActivity.setTitle(title)
        setNavigationOnClickListener(listener)
    }

    fun setFullScreen() {
        val decorView = window.decorView
        val uiOptions = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        decorView.systemUiVisibility = uiOptions
    }

    inline fun <reified T : Activity> Activity.activityStart(finishBefore: Boolean = true) {
        startActivity<T>()
        if (finishBefore) finish()
    }
}
