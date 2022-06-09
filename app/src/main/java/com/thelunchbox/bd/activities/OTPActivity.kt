package com.thelunchbox.bd.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isEmpty
import androidx.lifecycle.ViewModelProvider
import com.thelunchbox.bd.R
import com.thelunchbox.bd.databinding.ActivityOtpBinding
import com.thelunchbox.bd.dialogs.CustomAlertDialog
import com.thelunchbox.bd.utils.Constants
import com.thelunchbox.bd.utils.OTP_Receiver
import kotlinx.android.synthetic.main.activity_otp.*

class OTPActivity : AppCompatActivity() {
    private var _binding:ActivityOtpBinding? = null
    private var currentApiVersion = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        _binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
        currentApiVersion = Build.VERSION.SDK_INT
        val flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                window.decorView.systemUiVisibility = flags
            }
            val decorView = window.decorView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                    if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                        decorView.systemUiVisibility = flags
                    }
                }
            }
        }
        //we need to ask user permission to auto read sms
        requestsmspermission()
        OTP_Receiver().setEditText(_binding!!.otpView,_binding!!.loadingView)

        _binding!!.back.setOnClickListener {
            finish()
        }

        _binding!!.verify.setOnClickListener{
            if(!otp_view.isEmpty()){
                if(Constants.gotoPhoneActivity == 1){
                    val intent = Intent(this,RegistrationActivity::class.java)
                    intent.putExtra("phone_number",getIntent().getStringExtra("phone_number"))
                    startActivity(intent)
                }
                else{
                    val intent = Intent(this,ResetPasswordActivity::class.java)
                    intent.putExtra("phone_number",getIntent().getStringExtra("phone_number"))
                    startActivity(intent)
                }

            }else{
                CustomAlertDialog.showError(this,"Please give input your otp verification code")
            }

        }
        _binding!!.resentNew.setOnClickListener{
            startActivity(Intent(this,PhoneActivity::class.java))
        }
    }


    private fun requestsmspermission() {
        val smspermission: String = Manifest.permission.RECEIVE_SMS
        val grant = ContextCompat.checkSelfPermission(this, smspermission)
        //check if read SMS permission is granted or not
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permission_list = arrayOfNulls<String>(1)
            permission_list[0] = smspermission
            ActivityCompat.requestPermissions(this, permission_list, 1)
        }
    }

}