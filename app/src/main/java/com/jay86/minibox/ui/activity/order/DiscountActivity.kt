package com.jay86.minibox.ui.activity.order

import android.os.Bundle
import android.view.View
import com.jay86.minibox.R
import com.jay86.minibox.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.toolbar_common.*

class DiscountActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discount)
        toolbar.init(View.OnClickListener { finish() })

        //todo 优惠卷
    }
}
