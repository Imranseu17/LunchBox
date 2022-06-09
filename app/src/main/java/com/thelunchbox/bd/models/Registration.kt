package com.thelunchbox.bd.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@JsonIgnoreProperties(ignoreUnknown = true)
class Registration {
    @SerializedName("id")
    @Expose
    var id: Int? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("phone")
    @Expose
    var phone: String? = null
    @SerializedName("email")
    @Expose
    var email: String? = null
    @SerializedName("occupation")
    @Expose
    var occupation: String? = null
    @SerializedName("password")
    @Expose
    var password: String? = null


}