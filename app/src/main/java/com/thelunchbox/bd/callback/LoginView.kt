package com.thelunchbox.bd.callback

import com.thelunchbox.bd.models.Login

interface LoginView {
    fun onSuccess(login: Login?, code: Int)
    fun onError(error: String?, code: Int)
}