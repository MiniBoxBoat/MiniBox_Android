package com.jay86.minibox.utils.extension

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Created by Jay
 */
fun View?.showKeyBoard() {
    if (this == null) return
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
}

fun View?.hideKeyBoard() {
    if (this == null) return
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(windowToken, 0)
}

fun View?.autoHideKeyBoard(event: MotionEvent?) {
    if (isShouldHideInput(this, event)) hideKeyBoard()
}

private fun isShouldHideInput(v: View?, event: MotionEvent?): Boolean {
    if (event != null && v != null && v is EditText) {
        val leftTop = intArrayOf(0, 0)
        v.getLocationInWindow(leftTop)
        val left = leftTop[0]
        val top = leftTop[1]
        val bottom = top + v.height
        val right = left + v.width
        return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
    }
    return false
}