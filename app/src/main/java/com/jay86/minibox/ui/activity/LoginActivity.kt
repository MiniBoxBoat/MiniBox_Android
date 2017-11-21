package com.jay86.minibox.ui.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jay86.minibox.R
import org.jetbrains.anko.startActivity

class LoginActivity : AppCompatActivity() {
    companion object {
        fun activityStart(context: Context, finishBefore: Boolean = true) {
            context.startActivity<LoginActivity>()
            if (finishBefore && context is Activity) context.finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}
