package com.ylz.filemanager

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.google.gson.JsonObject
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.ylz.filemanager.adapter.LogRecycleAdapter
import com.ylz.filemanager.adapter.RecycleViewDivider
import com.ylz.filemanager.listener.DownListener
import com.ylz.filemanager.listener.PopularListener
import com.ylz.filemanager.util.DialogUtil
import com.ylz.filemanager.util.FileUtil
import com.ylz.filemanager.util.HttpUtil
import http.HttpClientMaker
import http.bean.YlzFile
import kotlinx.android.synthetic.main.activity_log_show.*
import pub.devrel.easypermissions.EasyPermissions
import tech.gujin.toast.ToastUtil
import java.io.File
import java.util.*

class MainActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {
    var mAdapter: LogRecycleAdapter? = null
    private var longCV: View? = null


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.e("MainActivity", "当前权限被通过")
    }

    override fun onPermissionsDenied(i: Int, list: List<String>) {
        Log.e("MainActivity", "当前权限被拒绝")
    }

    override fun onRequestPermissionsResult(i: Int, strings: Array<String>, ints: IntArray) {
        Log.e("MainActivity", "当前权限全通过")
    }

    var perms = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
//        ,
//        Manifest.permission.RECORD_AUDIO,
//        Manifest.permission.READ_PHONE_STATE,
//        Manifest.permission.ACCESS_COARSE_LOCATION,
//        Manifest.permission.ACCESS_FINE_LOCATION,
//        Manifest.permission.CAMERA,
//        Manifest.permission.CALL_PHONE
    )

    var mList: MutableList<YlzFile> = ArrayList<YlzFile>() as MutableList<YlzFile>
    var currentDir = "d://"
    var chooseViewList = ArrayList<View>()
    var view: TextView? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_show)
        view = TextView(this@MainActivity)
        if (EasyPermissions.hasPermissions(this, *perms)) {
            println("~~~~~~~~~~~~~~~")
        } else {
            EasyPermissions.requestPermissions(
                this,
                "请务必打开所有必要的权限",
                0,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }
        //1)初始化RecyclerView设置
        x_recycle_list.setPullRefreshEnabled(true)
        x_recycle_list.setLoadingMoreEnabled(true)
        x_recycle_list.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader)
        x_recycle_list.setLoadingMoreProgressStyle(ProgressStyle.BallClipRotate)
        x_recycle_list.addItemDecoration(RecycleViewDivider(this, VERTICAL))
        //2)添加布局管理器
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = VERTICAL
        x_recycle_list.layoutManager = layoutManager
        mAdapter = LogRecycleAdapter(this, mList)
        x_recycle_list.adapter = mAdapter

        mAdapter!!.setOnItemLongClickListener(object : LogRecycleAdapter.LongClickListener {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun onItemLongClick(v: View) {
                //TODO --------------------
                longCV = v
                operation.visibility = View.VISIBLE
                v.setBackgroundColor(getColor(android.R.color.holo_blue_bright))
                chooseViewList.add(v)
            }
        })

        mAdapter!!.setOnItemClickListener(object : LogRecycleAdapter.ClickListener {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun onItemClick(v: View, position: Int) {
                try {
                    //恢复之前的选择状态
                    recoverState()
                    //执行相关操作
                    val tv = v.findViewById(R.id.name) as TextView
                    val tp = v.findViewById(R.id.type) as TextView
                    val size = v.findViewById<TextView>(R.id.size)
                    if (tp.text == "文件夹") {
                        val gp = tv.hint.toString().replace("\\\\".toRegex(), "/")
                        makeHttpRequest(gp, mAdapter!!)
                    } else {
                        val gp = tv.hint.toString().replace("\\\\".toRegex(), "/")
                        val f = File(gp)
                        getFile(gp, f.name, size.text.toString(), size.hint.toString())
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

        //5)实现 下拉刷新和加载更多 接口
        x_recycle_list.setLoadingListener(object : XRecyclerView.LoadingListener {
            override fun onRefresh() {
                ToastUtil.show("-----此功能后续开发-----")
            }

            override fun onLoadMore() {
                ToastUtil.show("----------我也是有底线的------------")
            }
        })
        makeHttpRequest(currentDir, mAdapter!!)
        back.setOnClickListener {
            recoverState()
            //返回上一层
            if (currentDir == "d://" || currentDir == "d:/" || currentDir == "d:") {
            } else {
                val f = File(currentDir)
                if (f.parent != null) {
                    makeHttpRequest(
                        f.parent.replace("\\\\".toRegex(), ""),
                        mAdapter!!
                    )
                } else {
                    ToastUtil.show("已经退到最后一层了！")
                }
            }
        }
    }


    private fun makeHttpRequest(currentPath: String, mAdapter: LogRecycleAdapter) {
        currentDir = currentPath
        val httpService = HttpClientMaker.makePopularClient()
        Log.e("MainActivity", currentDir)
        val followable =
            httpService.getPopular("http://192.168.168.109:10080/message/rest/wjlj?wjlj=$currentPath")
        val userListener: PopularListener = object : PopularListener {
            override fun listen(json: JsonObject) {
                if (json.asJsonObject.get("ret").asString == "ERROR") {
                    ToastUtil.show(json.asJsonObject.get("data").asString)
                    return
                }
                try {
                    val dirs = json.getAsJsonObject("data").getAsJsonArray("dirs")
                    mList.clear()
                    for (i in 0 until dirs.size()) {
                        val yf = YlzFile()
                        yf.abPath = dirs.get(i).asJsonObject.get("abPath").asString
                        yf.name = dirs.get(i).asJsonObject.get("name").asString
                        yf.type = dirs.get(i).asJsonObject.get("type").asString
                        yf.modifyDate = dirs.get(i).asJsonObject.get("modifyDate").asString
                        yf.size = dirs.get(i).asJsonObject.get("size").asString
                        yf.bytesLength = 0
                        mList.add(yf)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                try {
                    val files = json.getAsJsonObject("data").getAsJsonArray("files")
                    for (i in 0 until files.size()) {
                        val yf = YlzFile()
                        yf.abPath = files.get(i).asJsonObject.get("abPath").asString
                        yf.name = files.get(i).asJsonObject.get("name").asString
                        yf.type = files.get(i).asJsonObject.get("type").asString
                        yf.modifyDate = files.get(i).asJsonObject.get("modifyDate").asString
                        yf.size = files.get(i).asJsonObject.get("size").asString
                        yf.bytesLength = files.get(i).asJsonObject.get("bytesLength").asLong
                        mList.add(yf)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                current_Dir.text = "当前目录：$currentDir"
                mAdapter.notifyDataSetChanged()
            }
        }
        HttpUtil.makePopularSub(followable, userListener)
    }


    private fun getFile(path: String, name: String, size: String, byteLength: String) {
        val httpService = HttpClientMaker.makePopularClient()
        val res =
            httpService.downloadFile("http://192.168.168.109:10080/message/rest/downloadPath?wjlj=$path")
        val downListener = object : DownListener {
            override fun startDown(res: Boolean) {
                if (res) {
                    //弹窗开始下载
                    runOnUiThread {
                        view!!.text = FileUtil.fileDownLength
                        DialogUtil.makePopularViewDialog(
                            this@MainActivity,
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
        view = TextView(this@MainActivity)
        HttpUtil.makeSubDownloadLog(res, "/sdcard/AAAAAA/$name", downListener, view!!, byteLength)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBackPressed() {
        if (operation.visibility == View.VISIBLE) {
            recoverState()
        } else {
            back.callOnClick()
        }
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun recoverState() {
        if (operation.visibility == View.VISIBLE) {
            operation.visibility = View.GONE
            chooseViewList.forEach {
                it.background = getDrawable(R.drawable.item_selector)
            }
        }
    }


    override fun onResume() {
        super.onResume()

        delete.setOnClickListener {
            val httpService = HttpClientMaker.makePopularClient()
            val tv = longCV!!.findViewById(R.id.name) as TextView
            val gp = tv.hint.toString().replace("\\\\".toRegex(), "/")
            val followable =
                httpService.getPopular("http://192.168.168.109:10080/message/rest/deletePath?wjlj=$gp")
            val userListener: PopularListener = object : PopularListener {
                override fun listen(json: JsonObject) {
                    if(json.get("ret").asString=="OK"){
                        ToastUtil.show("删除文件成功")
                        val f = File(currentDir)
                        makeHttpRequest(
                            f.path.replace("\\\\".toRegex(), ""),
                            mAdapter!!
                        )
                    }else{
                        ToastUtil.show("删除文件失败"+ json.get("data").asString)
                    }
                }
            }
            HttpUtil.makePopularSub(followable, userListener)
        }
    }
}
