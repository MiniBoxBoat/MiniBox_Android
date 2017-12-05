package com.jay86.minibox.ui.activity.user

import android.os.Bundle
import android.view.View
import com.jay86.minibox.R
import com.jay86.minibox.bean.User
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.utils.extension.snackbar
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar_common.*

class RegisterActivity : BaseActivity() {
    override val title: String
        get() = getString(R.string.register_title)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initView()
    }

    private fun initView() {
        toolbar.init(View.OnClickListener { finish() })
        register.setOnClickListener {
            performRegister()
        }
    }

    private fun check() = when {
        nickname.length() !in User.NICKNAME_LENGTH -> {
            val format = resources.getString(R.string.common_hint_error_nickname_length)
            nickname.snackbar(String.format(format, User.NICKNAME_LENGTH.first, User.NICKNAME_LENGTH.last))
            false
        }
        else -> true
    }

    private fun performRegister() {
        //todo check
        check()
    }
}
