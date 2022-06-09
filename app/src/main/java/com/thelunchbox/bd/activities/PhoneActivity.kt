package com.thelunchbox.bd.activities

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.thelunchbox.bd.databinding.ActivityPhoneBinding
import com.thelunchbox.bd.utils.DebugLog
import java.util.*


class PhoneActivity : AppCompatActivity() {

    private var currentApiVersion = 0
    private var _binding: ActivityPhoneBinding? = null
    private var REQUEST_CODE = 101
    private lateinit var phoneNo:String
    private lateinit var rands:String
    private lateinit var massage:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _binding = ActivityPhoneBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
        currentApiVersion = Build.VERSION.SDK_INT
        val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            window.decorView.systemUiVisibility = flags
            val decorView = window.decorView
            decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    decorView.systemUiVisibility = flags
                }
            }
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SEND_SMS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.SEND_SMS
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.SEND_SMS),
                    REQUEST_CODE
                )
            }
        }


        _binding!!.next.setOnClickListener {
            val code: String = _binding?.codeEt?.text.toString()
            val phone_number: String = _binding?.phoneEt?.text.toString()
            try {
                if (!phone_number.equals("")) {
                    try {
                        sendSMSMessage()
                        // on below line we are displaying a toast message for message send,
                        Toast.makeText(
                            applicationContext,
                            "Message Sent",
                            Toast.LENGTH_LONG
                        ).show()

                    } catch (e: Exception) {

                        // on catch block we are displaying toast message for error.
                        DebugLog.e("Please enter all the data.." + e.message.toString())
                        Toast.makeText(
                            applicationContext,
                            "Please enter all the data.." + e.message.toString(),
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    val intent = Intent(this, OTPActivity::class.java)
                    intent.putExtra("country_code", code)
                    intent.putExtra("phone_number", phone_number)
                    startActivity(intent)

                } else Toast.makeText(
                    this, "Please give your phone number",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        _binding!!.back.setOnClickListener {
            finish()
        }

    }

    fun getRandomNumberString(): String? {
        // It will generate 6 digit random Number.
        // from 0 to 999999
        val rnd = Random()
        val number: Int = rnd.nextInt(999999)

        // this will convert any number sequence into 6 character.
        return String.format("%06d", number)
    }


    protected fun sendSMSMessage() {
        phoneNo = _binding!!.phoneEt.text.toString().trim()
        rands = getRandomNumberString()!!
        massage = "your verification code is: "+rands
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(phoneNo, null, massage, null, null)
    }


}