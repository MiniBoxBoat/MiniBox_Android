package com.jay86.usedmarket.network.observer

import android.app.ProgressDialog
import android.content.Context
import com.jay86.minibox.network.observer.BaseObserver
import io.reactivex.disposables.Disposable

/**
 * Created by Jay on 2017/10/10.
 */

abstract class ProgressObserver<T>(context: Context, msg: String = "加载中...") : BaseObserver<T>() {
    private val dialog = ProgressDialog(context)

    init {
        dialog.setMessage(msg)
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
}