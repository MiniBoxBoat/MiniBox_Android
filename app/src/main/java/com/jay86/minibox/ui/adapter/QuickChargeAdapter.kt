package com.jay86.minibox.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.LatLng
import com.jay86.minibox.R
import com.jay86.minibox.bean.BoxGroup
import com.jay86.minibox.utils.LocationUtils
import org.jetbrains.anko.find

/**
 * Created By jay68 on 2018/4/7.
 */
class QuickChargeAdapter(val boxGroup: List<BoxGroup>) :
        RecyclerView.Adapter<QuickChargeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_quick_charge_rv, parent, false)
        return ViewHolder(itemView)

    }

    override fun getItemCount() = boxGroup.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = LatLng(boxGroup[position].lat, boxGroup[position].lng)
        val distance = AMapUtils.calculateLineDistance(location, LocationUtils.getCurLatLng())
        holder.distance.text = String.format("%.2f km", distance / 1000)
        holder.location.text = boxGroup[position].title
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val distance = itemView.find<TextView>(R.id.distance)
        val location = itemView.find<TextView>(R.id.location)
    }
}