package com.jay86.usedmarket.network.observer

import android.app.ProgressDialog
import android.content.Context
import com.jay86.minibox.network.observer.BaseObserver
import io.reactivex.disposables.Disposable

/**
 * Created by Jay
 */

abstract class ProgressObserver<T>(context: Context, msg: String = "加载中...") : BaseObserver<T>() {
    private val dialog = ProgressDialog(context)

    init {
        dialog.setMessage(msg)
        dialog.setCancelable(false)
    }

    override fun onSubscribe(d: Disposable) {
        super.onSubscribe(d)
        if (!dialog.isShowing) {
            dialog.show()
        }
    }

    override fun onComplete() {
        super.onComplete()
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    override fun onError(e: Throwable) {
        super.onError(e)
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
}