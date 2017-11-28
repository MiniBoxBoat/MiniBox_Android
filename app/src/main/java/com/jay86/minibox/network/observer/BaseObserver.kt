package com.jay86.minibox.network.observer

import com.jay86.minibox.utils.LogUtils
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

/**
 * Created by Jay on 2017/10/10.
 */

open class BaseObserver<T> : Observer<T> {
    override fun onSubscribe(d: Disposable) = Unit

    override fun onNext(_object: T) = Unit

    override fun onComplete() = Unit

    override fun onError(e: Throwable) {
        LogUtils.e("", e.message.orEmpty(), e)
    }
}