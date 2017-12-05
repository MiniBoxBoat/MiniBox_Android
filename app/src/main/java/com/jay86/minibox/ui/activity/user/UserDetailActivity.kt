package com.jay86.minibox.ui.activity.user

import android.os.Bundle
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.BaseActivity

class UserDetailActivity : BaseActivity() {
    override val title: String
        get() = getString(R.string.main_user_detail)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
    }
}
