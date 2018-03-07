package com.jay86.minibox.ui.fragment

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jay86.minibox.R
import com.jay86.minibox.utils.extension.isPhoneNumber
import com.jay86.minibox.utils.extension.snackbar
import org.jetbrains.anko.find

class ResetPasswordByPhoneFragment : BaseFragment() {
    override val layoutId: Int = R.layout.fragment_reset_password_by_phone

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val parent = super.onCreateView(inflater, container, savedInstanceState)!!
        val phone = parent.find<TextInputEditText>(R.id.phoneView)
        parent.find<Button>(R.id.submit).setOnClickListener {
            if (phone.text.isPhoneNumber()) {
                ResetPasswordVerifyCodeFragment.newInstance(phone.text.toString(), activity as AppCompatActivity)
            } else {
                phone.snackbar("手机号格式错误")
            }
        }
        return parent
    }
}
