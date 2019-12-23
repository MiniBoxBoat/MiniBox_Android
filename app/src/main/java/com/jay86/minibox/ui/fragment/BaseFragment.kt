package com.jay86.minibox.ui.fragment

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Jay
 */
open class BaseFragment : Fragment() {
    @LayoutRes
    protected open val layoutId: Int = -1
    open val title = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return if (layoutId == -1) null else inflater!!.inflate(layoutId, container, false)
    }
}