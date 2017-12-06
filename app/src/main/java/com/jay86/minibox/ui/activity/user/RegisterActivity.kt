package com.jay86.minibox.ui.activity.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jay86.minibox.R
import com.jay86.minibox.bean.User
import com.jay86.minibox.network.RequestManager
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.activity.login.LoginActivity
import com.jay86.minibox.utils.extension.error
import com.jay86.minibox.utils.extension.md5
import com.jay86.minibox.utils.extension.orError
import com.jay86.usedmarket.network.observer.ProgressObserver
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar_common.*
import org.jetbrains.anko.toast

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
        registerView.setOnClickListener {
            performRegister()
        }
    }

    private fun check() = when {
        nicknameView.length() !in User.NICKNAME_LENGTH -> {
            val format = resources.getString(R.string.common_hint_error_nickname_length)
            nicknameView.error(String.format(format, User.NICKNAME_LENGTH.first, User.NICKNAME_LENGTH.last))
            false
        }

        nicknameView.text.contains("\\s".toRegex()) -> {
            nicknameView.error(getString(R.string.common_hint_error_empty_char))
            false
        }

        nicknameView.text[0] in '0'..'9' -> {
            nicknameView.error(getString(R.string.common_hint_error_number_start))
            false
        }

        phoneView.length() !in User.PHONE_LENGTH -> {
            val format = resources.getString(R.string.common_hint_error_phone_length)
            phoneView.error(String.format(format, User.PHONE_LENGTH.first, User.PHONE_LENGTH.last))
            false
        }

        passwordView.length() !in User.PASSWORD_LENGTH -> {
            val format = resources.getString(R.string.common_hint_error_password_length)
            passwordView.error(String.format(format, User.PASSWORD_LENGTH.first, User.PASSWORD_LENGTH.last))
            false
        }

        codeView.length() != resources.getInteger(R.integer.verification_code_length) -> {
            codeView.error(getString(R.string.common_hint_error_verification_code))
            false
        }

        else -> true
    }

    private fun performRegister() {
        if (!check()) return

        val nickname = nicknameView.text.toString()
        val phone = phoneView.text.toString()
        val password = passwordView.text.toString()
        val gender = genderView.selectedItem.toString()
        RequestManager.register(nickname, phone, password.md5(), gender, object : ProgressObserver<User>(this) {
            override fun onNext(_object: User) {
                super.onNext(_object)
                toast(getString(R.string.register_register_successful))
                val intent = Intent()
                intent.putExtra("phone", phone)
                intent.putExtra("password", password)
                setResult(LoginActivity.RESULT_CODE, intent)
                finish()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                registerView.error(e.message.orError(getString(R.string.register_hint_error_default)))
            }
        })
    }
}
