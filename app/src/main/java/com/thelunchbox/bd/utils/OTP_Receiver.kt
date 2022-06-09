package com.thelunchbox.bd.utils


import `in`.aabhasjindal.otptextview.OtpTextView
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.view.View
import androidx.annotation.RequiresApi
import com.airbnb.lottie.LottieAnimationView

class OTP_Receiver : BroadcastReceiver() {
    fun setEditText(editText: OtpTextView?,animation:LottieAnimationView?) {
        Companion.editText = editText
        Companion.animation = animation
    }

    // OnReceive will keep trace when sms is been received in mobile
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onReceive(context: Context, intent: Intent) {
        //message will be holding complete sms that is received
        animation?.visibility = View.GONE
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        for (sms in messages) {
            val msg = sms.messageBody
            // here we are spliting the sms using " : " symbol
            val otp = msg.split(": ").toTypedArray()[1]
            editText!!.setOTP(otp)
        }
    }

    companion object {
        private var editText: OtpTextView? = null
        private var animation:LottieAnimationView? = null
    }
}