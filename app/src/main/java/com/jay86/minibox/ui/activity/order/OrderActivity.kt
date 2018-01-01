package com.jay86.minibox.ui.activity.order

import android.app.Activity
import android.os.Bundle
import com.jay86.minibox.R
import com.jay86.minibox.bean.BoxGroup
import com.jay86.minibox.ui.activity.BaseActivity
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class OrderActivity : BaseActivity() {
    companion object {
        fun activityStart(activity: Activity, url: String) {
            activity.startActivity<OrderActivity>("url" to url)
        }

        fun activityStart(activity: Activity, boxGroup: BoxGroup) {
            activity.startActivity<OrderActivity>("boxGroup" to boxGroup)
        }
    }

    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        url = intent.getStringExtra("url")
        toast(url)
    }
}
