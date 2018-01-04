package com.jay86.minibox.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jay86.minibox.R
import kotlinx.android.synthetic.main.fragment_appoint.*
import org.jetbrains.anko.find

/**
 * Created By jay68 on 2018/1/2.
 */
class ImmediatelyFragment : AppointFragment(), View.OnClickListener {

    override fun getOrderInfo() = hashMapOf(
            "boxCount" to boxCount.text.toString(),
            "boxSize" to boxType.selectedItem.toString()
    )

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val parent = super.onCreateView(inflater, container, savedInstanceState)
        parent.find<View>(R.id.openTimeContainer).visibility = View.GONE
        parent.find<View>(R.id.useTimeContainer).visibility = View.GONE
        parent.find<View>(R.id.openTimeDivider).visibility = View.GONE
        parent.find<View>(R.id.useTimeDivider).visibility = View.GONE
        return parent
    }
}