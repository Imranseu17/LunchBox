package com.thelunchbox.bd.activities

import android.app.Activity
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.thelunchbox.bd.R
import com.thelunchbox.bd.callback.LoginView
import com.thelunchbox.bd.databinding.ActivityLoginBinding
import com.thelunchbox.bd.dialogs.CustomAlertDialog
import com.thelunchbox.bd.models.Login
import com.thelunchbox.bd.presenter.LoginPresenter
import com.thelunchbox.bd.utils.Constants

class LoginActivity : AppCompatActivity(),LoginView {
    private var currentApiVersion = 0
    private var _binding: ActivityLoginBinding? = null
    private var loginPresenter:LoginPresenter?= null
    @RequiresApi(Build.VERSION_CODES.HONEYCOMB)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        _binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)
        loginPresenter = LoginPresenter(this)
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

        _binding!!.rectangleLogin.setOnClickListener {
            submitLogin()
        }
        _binding!!.createAccountLink.setOnClickListener{
            Constants.gotoPhoneActivity = 1
            startActivity(Intent(this,PhoneActivity::class.java))
        }
        _binding!!.forgotPass.setOnClickListener{
            Constants.gotoPhoneActivity = 2
            startActivity(Intent(this,PhoneActivity::class.java))
        }
    }

    private fun validateEmail(): Boolean {
        val phone: String = _binding!!.phoneEt.getText().toString().trim()
        if (phone.isEmpty()) {
            _binding!!.phoneEt.setError(getString(R.string.err_msg_phone_length))
            requestFocus(_binding!!.phoneEt)
            return false
        }
        else
            return true
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

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }

    private fun submitLogin() {
        if (!validateEmail()) {
            return
        }
        if (!validatePassword()) {
            return
        }

     hideKeyboard(this)
       getLogin()
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

    override fun onSuccess(login: Login?, code: Int) {
       CustomAlertDialog.showSuccess(this,"login success: "+login?.occupation)
        startActivity(Intent(this,MainActivity::class.java))
    }

    override fun onError(error: String?, code: Int) {
       CustomAlertDialog.showError(this,error)
    }

    private fun getLogin() {
        if (checkConnection()) {
            val phone: String = _binding!!.phoneEt.getText().toString().trim()
            val password: String = _binding!!.passwordEt.getText().toString().trim()
            loginPresenter?.attemptLogin(phone, password)
        } else CustomAlertDialog.showError(this, getString(R.string.no_internet_connection))
    }
    private fun checkConnection(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }


}