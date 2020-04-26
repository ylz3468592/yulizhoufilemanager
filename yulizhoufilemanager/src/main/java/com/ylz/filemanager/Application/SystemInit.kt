package com.ylz.filemanager.Application

import android.content.Context
import tech.gujin.toast.ToastUtil

/**
 * @author yulizhou
 * @description:
 * @date :2020/4/17 13:20
 */
class SystemInit {
    fun init(context: Context) {
        ToastUtil.initialize(context)
    }
}