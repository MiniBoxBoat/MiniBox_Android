package com.jay86.minibox.ui.activity.main

import android.os.Bundle
import android.view.View
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.toolbar_common.*

class SettingActivity : BaseActivity() {
    override val title: String
        get() = getString(R.string.setting_title)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        toolbar.init(View.OnClickListener { finish() })
    }
}
