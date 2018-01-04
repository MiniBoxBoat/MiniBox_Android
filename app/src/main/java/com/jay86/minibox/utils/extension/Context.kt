package com.jay86.minibox.utils.extension

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.jay86.minibox.R
import com.jay86.minibox.config.SP_DEFAULT_FILENAME
import com.tbruyelle.rxpermissions2.RxPermissions
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.yesButton


/**
 * Created By jay68 on 2017/11/28.
 */

inline fun <reified T : Activity> Activity.activityStart(finishBefore: Boolean = true) {
    startActivity<T>()
    if (finishBefore) finish()
}

inline fun <reified T : Activity> Activity.activityStartForResult(requestCode: Int)
        = startActivityForResult<T>(requestCode)

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

fun Activity.doPermissionActionWithHint(
        permission: String,
        reason: String,
        action: (() -> Unit)? = null,
        doOnRefuse: (() -> Unit)? = null
) {
    val rxPermissions = RxPermissions(this@doPermissionActionWithHint)
    if (rxPermissions.isGranted(permission)) {
        action?.invoke()
        return
    }
    alert(message = reason) {
        yesButton {
            doPermissionAction(permission, action, doOnRefuse)
        }
        onCancelled { doPermissionAction(permission, action, doOnRefuse) }
    }.show()
}

fun Activity.doPermissionAction(
        permission: String,
        action: (() -> Unit)? = null,
        doOnRefuse: (() -> Unit)? = null
) {
    val rxPermissions = RxPermissions(this@doPermissionAction)
    if (rxPermissions.isGranted(permission)) {
        action?.invoke()
        return
    }
    rxPermissions.request(permission).subscribe { if (it) action?.invoke() else doOnRefuse?.invoke() }
}

fun <T : Parcelable> AppCompatActivity.replaceFragment(fragment: Fragment, vararg argument: Pair<String, T>) {
    if (argument.isNotEmpty()) {
        val bundle = Bundle()
        argument.forEach {
            bundle.putParcelable(it.first, it.second)
        }
        fragment.arguments = bundle
    }
    val fragmentManager = supportFragmentManager
    val transaction = fragmentManager.beginTransaction()
    transaction.replace(R.id.fragmentContainer, fragment)
    transaction.commit()
}