package com.thelunchbox.bd.utils

import android.util.Log

object DebugLog {
    private const val isDebug = true
    private const val TAG = "StudyLity"
    fun i(value: String) {
        if (isDebug) Log.i(TAG, value + "")
    }

    fun d(value: String) {
        if (isDebug) Log.d(TAG, value + "")
    }

    fun e(value: String) {
        if (isDebug) Log.e(TAG, value + "")
    }

    fun v(value: String) {
        if (isDebug) Log.v(TAG, value + "")
    }
}