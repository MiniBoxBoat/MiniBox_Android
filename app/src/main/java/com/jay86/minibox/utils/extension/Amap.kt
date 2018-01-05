package com.jay86.minibox.utils.extension

import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
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
    if (isLock == isCenterLocked) return
    myLocationStyle = myLocationStyle
            .myLocationType(if (isLock) LOCATION_TYPE_CENTER else LOCATION_TYPE_NO_CENTER)
}

fun AMap.moveToMarker(boxGroup: BoxGroup): Marker {
    val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_marker)
    val option = MarkerOptions()
            .icon(icon)
            .setFlat(false)
            .visible(true)
            .draggable(false)
            .position(LatLng(boxGroup.lat, boxGroup.lng))
            .title(boxGroup.title)
            .snippet(BoxGroup.toJson(boxGroup))
    val marker = addMarker(option)
    marker.showInfoWindow()
    animateCamera(boxGroup.lat, boxGroup.lng)
    return marker
}

fun AMap.showMarker(vararg boxGroup: BoxGroup, infoEnable: Boolean = true/*, zoomToShowAll: Boolean = false*/): List<Marker> {
    val markerList = arrayListOf<Marker>()
    val icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_location_marker)
    val option = MarkerOptions()
            .icon(icon)
            .setFlat(false)
            .visible(true)
            .draggable(false)
            .infoWindowEnable(infoEnable)

    boxGroup.forEach {
        option.position(LatLng(it.lat, it.lng))
                .title(it.title)
                .snippet(BoxGroup.toJson(it))
        markerList += addMarker(option)
    }
    return markerList
    /*if (zoomToShowAll) {
        val builder = LatLngBounds.builder()
        boxGroup.forEach { builder.include(LatLng(it.lat, it.lng)) }
        lockCenter(false)
        animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 15))
    }*/
}

fun AMap.moveCamera(lat: Double? = null, lng: Double? = null, zoom: Float? = null) {
    lockCenter(false)
    val cp = cameraPosition
    val latLng = if (lat == null || lng == null) cp.target else LatLng(lat, lng)
    moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom ?: cp.zoom))
}

fun AMap.animateCamera(lat: Double? = null, lng: Double? = null, zoom: Float? = null) {
    lockCenter(false)
    val cp = cameraPosition
    val latLng = if (lat == null || lng == null) cp.target else LatLng(lat, lng)
    animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom ?: cp.zoom))
}