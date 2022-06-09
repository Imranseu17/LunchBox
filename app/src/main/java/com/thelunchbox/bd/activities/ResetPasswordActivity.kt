package com.thelunchbox.bd.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import com.thelunchbox.bd.R
import com.thelunchbox.bd.databinding.ActivityLoginBinding
import com.thelunchbox.bd.databinding.ActivityResetPasswordBinding
import com.thelunchbox.bd.dialogs.CustomAlertDialog
import com.thelunchbox.bd.utils.DebugLog
import kotlinx.android.synthetic.main.activity_phone.*

class ResetPasswordActivity : AppCompatActivity() {
    private var currentApiVersion = 0
    private var _binding: ActivityResetPasswordBinding? = null
    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _binding = ActivityResetPasswordBinding.inflate(layoutInflater)
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
            decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    decorView.systemUiVisibility = flags
                }
            }
        }
        _binding!!.rectangleSubmit.setOnClickListener{
           submitResetPassword()
        }

        _binding!!.back.setOnClickListener{
            finish()
        }
    }
    private fun validatePassword(): Boolean {
        val password: String = _binding!!.passwordEt.getText().toString().trim()
        if (password.isEmpty()) {
            _binding!!.passwordEt.setError(getString(R.string.err_msg_password))
            requestFocus(_binding!!.passwordEt)
            return false
        } else if (password.length < 6) {
            _binding!!.passwordEt.setError(getString(R.string.err_msg_password_length))
            requestFocus(_binding!!.passwordEt)
            return false
        }
        return true
    }

    private fun validateConfirmPassword(): Boolean {
        val confirmPassword: String = _binding!!.confirmPasswordEt.getText().toString().trim()
        if (confirmPassword.isEmpty()) {
            _binding!!.confirmPasswordEt.setError(getString(R.string.err_msg_password))
            requestFocus(_binding!!.confirmPasswordEt)
            return false
        } else if (confirmPassword.length < 6) {
            _binding!!.confirmPasswordEt.setError(getString(R.string.err_msg_password_length))
            requestFocus(_binding!!.confirmPasswordEt)
            return false
        }
        else if (!confirmPassword.equals(_binding!!.passwordEt.text.toString().trim())) {
            _binding!!.confirmPasswordEt.setError(getString(R.string.err_msg_confirm_password_length))
            requestFocus(_binding!!.confirmPasswordEt)
            return false
        }
        return true
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun submitResetPassword() {

        if (!validatePassword()) {
            return
        }
        if (!validateConfirmPassword()) {
            return
        }

        hideKeyboard(this)
        getResetPassword()
    }

    private fun getResetPassword() {
        if (checkConnection()) {
            val phone:String = intent?.getStringExtra("phone_number")!!
            DebugLog.e(phone)
            val password: String = _binding!!.passwordEt.getText().toString().trim()
        } else CustomAlertDialog.showError(this, getString(R.string.no_internet_connection))
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun checkConnection(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }



}