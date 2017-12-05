package com.jay86.minibox.ui.fragment

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created By jay68 on 2017/12/2.
 */
open class BaseFragment : Fragment() {
    @LayoutRes open protected val layoutId: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(layoutId, container, false)
    }
}