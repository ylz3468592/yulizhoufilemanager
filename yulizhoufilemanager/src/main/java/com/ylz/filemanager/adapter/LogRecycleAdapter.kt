package com.ylz.filemanager.adapter

/**
 * @author yulizhou
 * @description:
 * @date :2020/4/3 10:41
 */

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ylz.filemanager.R
import http.bean.YlzFile


class LogRecycleAdapter(private val context: Context, private val items: List<YlzFile>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var clickListener: ClickListener? = null
    private var longClickListener: LongClickListener? = null


    interface ClickListener {
        fun onItemClick(v: View, position: Int)
    }

    interface  LongClickListener{
        fun onItemLongClick(v: View)
    }

    fun setOnItemClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    fun setOnItemLongClickListener(longClickListener:LongClickListener){
        this.longClickListener = longClickListener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        var viewHolder: RecyclerView.ViewHolder? = null

        when (viewType) {
            0 -> {
                view = LayoutInflater.from(parent.context).inflate(
                    R.layout.item_log_show, parent, false
                )
                viewHolder = ViewHolderOne(view)
            }
        }
        return viewHolder!!
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val newsBean = items[position]
        when (getItemViewType(position)) {
            0 //1张图 情况
            -> {
                val ViewHolderOne = holder as ViewHolderOne
                //                ViewHolderOne.tvTitle.setText(newsBean.getNews_title());
                //                ViewHolderOne.tvCateName.setText(newsBean.getSource_name());
                ViewHolderOne.name.text = newsBean.name
                ViewHolderOne.name.hint = newsBean.abPath
                ViewHolderOne.modify_date.text = newsBean.modifyDate
                ViewHolderOne.type.text = newsBean.type
                ViewHolderOne.size.text = newsBean.size
                ViewHolderOne.size.hint = newsBean.bytesLength.toString()
                if (newsBean.type == "文件") {
                    ViewHolderOne.pic.setImageDrawable(context.getDrawable(R.mipmap.file))
                } else {
                    ViewHolderOne.pic.setImageDrawable(context.getDrawable(R.mipmap.dir))
                }
            }
        }
        holder.itemView.setOnClickListener {
            val pos = holder.layoutPosition
            clickListener!!.onItemClick(holder.itemView, pos)
        }

        holder.itemView.setOnLongClickListener(object :View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                longClickListener!!.onItemLongClick(p0!!)
                return false
            }
        })

    }

    override fun getItemViewType(position: Int): Int {
        return 0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return items.size
    }


    class ViewHolderOne(view: View) : RecyclerView.ViewHolder(view) {
        internal var name: TextView
        internal var modify_date: TextView
        internal var type: TextView
        internal var size: TextView
        internal var pic: ImageView

        init {
            name = view.findViewById(R.id.name)
            modify_date = view.findViewById(R.id.modify_date)
            type = view.findViewById(R.id.type)
            size = view.findViewById(R.id.size)
            pic = view.findViewById(R.id.pic)
        }
    }
}