package com.jay86.minibox.ui.activity.order

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.view.View
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.adapter.MyBoxPagerAdapter
import com.jay86.minibox.ui.fragment.AppointingBoxFragment
import com.jay86.minibox.ui.fragment.UsingBoxFragment
import kotlinx.android.synthetic.main.activity_my_box.*
import kotlinx.android.synthetic.main.toolbar_common.*

class MyBoxActivity : BaseActivity() {
    override val title: String = "我的箱子"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_box)
        toolbar.init(View.OnClickListener { finish() })

        val pagerAdapter = MyBoxPagerAdapter(supportFragmentManager, listOf(UsingBoxFragment(), AppointingBoxFragment()))
        viewPager.adapter = pagerAdapter
        tabLayout.tabMode = TabLayout.MODE_FIXED
        tabLayout.setupWithViewPager(viewPager)
    }
}
