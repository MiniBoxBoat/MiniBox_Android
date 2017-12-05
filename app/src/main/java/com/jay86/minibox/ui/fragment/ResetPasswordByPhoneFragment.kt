package com.jay86.minibox.ui.fragment

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.jay86.minibox.R
import com.jay86.minibox.bean.User
import com.jay86.minibox.utils.extension.snackbar
import org.jetbrains.anko.find

class ResetPasswordByPhoneFragment : BaseFragment() {
    override val layoutId: Int = R.layout.fragment_reset_password_by_phone

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parent = super.onCreateView(inflater, container, savedInstanceState) ?: throw Exception()
        val phone = parent.find<TextInputEditText>(R.id.phone)
        parent.find<Button>(R.id.submit).setOnClickListener {
            if (phone.length() !in User.PHONE_LENGTH) {
                phone.snackbar("手机号格式错误")
            } else {
                //todo 跳转
            }
        }
        return parent
    }
}
