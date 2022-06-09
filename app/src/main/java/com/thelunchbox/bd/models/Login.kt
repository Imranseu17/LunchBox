package com.thelunchbox.bd.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

@JsonIgnoreProperties(ignoreUnknown = true)
class Login {
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null
    @SerializedName("occupation")
    @Expose
    var occupation: String? = null


}