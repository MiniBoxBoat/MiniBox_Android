package com.jay86.minibox.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.TextView
import com.jay86.minibox.R
import com.jay86.minibox.bean.BoxGroup
import kotlinx.android.synthetic.main.fragment_appoint.*
import org.jetbrains.anko.find

/**
 * Created By jay68 on 2018/1/2.
 */
open class AppointFragment : BaseFragment(), View.OnClickListener {
    override val layoutId: Int = R.layout.fragment_appoint

    protected lateinit var boxGroup: BoxGroup

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        boxGroup = args!!.getParcelable("boxGroup")
    }

    open fun getOrderInfo() = listOf(
            "boxCount" to boxCount.text.toString(),
            "boxSize" to boxType.selectedItem.toString(),
            "openTime" to openTime.selectedItem.toString(),
            "useTime" to useTime.selectedItem.toString()
    )

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val parent = super.onCreateView(inflater, container, savedInstanceState)

        val boxType = parent.find<Spinner>(R.id.boxType)

        val plus = parent.find<View>(R.id.plus)
        val max = if (boxType.selectedItem == "大") boxGroup.largeEmpty else boxGroup.smallEmpty
        plus.isEnabled = max != 1
        plus.setOnClickListener(this)

        val minus = parent.find<View>(R.id.minus)
        minus.isEnabled = false
        minus.setOnClickListener(this)

        val boxCount = parent.find<TextView>(R.id.boxCount)
        boxType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                var count = boxCount.text.toString().toInt()
                val maxCount = if (position == 0) boxGroup.largeEmpty else boxGroup.smallEmpty
                if (count >= maxCount) {
                    count = maxCount
                    plus.isEnabled = false
                } else {
                    plus.isEnabled = true
                }
                if (maxCount == 1 || count == 1) {
                    minus.isEnabled = false
                }
                boxCount.text = "$count"
            }

        }

        parent.find<TextView>(R.id.boxPosition).text = boxGroup.title

        return parent
    }

    override fun onClick(v: View?) = when (v!!.id) {
        R.id.plus -> {
            val count = boxCount.text.toString().toInt()
            val max = if (boxType.selectedItem == "大") boxGroup.largeEmpty else boxGroup.smallEmpty
            plus.isEnabled = count + 1 != max
            minus.isEnabled = true
            boxCount.text = "${count + 1}"
        }

        R.id.minus -> {
            val count = boxCount.text.toString().toInt()
            minus.isEnabled = count != 2
            plus.isEnabled = true
            boxCount.text = "${count - 1}"
        }

        else -> Unit
    }
}