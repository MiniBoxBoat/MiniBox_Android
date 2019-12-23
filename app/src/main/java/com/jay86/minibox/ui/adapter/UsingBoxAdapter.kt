package com.jay86.minibox.ui.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.jay86.minibox.R
import com.jay86.minibox.bean.Box
import org.jetbrains.anko.find

/**
 * Created by Jay
 */
class UsingBoxAdapter(val data: MutableList<Box>,
                      private val onItemSelectedListener: (position: Int, box: Box) -> Unit)
    : RecyclerView.Adapter<UsingBoxAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_using_box, parent, false)
    )

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener { onItemSelectedListener(position, data[position]) }
        holder.boxType.text = data[position].boxSize
        holder.boxId.text = data[position].boxId
        holder.groupName.text = data[position].groupName
        holder.useTime.text = data[position].usedTime
        holder.price.text = String.format("%.2f", data[position].price)
    }

    fun refreshData(list: List<Box>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val boxType = itemView.find<TextView>(R.id.boxType)
        val boxId = itemView.find<TextView>(R.id.boxId)
        val useTime = itemView.find<TextView>(R.id.useTime)
        val price = itemView.find<TextView>(R.id.price)
        val groupName = itemView.find<TextView>(R.id.groupName)
    }
}