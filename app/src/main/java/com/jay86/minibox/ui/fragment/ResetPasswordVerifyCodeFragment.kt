package com.jay86.minibox.ui.fragment


import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.network.RequestManager
import com.jay86.minibox.network.observer.BaseObserver
import com.jay86.minibox.utils.extension.hideKeyBoard
import com.jay86.minibox.utils.extension.snackbar
import com.jay86.usedmarket.network.observer.ProgressObserver
import kotlinx.android.synthetic.main.fragment_reset_password_verify_code.*
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

class ResetPasswordVerifyCodeFragment : BaseFragment(), Runnable {
    companion object {
        fun newInstance(phoneNumber: String, activity: AppCompatActivity) {
            val fragment = ResetPasswordVerifyCodeFragment()
            fragment.phoneNumber = phoneNumber
            activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainer, fragment, "second")
                    .addToBackStack("second")
                    .commit()
        }
    }

    override val layoutId: Int = R.layout.fragment_reset_password_verify_code
    private val codeInterval = 30
    private var code = ""
    private var getCodeTime = 0
    private lateinit var phoneNumber: String

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val parent = super.onCreateView(inflater, container, savedInstanceState)!!
        val verifyCodeView = parent.find<TextView>(R.id.getCodeView)
        val submitView = parent.find<Button>(R.id.submit)
        val phoneHint = parent.find<TextView>(R.id.phoneHint)
        phoneHint.text = "您的手机号为${phoneNumber}，请输入验证码"
        verifyCodeView.setOnClickListener { sendSms() }
        submitView.setOnClickListener { submit() }
        sendSms(verifyCodeView)
        return parent
    }

    private fun submit() {
        getCodeView.hideKeyBoard()
        if (getCodeView.text.toString() != code) {
            getCodeView.snackbar(R.string.common_hint_error_verification_code)
            return
        }
        RequestManager.resetPassword(phoneNumber, code, App.user!!.token, object : ProgressObserver<Unit>(activity) {
            override fun onNext(_object: Unit) {
                super.onNext(_object)
                activity.toast("修改成功")
                activity.finish()
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                getCodeView.snackbar(e.message
                        ?: getString(R.string.common_hint_error_verification_code))
            }
        })
    }

    private fun sendSms(verifyCodeView: TextView = getCodeView) {
        if (getCodeTime != 0) {
            return
        }
        verifyCodeView.isEnabled = false
        getCodeTime = codeInterval
        verifyCodeView.postDelayed(this, 1000)
        RequestManager.sendSms(phoneNumber, object : BaseObserver<String>() {
            override fun onNext(_object: String) {
                super.onNext(_object)
                code = _object
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                verifyCodeView.snackbar(e.message
                        ?: getString(R.string.common_hint_error_get_code_fail))
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
