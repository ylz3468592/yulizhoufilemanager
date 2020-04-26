package com.ylz.filemanager.util

import android.annotation.SuppressLint
import android.os.Message
import android.util.Log
import android.widget.TextView
import com.dscomm.mit.ga.android.http.ApiSubscriberCallBack
import com.google.gson.JsonObject
import com.ylz.filemanager.listener.DownListener
import com.ylz.filemanager.listener.PopularListener
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody


/**
 * @author yulizhou
 * @description:
 * @date :2020/4/17 11:07
 */
object HttpUtil {

    fun makePopularSub(followable: Flowable<JsonObject>, listener: PopularListener) {
        followable.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : ApiSubscriberCallBack<JsonObject>() {
                @SuppressLint("SetTextI18n")
                override fun onSuccess(t: JsonObject) {
                    listener.listen(t)
                }

                override fun onFailure(t: Throwable) {
                    Log.e("makePopularSub", t.message!!)
                    listener.listen(makeErrorJsonObject(t.message!!))
                }
            })
    }

    fun makeSubDownloadLog(followable: Flowable<ResponseBody>, path: String,downListener: DownListener,view:TextView,byteLength:String) {
        followable.subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : ApiSubscriberCallBack<ResponseBody>() {
                @SuppressLint("SetTextI18n")
                override fun onSuccess(t: ResponseBody) {
                    try {
                        object : Thread() {
                            override fun run() {
                                downListener.startDown(true)
                                FileUtil.writeToLocal(path, t.byteStream(),view,byteLength)
                            }
                        }.start()
                    } catch (e: Exception) {
                        downListener.startDown(false)
                        e.printStackTrace()
                    }
                }

                override fun onFailure(t: Throwable) {
                    downListener.startDown(false)
                    Log.e("makeSubDownloadLog",t.message!!)
                }
            })
    }


    fun makeErrorJsonObject(reason: String): JsonObject {
        val obj = JsonObject()
        obj.addProperty("ret", "ERROR")
        obj.addProperty("data", reason)
        return obj
    }


}