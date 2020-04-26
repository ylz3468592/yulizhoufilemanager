package com.dscomm.mit.ga.android.http

import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription



abstract class ApiSubscriberCallBack<T>: Subscriber<T> {
    override fun onComplete() {
    }

    override fun onSubscribe(s: Subscription?) {
        s!!.request(Long.MAX_VALUE)
    }

    override fun onNext(t: T) {
        onSuccess(t)
    }

    override fun onError(t: Throwable?) {
        onFailure(t!!)
    }

    abstract fun onSuccess(t: T)

    abstract fun onFailure(t: Throwable)
}