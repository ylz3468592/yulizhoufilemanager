package com.ylz.filemanager.util

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.TextView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

/**
 * @author yulizhou
 * @description:
 * @date :2020/4/17 13:36
 */
@SuppressLint("StaticFieldLeak")
object FileUtil {

    var fileDownLength = "0B"
    var tv :TextView? =null

    @Throws(IOException::class)
    fun writeToLocal(destination: String, input: InputStream,view:TextView,byteLength: String) {
        this.tv = view
        val file = File(destination)
        if(!file.parentFile.exists()){
            file.parentFile.mkdirs()
        }
        val byteSize = 1024
        var readSize = 0L
        var index: Int
        val bytes = ByteArray(1024)
        val downloadFile = FileOutputStream(destination)
        index = input.read(bytes)
        readSize += index
        while (index != -1) {
            downloadFile.write(bytes, 0, index)
            downloadFile.flush()
            index = input.read(bytes)
            if(index!=-1){
                readSize += index
            }
//            ToastUtil.postShow(""+readSize+"/"+input.available())
            fileDownLength = getPrintSize(readSize)
            val msg = Message.obtain()
            msg.obj = "当前下载进度$readSize/$byteLength"
            handler.handleMessage(msg)
        }
        input.close()
        downloadFile.close()
    }


    fun getPrintSize(size: Long): String {
        var size = size
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return size.toString() + "B"
        } else {
            size /= 1024
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (size < 1024) {
            return size.toString() + "KB"
        } else {
            size /= 1024
        }
        if (size < 1024) {
            // 因为如果以MB为单位的话，要保留最后1位小数，
            // 因此，把此数乘以100之后再取余
            size *= 100
            return (size / 100).toString() + "." + (size % 100).toString() + "MB"
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024
            return (size / 100).toString() + "." + (size % 100).toString() + "GB"
        }
    }

    var handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            tv!!.text = (msg.obj as CharSequence?).toString()
        }
    }


}