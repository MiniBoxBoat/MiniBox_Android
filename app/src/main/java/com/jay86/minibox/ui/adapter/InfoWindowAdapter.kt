package com.jay86.minibox.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.amap.api.maps.AMap
import com.amap.api.maps.model.Marker
import com.jay86.minibox.R
import com.jay86.minibox.bean.BoxGroup
import com.jay86.minibox.ui.activity.order.OrderActivity
import org.jetbrains.anko.find

/**
 * Created By jay68 on 2018/1/1.
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
            OrderActivity.activityStart(activity, boxGroup)
        }
        return container
    }
}