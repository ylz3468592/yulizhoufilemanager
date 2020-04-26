package com.ylz.filemanager.listener

import com.google.gson.JsonObject

/**
 * @author yulizhou
 * @description:
 * @date :2020/4/17 11:01
 */
interface PopularListener {
    fun listen(json:JsonObject)
}