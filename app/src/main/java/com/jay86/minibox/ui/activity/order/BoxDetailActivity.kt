package com.jay86.minibox.ui.activity.order

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.jay86.minibox.App
import com.jay86.minibox.R
import com.jay86.minibox.bean.Box
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.utils.LocationUtils
import com.jay86.minibox.utils.extension.moveCamera
import kotlinx.android.synthetic.main.activity_box_detail.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class BoxDetailActivity : BaseActivity() {
    override val title: String = "箱子详情"

    companion object {
        fun activityStart(activity: Activity, box: Box) {
            activity.startActivity<BoxDetailActivity>("box" to box)
        }
    }

    private lateinit var box: Box

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_box_detail)
        toolbar.init(View.OnClickListener { finish() })
        box = intent.getParcelableExtra("box")
        initMap(savedInstanceState)
        initView()
    }

    private fun initView() {
        val user = App.user!!
        username.text = user.nickname
        phoneNumber.text = user.phoneNumber
        position.text = "${box.groupName}${box.boxId}号米你箱"
        useTime.text = box.usedTime
        price.text = String.format("%.2f", box.price)
    }

    private fun initMap(savedInstanceState: Bundle?) {
        mapView.onCreate(savedInstanceState)
        val amap = mapView.map
        val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_marker)
        val option = MarkerOptions()
                .icon(icon)
                .setFlat(false)
                .visible(true)
                .draggable(false)
                .position(LatLng(box.lat, box.lng))
        amap.apply {
            moveCamera(box.lat, box.lng, 16f)
            addMarker(option)
        }
        amap.uiSettings.apply {
            isZoomControlsEnabled = false
            isMyLocationButtonEnabled = false
            setAllGesturesEnabled(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_box_detail_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.pay -> {
            //todo 支付
            if (!allowPay()) {
                toast("为了您的物品安全，请距离您的米你箱${OrderActivity.MAX_DISTANCE}米以内开启")
            } else {
                toast("支付功能审核中")
            }
            true
        }
        else -> false
    }

    private fun allowPay(): Boolean {
        val curLatLng = LocationUtils.getCurLatLng() ?: return false
        val boxLatLng = LatLng(box.lat, box.lng)
        val distance = AMapUtils.calculateLineDistance(curLatLng, boxLatLng)
        return distance <= OrderActivity.MAX_DISTANCE
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }
}
