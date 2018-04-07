package com.jay86.minibox.ui.activity.user

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jay86.minibox.R
import com.jay86.minibox.bean.User
import com.jay86.minibox.network.RequestManager
import com.jay86.minibox.network.observer.BaseObserver
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.activity.setting.NeedToKnowActivity
import com.jay86.minibox.utils.extension.*
import com.jay86.usedmarket.network.observer.ProgressObserver
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar_common.*
import org.jetbrains.anko.toast

class RegisterActivity : BaseActivity(), Runnable {
    override val title: String
        get() = getString(R.string.register_title)
    private val codeInterval = 30
    private var code = ""
    private var getCodeTime = 0

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

        getCodeView.setOnClickListener {
            sendSms()
        }

        needToKnow.setOnClickListener { activityStart<NeedToKnowActivity>(false) }
    }

    private fun sendSms() {
        if (getCodeTime != 0) {
            return
        }

        if (!phoneView.text.isPhoneNumber()) {
            phoneView.snackbar(getString(R.string.common_hint_error_phone_number))
            return
        }
        getCodeView.isEnabled = false
        getCodeTime = codeInterval
        getCodeView.postDelayed(this, 1000)
        RequestManager.sendSms(phoneView.text.toString(), object : BaseObserver<String>() {
            override fun onNext(_object: String) {
                super.onNext(_object)
                code = _object
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getCodeView.snackbar(getString(R.string.common_hint_error_get_code_fail))
            }
        })
    }

    private fun check() = when {
        nicknameView.length() !in User.NICKNAME_LENGTH -> {
            val format = getString(R.string.common_hint_error_nickname_length)
            nicknameView.snackbar(String.format(format, User.NICKNAME_LENGTH.first, User.NICKNAME_LENGTH.last))
            false
        }

        nicknameView.text.containsEmpty() -> {
            nicknameView.snackbar(getString(R.string.common_hint_error_empty_char))
            false
        }

        nicknameView.text[0] in '0'..'9' -> {
            nicknameView.snackbar(getString(R.string.common_hint_error_number_start))
            false
        }

        !phoneView.text.isPhoneNumber() -> {
            phoneView.snackbar(getString(R.string.common_hint_error_phone_number))
            false
        }

        passwordView.length() !in User.PASSWORD_LENGTH -> {
            val format = getString(R.string.common_hint_error_password_length)
            passwordView.snackbar(String.format(format, User.PASSWORD_LENGTH.first, User.PASSWORD_LENGTH.last))
            false
        }

        code.isEmpty() -> {
            codeView.snackbar(getString(R.string.common_hint_error_verification_code))
            false
        }

        codeView.text.toString() != code -> {
            codeView.snackbar(getString(R.string.common_hint_error_verification_code))
            false
        }

        !checkbox.isChecked -> {
            checkbox.snackbar("请先同意《米你箱用户服务协议》")
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
        val code = codeView.text.toString()
        //longToast("$nickname $phone $password $gender $code")
        RequestManager.register(nickname, phone, password.md5(), gender, code, object : ProgressObserver<User>(this) {
            override fun onNext(_object: User) {
                super.onNext(_object)
                toast(getString(R.string.register_register_successful))
                val intent = Intent()
                intent.putExtra("phone", phone)
                intent.putExtra("password", password)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                registerView.snackbar(e.message ?: getString(R.string.register_hint_error_default))
            }
        })
    }

    @SuppressLint("SetTextI18n")
    override fun run() {
        if (getCodeTime > 0) {
            getCodeView.text = "($getCodeTime s)"
            --getCodeTime
            getCodeView.postDelayed(this, 1000)
        } else {
            getCodeView.isEnabled = true
            getCodeView.setText(R.string.register_get_code)
        }
    }
}
