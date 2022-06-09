package com.thelunchbox.bd.callback

import com.thelunchbox.bd.models.Registration


interface RegistrationView {
    fun onSuccess(registration: Registration?, code: Int)
    fun onError(error: String?, code: Int)
}