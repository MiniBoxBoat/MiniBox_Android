package com.jay86.minibox.ui.adapter

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.jay86.minibox.ui.fragment.BaseFragment

/**
 * Created by Jay
 */
class MyBoxPagerAdapter(fm: FragmentManager?, private val fragments: List<BaseFragment>) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int) = fragments[position]

    override fun getCount() = fragments.size

    override fun getPageTitle(position: Int) = fragments[position].title
}