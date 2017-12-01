package com.jay86.minibox.ui.activity.user

import android.os.Bundle
import android.view.View
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.toolbar_common.*

class RegisterActivity : BaseActivity() {
    override val title: String
        get() = getString(R.string.register_title)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        toolbar.init(View.OnClickListener { finish() })

        //todo register
    }
}
