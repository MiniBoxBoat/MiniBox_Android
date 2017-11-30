package com.jay86.minibox.utils.extension

import android.app.Activity
import android.content.Context
import com.jay86.minibox.config.SP_DEFAULT_FILENAME
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * Created By jay68 on 2017/11/28.
 */
fun Context.setPreference(key: String, value: Any?, name: String = SP_DEFAULT_FILENAME) {
    val editor = getSharedPreferences(name, Context.MODE_PRIVATE).edit()
    when (value) {
        is Boolean -> editor.putBoolean(key, value)
        is Int -> editor.putInt(key, value)
        is Float -> editor.putFloat(key, value)
        is Long -> editor.putLong(key, value)
        is String -> editor.putString(key, value)
    }
    editor.apply()
}

@Suppress("UNCHECKED_CAST")
fun <T> Context.getPreference(key: String, defaultValue: T, name: String = SP_DEFAULT_FILENAME): T? {
    val sp = getSharedPreferences(name, Context.MODE_PRIVATE)
    return when (defaultValue) {
        is Boolean -> sp.getBoolean(key, defaultValue) as T
        is Int -> sp.getInt(key, defaultValue) as T
        is Float -> sp.getFloat(key, defaultValue) as T
        is Long -> sp.getLong(key, defaultValue) as T
        is String -> sp.getString(key, defaultValue) as T
        else -> null
    }
}

fun Context.remove(key: String, name: String = SP_DEFAULT_FILENAME) {
    val editor = getSharedPreferences(name, Context.MODE_PRIVATE).edit()
    editor.remove(key)
    editor.apply()
}

fun Activity.doPermissionAction(
        permission: String,
        action: (() -> Unit)? = null,
        doOnRefuse: (() -> Unit)? = null
) {
    val rxPermissions = RxPermissions(this)
    if (rxPermissions.isGranted(permission)) action?.invoke()
    else rxPermissions.request(permission).subscribe { if (it) action?.invoke() else doOnRefuse?.invoke() }
}