package com.jay86.minibox.utils.extension

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View

/**
 * Created by Jay
 */
fun View.snackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.snackbar(@StringRes messageId: Int) {
    snackbar(resources.getString(messageId))
}

fun View.longSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.longSnackbar(@StringRes messageId: Int) {
    snackbar(resources.getString(messageId))
}