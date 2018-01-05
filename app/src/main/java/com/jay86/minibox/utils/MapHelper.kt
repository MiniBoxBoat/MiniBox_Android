package com.jay86.minibox.utils

import android.app.Activity
import android.graphics.Color
import android.location.Location
import android.view.MotionEvent
import com.amap.api.maps.AMap
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MyLocationStyle
import com.jay86.minibox.bean.BoxGroup
import com.jay86.minibox.network.RequestManager
import com.jay86.minibox.network.observer.BaseObserver
import com.jay86.minibox.ui.adapter.InfoWindowAdapter
import com.jay86.minibox.utils.extension.animateCamera
import com.jay86.minibox.utils.extension.lockCenter
import com.jay86.minibox.utils.extension.showMarker
import org.jetbrains.anko.toast


/**
 * Created By jay68 on 2017/12/17.
 */
class MapHelper(val aMap: AMap, private val activity: Activity) : AMap.OnMyLocationChangeListener,
        AMap.OnCameraChangeListener, AMap.OnInfoWindowClickListener, AMap.OnMapTouchListener {

    private var curLocation: Location? = null
    private var curMarker: Marker? = null

    fun init() {
        val customLocationStyle = MyLocationStyle()
                .interval(2000)
                .strokeColor(Color.WHITE)
                .radiusFillColor(Color.argb(40, 169, 70, 142))
                .strokeWidth(1.0f)

        aMap.apply {
            isMyLocationEnabled = true
            myLocationStyle = customLocationStyle
            animateCamera(zoom = 15f)
            setOnCameraChangeListener(this@MapHelper)
            setOnMyLocationChangeListener(this@MapHelper)
            setInfoWindowAdapter(InfoWindowAdapter(activity))
            setOnInfoWindowClickListener(this@MapHelper)
            setOnMapTouchListener(this@MapHelper)
            setOnMarkerClickListener {
                lockCenter(false)
                curMarker = it
                false
            }
            lockCenter(true)
        }
        aMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isScaleControlsEnabled = true
            isRotateGesturesEnabled = false
            isTiltGesturesEnabled = false
        }
    }

    fun myLocation() = aMap.lockCenter(true)

    override fun onTouch(p0: MotionEvent?) {
        when (p0?.action) {
            MotionEvent.ACTION_MOVE -> aMap.lockCenter(false)

            MotionEvent.ACTION_UP -> curMarker?.hideInfoWindow()
        }
    }

    override fun onInfoWindowClick(p0: Marker?) {
    }

    override fun onMyLocationChange(p0: Location?) {
        curLocation = p0
    }

    override fun onCameraChangeFinish(p0: CameraPosition?) {
        val position = p0?.target ?: return
        RequestManager.searchByPoint(position.latitude, position.longitude, object : BaseObserver<List<BoxGroup>>() {
            override fun onNext(_object: List<BoxGroup>) {
                super.onNext(_object)
                aMap.showMarker(*_object.toTypedArray())
            }

            override fun onError(e: Throwable) {
                super.onError(e)
                RequestManager.searchByPoint(position.latitude, position.longitude, object : BaseObserver<List<BoxGroup>>() {
                    override fun onNext(_object: List<BoxGroup>) {
                        super.onNext(_object)
                        aMap.showMarker(*_object.toTypedArray())
                    }

                    override fun onError(e: Throwable) {
                        super.onError(e)
                        activity.toast(e.message ?: "网络异常")
                    }
                })
            }
        })
    }

    override fun onCameraChange(p0: CameraPosition?) = Unit
}