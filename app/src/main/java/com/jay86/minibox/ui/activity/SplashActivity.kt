package com.jay86.minibox.ui.activity

import android.os.Bundle
import com.jay86.minibox.R


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setFullScreen()
    }
}
