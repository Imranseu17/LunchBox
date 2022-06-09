package com.thelunchbox.bd.dialogs

import android.content.Context
import android.widget.Toast
import com.thelunchbox.bd.R
import com.thelunchbox.bd.dialogs.PromptDialog.OnPositiveListener

object CustomAlertDialog {
    fun showSuccess(context: Context, msg: String?) {
        try {
            PromptDialog(context)
                .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                .setAnimationEnable(true)
                .setTitleText(context.getString(R.string.success))
                .setContentText(msg)
                .setPositiveListener(context.getString(R.string.ok), object : OnPositiveListener {
                    override fun onClick(dialog: PromptDialog?) {
                       dialog?.dismiss()
                    }
                }).show()
        } catch (e: Exception) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun showWarning(context: Context, msg: String?) {
        try {
            PromptDialog(context)
                .setDialogType(PromptDialog.DIALOG_TYPE_WARNING)
                .setAnimationEnable(true)
                .setTitleText(context.getString(R.string.warning))
                .setContentText(msg)
                .setPositiveListener(context.getString(R.string.ok), object : OnPositiveListener {
                    override fun onClick(dialog: PromptDialog?) {
                        TODO("Not yet implemented")
                    }
                }).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun showInfo(context: Context, msg: String?) {
        try {
            PromptDialog(context)
                .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
                .setAnimationEnable(true)
                .setTitleText(context.getString(R.string.info))
                .setContentText(msg)
                .setPositiveListener(context.getString(R.string.ok), object : OnPositiveListener {


                    override fun onClick(dialog: PromptDialog?) {
                        dialog?.dismiss()
                    }
                }).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun showHelp(context: Context, msg: String?) {
        try {
            PromptDialog(context)
                .setDialogType(PromptDialog.DIALOG_TYPE_HELP)
                .setAnimationEnable(true)
                .setTitleText(context.getString(R.string.help))
                .setContentText(msg)
                .setPositiveListener(context.getString(R.string.ok), object : OnPositiveListener {

                    override fun onClick(dialog: PromptDialog?) {
                        dialog?.dismiss()
                    }
                }).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }

    fun showError(context: Context, msg: String?) {
        try {
            PromptDialog(context)
                .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                .setAnimationEnable(true)
                .setTitleText(context.getString(R.string.err))
                .setContentText(msg)
                .setPositiveListener(context.getString(R.string.ok), object : OnPositiveListener {

                    override fun onClick(dialog: PromptDialog?) {
                        dialog?.dismiss()
                    }
                }).show()
        } catch (e: java.lang.Exception) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}