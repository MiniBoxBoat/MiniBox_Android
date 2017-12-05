package com.jay86.minibox.ui.activity.login

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
import com.jay86.minibox.utils.extension.hideKeyBoard
import com.jay86.minibox.utils.extension.md5
import com.jay86.minibox.utils.extension.setPreference
import com.jay86.minibox.utils.extension.snackbar
import com.jay86.usedmarket.network.observer.ProgressObserver
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity() {
    companion object {
        val REQUEST_CODE = LoginActivity::javaClass.hashCode()
        val RESULT_CODE = "register".hashCode()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_CODE) {
            account.setText(data?.getStringExtra("phone") ?: "")
            password.setText(data?.getStringExtra("password") ?: "")
        }
    }

    private fun initView() {
        account.requestFocus()
        toRegister.setOnClickListener { activityStart<RegisterActivity>(false) }
        login.setOnClickListener { performLogin() }
        forgetPassword.setOnClickListener { activityStartForResult<ForgetPasswordActivity>(REQUEST_CODE) }
        //软键盘登陆
        password.setOnEditorActionListener { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    password.hideKeyBoard()
                    performLogin()
                    true
                }
                else -> false
            }
        }
    }

    private fun performLogin() {
        when {
            account.length() !in User.PHONE_LENGTH -> {
                account.snackbar(resources.getString(R.string.common_hint_error_account))
                return
            }

            password.length() !in User.PASSWORD_LENGTH -> {
                password.snackbar(resources.getString(R.string.common_hint_error_password))
                return
            }
        }
        val pwd = password.text.toString().md5()
        RequestManager.login(account.text.toString(), pwd, object : ProgressObserver<User>(this) {
            override fun onNext(_object: User) {
                super.onNext(_object)
                _object.password = pwd
                App.user = _object
                setPreference(SP_USER_KEY, _object.toString())
                finish()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                //todo 错误处理
                login.snackbar(e.message ?: getString(R.string.common_hint_network_error))
            }
        })
    }
}
