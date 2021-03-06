package com.thelunchbox.bd.dialogs

import android.content.Context
import android.graphics.Point
import android.view.WindowManager
import android.os.Build

/**
 * 作者 : andy
 * 日期 : 15/11/8 20:42
 * 邮箱 : andyxialm@gmail.com
 * 描述 : 换算工具类
 */
object DisplayUtil {
    /**
     * 根据分辨率从 dp 的单位 转成为 px(像素)
     */
    @JvmStatic
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 获得屏幕尺寸
     * @param context
     * @return
     */
    @JvmStatic
    fun getScreenSize(context: Context): Point {
        val point = Point()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            wm.defaultDisplay.getSize(point)
        }
        return point
    }
}