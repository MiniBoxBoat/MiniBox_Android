package com.jay86.minibox.ui.activity.order

import android.app.Activity
import android.os.Bundle
import android.view.View
import com.amap.api.maps.AMapUtils
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.jay86.minibox.App
import com.jay86.minibox.BuildConfig
import com.jay86.minibox.R
import com.jay86.minibox.bean.Box
import com.jay86.minibox.network.RequestManager
import com.jay86.minibox.network.observer.BaseObserver
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.ui.activity.login.LoginActivity
import com.jay86.minibox.utils.LocationUtils
import com.jay86.minibox.utils.extension.activityStart
import com.jay86.minibox.utils.extension.moveCamera
import com.jay86.usedmarket.network.observer.ProgressObserver
import kotlinx.android.synthetic.main.activity_appointing_box_detail.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class AppointingBoxDetailActivity : BaseActivity() {

    companion object {
        fun activityStart(activity: Activity, box: Box) {
            activity.startActivity<AppointingBoxDetailActivity>("box" to box)
        }
    }

    private lateinit var box: Box

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointing_box_detail)
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
        boxType.text = box.boxSize
        openTime.text = box.openTime.split(".")[0]
        submit.setOnClickListener { submitOrder() }
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
                .infoWindowEnable(false)

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

    private fun submitOrder() {
        if (!BuildConfig.DEBUG && !allowOpenBox()) {
            toast("您距离箱子过远，无法开启箱子")
            return
        }
        val user = App.user
        if (user == null) {
            toast("登陆过期，请重新登陆")
            activityStart<LoginActivity>()
            return
        }
        RequestManager.convertAppointToOrder(box.orderId, user.token, object : ProgressObserver<Unit>(this, "正在为您开箱...") {
            override fun onNext(_object: Unit) {
                super.onNext(_object)
                toast("已为您开启${box.boxId}号箱")
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                toast("开启箱子失败：${e.message}")
            }
        })
    }

    private fun allowOpenBox(): Boolean {
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
