package com.jay86.minibox.ui.activity.user

import android.os.Bundle
import android.view.View
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.utils.extension.activityStart
import com.jay86.minibox.utils.extension.setImageUrl
import kotlinx.android.synthetic.main.activity_user_detail.*
import kotlinx.android.synthetic.main.toolbar_common.*

class UserDetailActivity : BaseActivity() {
    override val title: String
        get() = getString(R.string.main_user_detail)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)
        toolbar.init(View.OnClickListener { finish() })

        val user = App.user!!

        avatarView.setImageUrl(user.avatar, resources.getDrawable(R.drawable.default_avatar))
        realView.text = if (user.trueName.isNotEmpty()) user.trueName else getString(R.string.user_detail_default_real_name)
        creditView.text = "${user.credibility ?: 0}"
        useTimeView.text = "${user.useTime ?: 0}"

        editUserView.setOnClickListener { activityStart<EditInfoActivity>(false) }
    }

    override fun onResume() {
        super.onResume()
        val user = App.user!!
        nicknameView.text = user.nickname
        phoneView.text = user.phoneNumber
        genderView.text = user.sex
        emailView.text = if (user.email.isNotEmpty()) user.email else getString(R.string.user_detail_default_email)
    }
}
