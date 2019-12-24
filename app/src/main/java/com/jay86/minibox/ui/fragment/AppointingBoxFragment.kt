package com.jay86.minibox.ui.fragment

import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.bean.Box
import com.jay86.minibox.network.RequestManager
import com.jay86.minibox.network.observer.BaseObserver
import com.jay86.minibox.ui.activity.order.AppointingBoxDetailActivity
import com.jay86.minibox.ui.activity.order.UsingBoxDetailActivity
import com.jay86.minibox.ui.adapter.AppointingAdapter
import kotlinx.android.synthetic.main.fragment_using_box.*
import org.jetbrains.anko.find
import org.jetbrains.anko.longToast

/**
 * Created by Jay
 */
class AppointingBoxFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    override val title: String = "已预约"
    override val layoutId: Int = R.layout.fragment_appointing_box

    private val adapter = AppointingAdapter(arrayListOf()) { _, box ->
        AppointingBoxDetailActivity.activityStart(activity, box)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val parent = super.onCreateView(inflater, container, savedInstanceState)!!

        val rv = parent.find<RecyclerView>(R.id.rv)
        val layoutManager = LinearLayoutManager(activity)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        rv.layoutManager = layoutManager
        rv.adapter = adapter

        val refreshLayout = parent.find<SwipeRefreshLayout>(R.id.refreshLayout)
        refreshLayout.setOnRefreshListener(this)
        refreshLayout.isRefreshing = true
        onRefresh()

        return parent
    }

    override fun onResume() {
        super.onResume()
        onRefresh()
    }

    override fun onRefresh() {
        RequestManager.showAppointingBox(App.user!!.token, object : BaseObserver<List<Box>>() {
            override fun onNext(_object: List<Box>) {
                super.onNext(_object)
                refreshLayout.isRefreshing = false
                adapter.refreshData(_object)
                if (_object.isEmpty()) {
                    emptyContainer.visibility = View.VISIBLE
                    return
                }
                emptyContainer.visibility = View.GONE
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                refreshLayout.isRefreshing = false
                activity.longToast("获取预约箱子出错：${e.message ?: "网络异常"}")
            }
        })
    }
}