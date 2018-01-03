package com.jay86.minibox.utils.extension

import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.maps.model.MyLocationStyle
import com.jay86.minibox.R
import com.jay86.minibox.bean.BoxGroup

/**
 * Created By jay68 on 2018/1/3.
 */

private const val LOCATION_TYPE_CENTER = MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE
private const val LOCATION_TYPE_NO_CENTER = MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER

val AMap.isCenterLocked: Boolean
    get() = myLocationStyle?.myLocationType == LOCATION_TYPE_CENTER

fun AMap.lockCenter(isLock: Boolean) {
    myLocationStyle = myLocationStyle
            .myLocationType(if (isLock) LOCATION_TYPE_CENTER else LOCATION_TYPE_NO_CENTER)
}

fun AMap.showMarker(vararg boxGroup: BoxGroup) {
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

fun AMap.moveCamera(lat: Double? = null, lng: Double? = null, zoom: Float? = null) {
    val cp = cameraPosition
    val latLng = if (lat == null || lng == null) cp.target else LatLng(lat, lng)
    moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom ?: cp.zoom))
}

fun AMap.animateCamera(lat: Double? = null, lng: Double? = null, zoom: Float? = null) {
    val cp = cameraPosition
    val latLng = if (lat == null || lng == null) cp.target else LatLng(lat, lng)
    animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom ?: cp.zoom))
}