package com.ylz.filemanager.util

import android.content.Context
import android.graphics.Color
import android.view.View
import com.ylz.filemanager.R
import com.ylz.filemanager.dialog.Effectstype
import com.ylz.filemanager.dialog.NiftyDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

/**
 * @author yulizhou
 * @description:
 * @date :2020/4/20 10:05
 */
object DialogUtil {
    fun makePopularViewDialog(
        context: Context,
        title: String,
        okListener: ()->Unit,
        cancelListener: ()->Unit,
        view: View,
        tx1: String,
        tx2: String
    ): NiftyDialogBuilder? {
        val dialogBuilder = NiftyDialogBuilder.getInstance(context)
        dialogBuilder.getmMessage().visibility = View.GONE
        dialogBuilder
            .withTitle(title)                                  //.withTitle(null)  no title
            .withTitleColor("#FFFFFF")                                  //def
            .withDividerColor("#11000000")                              //def             //def  | withMessageColor(int resid)
            .withDialogColor("#FFFFFF")                               //def  | withDialogColor(int resid)
            .withIcon(context.resources.getDrawable(R.mipmap.jinghui))
            .withDuration(700)                                          //def
            .withEffect(makeEffect())                                         //def Effectstype.Slidetop
            .withButton1Text(tx1)                                      //def gone
            .withButton2Text(tx2)                                  //def gone //def    | isCancelable(true)
            .withMessage("")
            .isCancelableOnTouchOutside(false)
            .withMessageColor(Color.GRAY)
            .setCustomView(view, context)
            .setButton1Click {
                dialogBuilder.cancel()
                okListener()
            }
            .setButton2Click {
                dialogBuilder.cancel()
                cancelListener()
            }
            .show()
        return dialogBuilder
    }

    var timer = Timer()

    private fun makeEffect(): Effectstype {
        val list = ArrayList<Effectstype>()
        list.add(Effectstype.Fadein)
        list.add(Effectstype.Slideleft)
        list.add(Effectstype.Slidetop)
        list.add(Effectstype.SlideBottom)
        list.add(Effectstype.Slideright)
        list.add(Effectstype.Fall)
        list.add(Effectstype.Newspager)
        list.add(Effectstype.Fliph)
        list.add(Effectstype.Flipv)
        list.add(Effectstype.RotateBottom)
        list.add(Effectstype.RotateLeft)
        list.add(Effectstype.Fliph)
        list.add(Effectstype.Slit)
        list.add(Effectstype.Shake)
        list.add(Effectstype.Sidefill)
        return list[(0 until list.size).random()]
    }
}