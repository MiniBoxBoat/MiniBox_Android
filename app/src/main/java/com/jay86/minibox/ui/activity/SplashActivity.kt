package com.jay86.minibox.ui.activity

import android.os.Bundle
import android.os.Handler
import com.jay86.minibox.R


class SplashActivity : BaseActivity() {
    private val millisToFinish = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setFullScreen()
        Handler().postDelayed({ activityStart<MainActivity>() }, millisToFinish)
    }
}
