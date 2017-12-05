package com.jay86.minibox.ui.activity.login

import android.os.Bundle
import android.view.View
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.fragment.ResetPasswordByPhoneFragment
import kotlinx.android.synthetic.main.toolbar_common.*

class ForgetPasswordActivity : BaseActivity() {
    override val title: String
        get() = getString(R.string.forget_pwd_title)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)
        //todo 回退fragment
        toolbar.init(View.OnClickListener { finish() })

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, ResetPasswordByPhoneFragment())
        transaction.commit()
    }
}
