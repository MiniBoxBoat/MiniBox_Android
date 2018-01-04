package com.jay86.minibox.ui.activity

import android.os.Bundle
import android.os.Handler
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.main.MainActivity
import com.jay86.minibox.utils.extension.activityStart


class SplashActivity : BaseActivity() {
    private val millisToFinish = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setFullScreen()
        Handler().postDelayed({ activityStart<MainActivity>() }, millisToFinish)
    }
}
