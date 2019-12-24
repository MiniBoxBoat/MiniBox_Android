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
 * Created by Jay
 */
class SearchRvAdapter(val callback: (boxGroup: BoxGroup) -> Unit)
    : RecyclerView.Adapter<SearchRvAdapter.ViewHolder>() {

    private val distance = mutableListOf<Float>()
    private var data = listOf<BoxGroup>()

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search_rv, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val boxGroup = data[position]
        holder.position.text = boxGroup.title
        if (distance.size > position) {
            val ds = if (distance[position] >= 1000)
                String.format("%.2fkm", distance[position] / 1000)
            else "${distance[position].toInt()}m"
            holder.distance.text = ds
        }  else {
            holder.distance.text = "10km"
        }

        holder.itemView.setOnClickListener {
            callback(boxGroup)
        }
    }

    fun resetData(list: List<BoxGroup>) {
        data = list
        distance.clear()
        data.sortedBy {
            val ds = AMapUtils.calculateLineDistance(LatLng(it.lat, it.lng), LocationUtils.getCurLatLng())
            distance.add(ds)
            ds
        }
        distance.sort()
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val position = itemView.find<TextView>(R.id.position)
        val distance = itemView.find<TextView>(R.id.distance)
    }
}