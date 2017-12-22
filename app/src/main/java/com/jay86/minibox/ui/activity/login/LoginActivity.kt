package com.jay86.minibox.ui.activity.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.bean.User
import com.jay86.minibox.config.SP_USER_KEY
import com.jay86.minibox.network.RequestManager
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.activity.user.RegisterActivity
import com.jay86.minibox.utils.extension.*
import com.jay86.usedmarket.network.observer.ProgressObserver
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    companion object {
        val REQUEST_CODE = 0x1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            accountView.setText(data?.getStringExtra("phone") ?: "")
            passwordView.setText(data?.getStringExtra("password") ?: "")
        }
    }

    private fun initView() {
        accountView.requestFocus()
        toRegisterLayout.setOnClickListener { activityStartForResult<RegisterActivity>(REQUEST_CODE) }
        loginView.setOnClickListener { performLogin() }
        forgetPasswordView.setOnClickListener { activityStartForResult<ForgetPasswordActivity>(REQUEST_CODE) }
        //软键盘登陆
        passwordView.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    passwordView.hideKeyBoard()
                    performLogin()
                    true
                }
                else -> false
            }
        }
    }

    private fun performLogin() {
        when {
            !accountView.text.isPhoneNumber() -> {
                accountView.snackbar(resources.getString(R.string.common_hint_error_phone_number))
                return
            }

            passwordView.length() !in User.PASSWORD_LENGTH -> {
                passwordView.snackbar(resources.getString(R.string.common_hint_error_password))
                return
            }
        }
        val pwd = passwordView.text.toString().md5()
        RequestManager.login(accountView.text.toString(), pwd, object : ProgressObserver<User>(this) {
            override fun onNext(_object: User) {
                super.onNext(_object)
                _object.password = pwd
                App.user = _object
                setPreference(SP_USER_KEY, _object.toString())
                finish()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                loginView.snackbar(e.message ?: getString(R.string.common_hint_network_error))
            }
        })
    }
}
