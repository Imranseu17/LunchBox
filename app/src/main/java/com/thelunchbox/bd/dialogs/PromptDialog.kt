package com.thelunchbox.bd.dialogs

import android.app.Dialog
import android.content.Context
import com.thelunchbox.bd.dialogs.AnimationLoader.getInAnimation
import com.thelunchbox.bd.dialogs.AnimationLoader.getOutAnimation
import com.thelunchbox.bd.dialogs.DisplayUtil.dp2px
import com.thelunchbox.bd.dialogs.DisplayUtil.getScreenSize
import kotlin.jvm.JvmOverloads
import com.thelunchbox.bd.R
import android.view.animation.AnimationSet
import android.widget.TextView
import com.thelunchbox.bd.dialogs.PromptDialog.OnPositiveListener
import com.thelunchbox.bd.dialogs.AnimationLoader
import androidx.annotation.RequiresApi
import android.os.Build
import android.os.Bundle
import android.widget.LinearLayout
import android.view.ViewGroup
import com.thelunchbox.bd.dialogs.DisplayUtil
import com.thelunchbox.bd.dialogs.PromptDialog
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.ShapeDrawable
import android.view.WindowManager
import android.view.animation.Animation
import android.content.res.ColorStateList
import android.graphics.*
import android.view.View
import android.widget.ImageView

/**
 * 作者 : andy
 * 日期 : 15/11/10 11:28
 * 邮箱 : andyxialm@gmail.com
 * 描述 : 提示性的Dialog
 */
class PromptDialog @JvmOverloads constructor(context: Context?, theme: Int = 0) : Dialog(
    context!!, R.style.color_dialog
) {
    private var mAnimIn: AnimationSet? = null
    private var mAnimOut: AnimationSet? = null
    private var mDialogView: View? = null
    var titleTextView: TextView? = null
        private set
    var contentTextView: TextView? = null
        private set
    private var mPositiveBtn: TextView? = null
    private var mOnPositiveListener: OnPositiveListener? = null
    var dialogType = 0
        private set
    private var mIsShowAnim = false
    private var mTitle: CharSequence? = null
    private var mContent: CharSequence? = null
    private var mBtnText: CharSequence? = null
    private fun init() {
        mAnimIn = getInAnimation(context)
        mAnimOut = getOutAnimation(context)
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle) {
        super.onCreate(savedInstanceState)
        initView()
        initListener()
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private fun initView() {
        val contentView = View.inflate(context, R.layout.layout_promptdialog, null)
        setContentView(contentView)
        resizeDialog()
        mDialogView = window!!.decorView.findViewById(android.R.id.content)
        titleTextView = contentView.findViewById<View>(R.id.tvTitle) as TextView
        contentTextView = contentView.findViewById<View>(R.id.tvContent) as TextView
        mPositiveBtn = contentView.findViewById<View>(R.id.btnPositive) as TextView
        val llBtnGroup = findViewById<View>(R.id.llBtnGroup)
        val logoIv = contentView.findViewById<View>(R.id.logoIv) as ImageView
        logoIv.setBackgroundResource(getLogoResId(dialogType))
        val topLayout = contentView.findViewById<View>(R.id.topLayout) as LinearLayout
        val triangleIv = ImageView(context)
        triangleIv.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            dp2px(context, 10f)
        )
        triangleIv.setImageBitmap(
            createTriangel(
                (getScreenSize(context).x * 0.7).toInt(), dp2px(
                    context, 10f
                )
            )
        )
        topLayout.addView(triangleIv)
        setBtnBackground(mPositiveBtn)
        setBottomCorners(llBtnGroup)
        val radius = dp2px(context, DEFAULT_RADIUS.toFloat())
        val outerRadii = floatArrayOf(
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            0f,
            0f,
            0f,
            0f
        )
        val roundRectShape = RoundRectShape(outerRadii, null, null)
        val shapeDrawable = ShapeDrawable(roundRectShape)
        shapeDrawable.paint.style = Paint.Style.FILL
        shapeDrawable.paint.color = context.resources.getColor(getColorResId(dialogType))
        val llTop = findViewById<View>(R.id.llTop) as LinearLayout
        llTop.background = shapeDrawable
        titleTextView!!.text = mTitle
        contentTextView!!.text = mContent
        mPositiveBtn!!.text = mBtnText
    }

    private fun resizeDialog() {
        val params = window!!.attributes
        params.width = (getScreenSize(context).x * 0.7).toInt()
        window!!.attributes = params
    }

    override fun onStart() {
        super.onStart()
        startWithAnimation(mIsShowAnim)
    }

    override fun dismiss() {
        dismissWithAnimation(mIsShowAnim)
    }

    private fun startWithAnimation(showInAnimation: Boolean) {
        if (showInAnimation) {
            mDialogView!!.startAnimation(mAnimIn)
        }
    }

    private fun dismissWithAnimation(showOutAnimation: Boolean) {
        if (showOutAnimation) {
            mDialogView!!.startAnimation(mAnimOut)
        } else {
            super.dismiss()
        }
    }

    private fun getLogoResId(mDialogType: Int): Int {
        if (DIALOG_TYPE_DEFAULT == mDialogType) {
            return R.mipmap.ic_info
        }
        if (DIALOG_TYPE_INFO == mDialogType) {
            return R.mipmap.ic_info
        }
        if (DIALOG_TYPE_HELP == mDialogType) {
            return R.mipmap.ic_help
        }
        if (DIALOG_TYPE_WRONG == mDialogType) {
            return R.mipmap.ic_wrong
        }
        if (DIALOG_TYPE_SUCCESS == mDialogType) {
            return R.mipmap.ic_success
        }
        return if (DIALOG_TYPE_WARNING == mDialogType) {
            R.mipmap.icon_warning
        } else R.mipmap.ic_info
    }

    private fun getColorResId(mDialogType: Int): Int {
        if (DIALOG_TYPE_DEFAULT == mDialogType) {
            return R.color.color_type_info
        }
        if (DIALOG_TYPE_INFO == mDialogType) {
            return R.color.color_type_info
        }
        if (DIALOG_TYPE_HELP == mDialogType) {
            return R.color.color_type_help
        }
        if (DIALOG_TYPE_WRONG == mDialogType) {
            return R.color.color_type_wrong
        }
        if (DIALOG_TYPE_SUCCESS == mDialogType) {
            return R.color.color_type_success
        }
        return if (DIALOG_TYPE_WARNING == mDialogType) {
            R.color.color_type_warning
        } else R.color.color_type_info
    }

    private fun getSelBtn(mDialogType: Int): Int {
        if (DIALOG_TYPE_DEFAULT == mDialogType) {
            return R.drawable.sel_btn
        }
        if (DIALOG_TYPE_INFO == mDialogType) {
            return R.drawable.sel_btn_info
        }
        if (DIALOG_TYPE_HELP == mDialogType) {
            return R.drawable.sel_btn_help
        }
        if (DIALOG_TYPE_WRONG == mDialogType) {
            return R.drawable.sel_btn_wrong
        }
        if (DIALOG_TYPE_SUCCESS == mDialogType) {
            return R.drawable.sel_btn_success
        }
        return if (DIALOG_TYPE_WARNING == mDialogType) {
            R.drawable.sel_btn_warning
        } else R.drawable.sel_btn
    }

    private fun initAnimListener() {
        mAnimOut!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                mDialogView!!.post { callDismiss() }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    private fun initListener() {
        mPositiveBtn!!.setOnClickListener {
            if (mOnPositiveListener != null) {
                mOnPositiveListener!!.onClick(this@PromptDialog)
            }
        }
        initAnimListener()
    }

    private fun callDismiss() {
        super.dismiss()
    }

    private fun createTriangel(width: Int, height: Int): Bitmap? {
        return if (width <= 0 || height <= 0) {
            null
        } else getBitmap(
            width,
            height,
            context.resources.getColor(getColorResId(dialogType))
        )
    }

    private fun getBitmap(width: Int, height: Int, backgroundColor: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = backgroundColor
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(width.toFloat(), 0f)
        path.lineTo((width / 2).toFloat(), height.toFloat())
        path.close()
        canvas.drawPath(path, paint)
        return bitmap
    }

    private fun setBtnBackground(btnOk: TextView?) {
        btnOk!!.setTextColor(
            createColorStateList(
                context.resources.getColor(
                    getColorResId(
                        dialogType
                    )
                ),
                context.resources.getColor(R.color.color_dialog_gray)
            )
        )
        btnOk.setBackgroundDrawable(context.resources.getDrawable(getSelBtn(dialogType)))
    }

    private fun setBottomCorners(llBtnGroup: View) {
        val radius = dp2px(context, DEFAULT_RADIUS.toFloat())
        val outerRadii = floatArrayOf(
            0f,
            0f,
            0f,
            0f,
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat(),
            radius.toFloat()
        )
        val roundRectShape = RoundRectShape(outerRadii, null, null)
        val shapeDrawable = ShapeDrawable(roundRectShape)
        shapeDrawable.paint.color = Color.WHITE
        shapeDrawable.paint.style = Paint.Style.FILL
        llBtnGroup.setBackgroundDrawable(shapeDrawable)
    }

    private fun createColorStateList(
        normal: Int,
        pressed: Int,
        focused: Int = Color.BLACK,
        unable: Int = Color.BLACK
    ): ColorStateList {
        val colors = intArrayOf(pressed, focused, normal, focused, unable, normal)
        val states = arrayOfNulls<IntArray>(6)
        states[0] = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
        states[1] = intArrayOf(android.R.attr.state_enabled, android.R.attr.state_focused)
        states[2] = intArrayOf(android.R.attr.state_enabled)
        states[3] = intArrayOf(android.R.attr.state_focused)
        states[4] = intArrayOf(android.R.attr.state_window_focused)
        states[5] = intArrayOf()
        return ColorStateList(states, colors)
    }

    fun setAnimationEnable(enable: Boolean): PromptDialog {
        mIsShowAnim = enable
        return this
    }

    fun setTitleText(title: CharSequence?): PromptDialog {
        mTitle = title
        return this
    }

    fun setTitleText(resId: Int): PromptDialog {
        return setTitleText(context.getString(resId))
    }

    fun setContentText(content: CharSequence?): PromptDialog {
        mContent = content
        return this
    }

    fun setContentText(resId: Int): PromptDialog {
        return setContentText(context.getString(resId))
    }

    fun setDialogType(type: Int): PromptDialog {
        dialogType = type
        return this
    }

    fun setPositiveListener(btnText: CharSequence?, l: OnPositiveListener?): PromptDialog {
        mBtnText = btnText
        return setPositiveListener(l)
    }

    fun setPositiveListener(stringResId: Int, l: OnPositiveListener?): PromptDialog {
        return setPositiveListener(context.getString(stringResId), l)
    }

    fun setPositiveListener(l: OnPositiveListener?): PromptDialog {
        mOnPositiveListener = l
        return this
    }

    fun setAnimationIn(animIn: AnimationSet?): PromptDialog {
        mAnimIn = animIn
        return this
    }

    fun setAnimationOut(animOut: AnimationSet?): PromptDialog {
        mAnimOut = animOut
        initAnimListener()
        return this
    }




    interface OnPositiveListener {
        fun onClick(dialog: PromptDialog?)
    }

    companion object {
        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val DEFAULT_RADIUS = 6
        const val DIALOG_TYPE_INFO = 0
        const val DIALOG_TYPE_HELP = 1
        const val DIALOG_TYPE_WRONG = 2
        const val DIALOG_TYPE_SUCCESS = 3
        const val DIALOG_TYPE_WARNING = 4
        const val DIALOG_TYPE_DEFAULT = DIALOG_TYPE_INFO
    }

    init {
        init()
    }
}