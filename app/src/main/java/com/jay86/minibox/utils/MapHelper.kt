package com.jay86.minibox.utils

import android.graphics.Color
import android.location.Location
import android.view.MotionEvent
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.CameraPosition
import com.amap.api.maps.model.MyLocationStyle

/**
 * Created By jay68 on 2017/12/17.
 */
class MapHelper(val aMap: AMap) : AMap.OnMyLocationChangeListener, AMap.OnCameraChangeListener {
    companion object {
        private const val LOCATION_TYPE_CENTER = MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE
        private const val LOCATION_TYPE_NO_CENTER = MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER
    }

    private var preLocation: Location? = null
    private var curLocation: Location? = null

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
            setOnMyLocationChangeListener(this@MapHelper)
            setOnMapTouchListener { if (it.action == MotionEvent.ACTION_MOVE && aMap.isCenterLocked) aMap.lockCenter(false) }
            lockCenter(true)
        }
        aMap.uiSettings.apply {
            isMyLocationButtonEnabled = true
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isScaleControlsEnabled = true
        }
    }

    private val AMap.isCenterLocked: Boolean
        get() = myLocationStyle?.myLocationType == LOCATION_TYPE_CENTER

    private fun AMap.lockCenter(isLock: Boolean) {
        myLocationStyle = myLocationStyle
                .myLocationType(if (isLock) LOCATION_TYPE_CENTER else LOCATION_TYPE_NO_CENTER)
    }

    override fun onMyLocationChange(p0: Location?) {
        curLocation = p0
    }

    override fun onCameraChangeFinish(p0: CameraPosition?) {
    }

    override fun onCameraChange(p0: CameraPosition?) {
    }
}