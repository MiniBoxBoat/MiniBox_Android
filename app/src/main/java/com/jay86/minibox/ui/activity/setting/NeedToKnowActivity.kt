package com.jay86.minibox.ui.activity.setting

import android.os.Bundle
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_need_to_know.*

class NeedToKnowActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_need_to_know)
        content.loadUrl("http://minibox.jay86.com/needToKnow.html")
    }
}
