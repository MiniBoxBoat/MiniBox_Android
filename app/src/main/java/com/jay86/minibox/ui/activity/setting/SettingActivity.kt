package com.jay86.minibox.ui.activity.setting

import android.os.Bundle
import android.view.View
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.activity.login.LoginActivity
import com.jay86.minibox.ui.activity.setting.NeedToKnowActivity
import com.jay86.minibox.utils.extension.activityStart
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.toolbar_common.*
import org.jetbrains.anko.toast

class SettingActivity : BaseActivity() {
    override val title: String
        get() = getString(R.string.setting_title)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        toolbar.init(View.OnClickListener { finish() })

        account.setOnClickListener { toast("功能开发中...") }
        notify.setOnClickListener { toast("功能开发中...") }
        language.setOnClickListener { toast("功能开发中...") }
        manual.setOnClickListener { toast("功能开发中...") }
        needToKnow.setOnClickListener { activityStart<NeedToKnowActivity>(false) }
        address.setOnClickListener { toast("功能开发中...") }
        update.setOnClickListener { toast("已是最新版本") }
        logout.setOnClickListener {
            App.logout()
            activityStart<LoginActivity>()
        }
    }
}
