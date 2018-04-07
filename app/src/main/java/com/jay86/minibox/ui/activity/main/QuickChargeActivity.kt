package com.jay86.minibox.ui.activity.main

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.jay86.minibox.R
import com.jay86.minibox.bean.BoxGroup
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.adapter.QuickChargeAdapter
import kotlinx.android.synthetic.main.activity_quick_charge.*
import kotlinx.android.synthetic.main.toolbar_common.*
import org.jetbrains.anko.startActivity

class QuickChargeActivity : BaseActivity() {
    override val title = "快速充电"

    companion object {
        @JvmStatic
        fun activityStart(activity: Activity, boxGroup: List<BoxGroup>) {
            activity.startActivity<QuickChargeActivity>("boxGroup" to boxGroup)
        }
    }

    lateinit var boxGroup: List<BoxGroup>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quick_charge)
        toolbar.init(View.OnClickListener { finish() })
        boxGroup = intent.getParcelableArrayListExtra("boxGroup")
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = layoutManager
        rv.adapter = QuickChargeAdapter(boxGroup)
        rv.adapter.notifyDataSetChanged()
    }
}
