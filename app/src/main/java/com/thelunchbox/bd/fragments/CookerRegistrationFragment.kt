package com.thelunchbox.bd.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.thelunchbox.bd.R
import com.thelunchbox.bd.activities.LoginActivity
import com.thelunchbox.bd.callback.RegistrationView
import com.thelunchbox.bd.databinding.FragmentBuyerRegistrationBinding
import com.thelunchbox.bd.databinding.FragmentCookerRegistrationBinding
import com.thelunchbox.bd.dialogs.CustomAlertDialog
import com.thelunchbox.bd.models.Registration
import com.thelunchbox.bd.presenter.RegistrationPresenter
import com.thelunchbox.bd.utils.DebugLog


class CookerRegistrationFragment : Fragment(),RegistrationView {
    private var _binding: FragmentCookerRegistrationBinding? = null
    private var registrationPresenter:RegistrationPresenter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCookerRegistrationBinding.inflate(inflater, container, false)

        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registrationPresenter = RegistrationPresenter(this)
        _binding!!.rectangleSubmit.setOnClickListener{
            submitRegistration()
        }
    }

    private fun validateEmail(): Boolean {
        val email: String = _binding!!.emailEt.getText().toString().trim()
        if (!isValidEmail(email)) {
            _binding!!.emailEt.setError(getString(R.string.err_msg_email_required))
            requestFocus(_binding!!.emailEt)
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

    private fun validateName(): Boolean {
        val name: String = _binding!!.nameEt.getText().toString().trim()
        if (name.isEmpty()) {
            _binding!!.nameEt.setError(getString(R.string.err_msg_password))
            requestFocus(_binding!!.nameEt)
            return false
        }
        return true
    }

    private fun validateOccupation(): Boolean {
        val occupation: String = _binding!!.occupationEt.getText().toString().trim()
        if (occupation.isEmpty()) {
            _binding!!.occupationEt.setError(getString(R.string.err_msg_password))
            requestFocus(_binding!!.occupationEt)
            return false
        }
        return true
    }

    private fun requestFocus(view: View) {
        if (view.requestFocus()) {
            activity?. getWindow()?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
        }
    }
    fun isValidEmail(target: CharSequence?): Boolean {
        return if (TextUtils.isEmpty(target)) {
            false
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                Patterns.EMAIL_ADDRESS.matcher(target).matches()
            } else {
                TODO("VERSION.SDK_INT < FROYO")
            }
        }
    }

    private fun submitRegistration() {
        if (!validateEmail()) {
            return
        }
        if (!validateName()) {
            return
        }
        if (!validateOccupation()) {
            return
        }
        if (!validatePassword()) {
            return
        }
        if (!validateConfirmPassword()) {
            return
        }

        hideKeyboard(requireActivity())
         getRegistration()
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun checkConnection(): Boolean {
        val cm = activity?. getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo != null
    }

    private fun getRegistration() {
        if (checkConnection()) {
            val phone:String = activity?.intent?.getStringExtra("phone_number")!!
            DebugLog.e(phone)
            val email: String = _binding!!.emailEt.getText().toString().trim()
            val name: String = _binding!!.nameEt.getText().toString().trim()
            val occupation: String = _binding!!.occupationEt.getText().toString().trim()
            val password: String = _binding!!.passwordEt.getText().toString().trim()
            registrationPresenter?.attemptRegistration(email,password,name,phone,occupation)
        } else CustomAlertDialog.showError(requireContext(), getString(R.string.no_internet_connection))
    }

    override fun onSuccess(registration: Registration?, code: Int) {
        CustomAlertDialog.showSuccess(requireContext(),
            "Registration Successfully this number: "+registration?.phone)
        startActivity(Intent(requireContext(),LoginActivity::class.java))
    }

    override fun onError(error: String?, code: Int) {
        CustomAlertDialog.showError(requireContext(),error)
    }


}