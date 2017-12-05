package com.jay86.minibox.ui.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jay86.minibox.R

class ResetPasswordVerifyCodeFragment : BaseFragment() {
    companion object {
        fun newInstance(phoneNumber: String): ResetPasswordVerifyCodeFragment {
            val fragment = ResetPasswordVerifyCodeFragment()
            fragment.phoneNumber = phoneNumber
            return fragment
        }
    }

    override val layoutId: Int = R.layout.fragment_reset_password_verify_code
    private lateinit var phoneNumber: String

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parent = super.onCreateView(inflater, container, savedInstanceState) ?: throw Exception()
        //todo 事件
        return parent
    }
}
