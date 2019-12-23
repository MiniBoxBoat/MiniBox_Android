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
import com.jay86.minibox.ui.activity.BaseActivity
import com.jay86.minibox.utils.LocationUtils
import com.jay86.minibox.utils.extension.moveCamera
import com.jay86.usedmarket.network.observer.ProgressObserver
import kotlinx.android.synthetic.main.activity_using_box_detail.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class UsingBoxDetailActivity : BaseActivity() {
    override val title: String = "箱子详情"

    companion object {
        fun activityStart(activity: Activity, box: Box) {
            activity.startActivity<UsingBoxDetailActivity>("box" to box)
        }
    }

    private lateinit var box: Box

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_using_box_detail)
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
        submit.setOnClickListener {
            //todo 支付
            if (!BuildConfig.DEBUG && !allowPay()) {
                toast("为了您的物品安全，请距离您的米你箱${OrderActivity.MAX_DISTANCE}米以内开启")
            } else {
                RequestManager.endOrder(box.orderId, price.text.toString(), object : ProgressObserver<Unit>(this, "正在为您打开米你箱...") {
                    override fun onNext(_object: Unit) {
                        super.onNext(_object)
                        toast("已为您打开${box.boxId}号米你箱，请及时取走你的物品，欢迎下次使用～")
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        toast("打开米你箱失败：${e.message}，请稍后重试或联系客服")
                    }
                })
            }
        }
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
