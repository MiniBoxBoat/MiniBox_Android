package com.jay86.minibox.ui.activity.user

import android.os.Bundle
import android.text.Html
import com.jay86.minibox.R
import com.jay86.minibox.bean.User
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.utils.snackbar
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
    }

    private fun initView() {
        val (hint, register) = resources.getString(R.string.login_register).split("?")
        toRegister.text = Html.fromHtml("$hint?<font color='#263238'>$register</font>")

        toRegister.setOnClickListener { activityStart<RegisterActivity>(false) }
        login.setOnClickListener { performLogin() }
    }

    private fun performLogin() {
        when {
            account.length() < User.ACCOUNT_MIN_LENGTH -> {
                account.snackbar(resources.getString(R.string.login_hint_error_account))
                return
            }

            password.length() < User.PASSWORD_MIN_LENGTH -> {
                password.snackbar(resources.getString(R.string.login_hint_error_password))
                return
            }
        }
        //todo 登陆
    }
}
