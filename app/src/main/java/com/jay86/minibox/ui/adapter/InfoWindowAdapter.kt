package com.jay86.minibox.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.amap.api.maps.AMap
import com.amap.api.maps.model.Marker
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.bean.BoxGroup
import com.jay86.minibox.ui.activity.login.LoginActivity
import com.jay86.minibox.ui.activity.order.OrderActivity
import com.jay86.minibox.utils.extension.activityStart
import org.jetbrains.anko.find
import org.jetbrains.anko.toast

/**
 * Created by Jay
 */
class InfoWindowAdapter(private val activity: Activity) : AMap.InfoWindowAdapter {

    override fun getInfoContents(p0: Marker?) = null

    override fun getInfoWindow(p0: Marker?): View? {
        val boxGroup = BoxGroup.fromJson(p0?.snippet ?: return null) ?: return null
        val container = LayoutInflater.from(activity)
                .inflate(R.layout.item_info_window, null, false)
        container.find<TextView>(R.id.title).text = boxGroup.title
        container.find<TextView>(R.id.largeEmpty).text = boxGroup.largeEmpty.toString()
        container.find<TextView>(R.id.smallEmpty).text = boxGroup.smallEmpty.toString()
        container.find<ImageView>(R.id.openBox).setOnClickListener {
            if (App.isLogin) {
                if (boxGroup.largeEmpty == 0 && boxGroup.smallEmpty == 0) {
                    activity.toast("${boxGroup.title}的箱子已用完")
                    return@setOnClickListener
                }
                OrderActivity.activityStart(activity, boxGroup)
            } else {
                activity.activityStart<LoginActivity>(false)
            }
        }
        return container
    }
}