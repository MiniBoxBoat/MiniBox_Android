package com.jay86.minibox.utils

import android.app.Activity
import android.graphics.Color
import android.location.Location
import android.view.MotionEvent
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.jay86.minibox.R
import com.jay86.minibox.bean.BoxGroup
import com.jay86.minibox.network.RequestManager
import com.jay86.minibox.network.observer.BaseObserver
import com.jay86.minibox.ui.adapter.InfoWindowAdapter

/**
 * Created By jay68 on 2017/12/17.
 */
class MapHelper(private val aMap: AMap, private val activity: Activity) : AMap.OnMyLocationChangeListener, AMap.OnCameraChangeListener,
        AMap.OnInfoWindowClickListener, AMap.OnMapTouchListener {

    companion object {
        private const val LOCATION_TYPE_CENTER = MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE
        private const val LOCATION_TYPE_NO_CENTER = MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER
    }

    private var preLocation: Location? = null
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
            animateCamera(CameraUpdateFactory.zoomTo(15.0f))
            setOnCameraChangeListener(this@MapHelper)
            setOnMyLocationChangeListener(this@MapHelper)
            setInfoWindowAdapter(InfoWindowAdapter(activity))
            setOnInfoWindowClickListener(this@MapHelper)
            setOnMapTouchListener(this@MapHelper)
            setOnMarkerClickListener {
                curMarker = it
                false
            }
            lockCenter(true)
        }
        aMap.uiSettings.apply {
            isZoomControlsEnabled = true
            isScaleControlsEnabled = true
            isRotateGesturesEnabled = false
        }
    }

    fun myLocation() = aMap.lockCenter(true)

    private val AMap.isCenterLocked: Boolean
        get() = myLocationStyle?.myLocationType == LOCATION_TYPE_CENTER

    private fun AMap.lockCenter(isLock: Boolean) {
        myLocationStyle = myLocationStyle
                .myLocationType(if (isLock) LOCATION_TYPE_CENTER else LOCATION_TYPE_NO_CENTER)
    }

    private fun AMap.showMarker(boxGroup: List<BoxGroup>) {
        val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_marker)
        val option = MarkerOptions()
                .icon(icon)
                .setFlat(false)
                .visible(true)
                .draggable(false)

        boxGroup.forEach {
            option.position(LatLng(it.lat, it.lng))
                    .title(it.title)
                    .snippet(BoxGroup.toJson(it))
            addMarker(option)
        }
    }

    override fun onTouch(p0: MotionEvent?) {
        when (p0?.action) {
            MotionEvent.ACTION_MOVE -> {
                if (aMap.isCenterLocked) aMap.lockCenter(false)
                aMap.infoWindowAnimationManager.setInfoWindowBackEnable(false)
            }

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
                aMap.showMarker(_object)
            }
        })
    }

    override fun onCameraChange(p0: CameraPosition?) = Unit
}