package com.ylz.filemanager

import android.app.Activity
import android.util.Log
import android.widget.TextView
import com.google.gson.JsonObject
import com.ylz.filemanager.listener.DownListener
import com.ylz.filemanager.listener.PopularListener
import com.ylz.filemanager.util.DialogUtil
import com.ylz.filemanager.util.FileUtil
import com.ylz.filemanager.util.HttpUtil
import http.HttpClientMaker
import http.bean.YlzFile
import tech.gujin.toast.ToastUtil

/**
 * @author yulizhou
 * @description:
 * @date :2020/4/26 14:14
 */
class DownloadFile(val context: Activity, val url: String, path: String) {
    var view: TextView? = null

    private fun makeHttpRequest(currentPath: String) {
        val httpService = HttpClientMaker.makePopularClient()
        Log.e("DownloadFile", url)
        val followable =
            httpService.getPopular(url)
        val userListener: PopularListener = object : PopularListener {
            override fun listen(json: JsonObject) {
                if (json.asJsonObject.get("ret").asString == "ERROR") {
                    ToastUtil.show(json.asJsonObject.get("data").asString)
                    return
                }
                try {
                    val files = json.getAsJsonObject("data").getAsJsonArray("files")
                    if (files.size() > 1) {
                        ToastUtil.show("下载文件超过1，忽略---------")
                    } else {
                        val yf = YlzFile()
                        yf.abPath = files.get(0).asJsonObject.get("abPath").asString
                        yf.name = files.get(0).asJsonObject.get("name").asString
                        yf.type = files.get(0).asJsonObject.get("type").asString
                        yf.modifyDate = files.get(0).asJsonObject.get("modifyDate").asString
                        yf.size = files.get(0).asJsonObject.get("size").asString
                        yf.bytesLength = files.get(0).asJsonObject.get("bytesLength").asLong
                        getFile(yf.abPath,yf.name, yf.size, yf.bytesLength.toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        HttpUtil.makePopularSub(followable, userListener)
    }


    private fun getFile(path: String, name: String, size: String, byteLength: String) {
        val httpService = HttpClientMaker.makePopularClient()
        val res =
            httpService.downloadFile(url)
        val downListener = object : DownListener {
            override fun startDown(res: Boolean) {
                if (res) {
                    //弹窗开始下载
                    context.runOnUiThread {
                        view!!.text = FileUtil.fileDownLength
                        DialogUtil.makePopularViewDialog(
                            context,
                            "当前文件大小$size",
                            {},
                            {},
                            view!!,
                            "前往下载目录",
                            "确认"
                        )
                    }
                } else {
                    //弹窗提示下载失败
                }
            }
        }
        view = TextView(context)
        HttpUtil.makeSubDownloadLog(res, "$path/$name", downListener, view!!, byteLength)
    }

}