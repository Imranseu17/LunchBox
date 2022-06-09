package com.thelunchbox.bd.presenter

import com.google.gson.JsonObject
import com.thelunchbox.bd.callback.RegistrationView
import com.thelunchbox.bd.enum_class.ErrorCode
import com.thelunchbox.bd.enum_class.RegistrationEnum
import com.thelunchbox.bd.errors.APIErrors
import com.thelunchbox.bd.models.Registration
import com.thelunchbox.bd.service.APIClient
import com.thelunchbox.bd.utils.DebugLog
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.HashMap

class RegistrationPresenter(private val mViewInterface: RegistrationView) {
    private var mApiClient: APIClient? = null
    fun attemptRegistration(email: String?, password: String?,name: String?, phone_number: String?,
        occupation:String) {
        val map: MutableMap<String?, String?> = HashMap()
        map["Content-Type"] = "application/json"
        val jsonObject = JsonObject()
        jsonObject.addProperty("name", name)
        jsonObject.addProperty("phone", phone_number)
        jsonObject.addProperty("email", email)
        jsonObject.addProperty("occupation", occupation)
        jsonObject.addProperty("password", password)
        mApiClient!!.getAPI()
            ?.getRegistration(map, jsonObject)
            ?.enqueue(object : Callback<Registration?> {
                override fun onResponse(call: Call<Registration?>, response: Response<Registration?>) {
                    DebugLog.e("CODE: " + response.code())
                    if (response.isSuccessful) {
                        val Registration = response.body()
                        if (Registration != null) {
                            mViewInterface.onSuccess(Registration, RegistrationEnum.REGISTRATION_SUCCESS.code)
                        } else {
                            mViewInterface.onError("Error fetching data", response.code())
                        }
                    } else errorHandle(
                        response.code(),
                        RegistrationEnum.REGISTRATION_FAILED.code,
                        response.errorBody()
                    )
                }

                override fun onFailure(call: Call<Registration?>, e: Throwable) {
                    DebugLog.e(call.request().toString())
                    DebugLog.e(e.message!!)
                    e.stackTrace
                    if (e is HttpException) {
                        val code = e.response()!!.code()
                        val responseBody = e.response()!!
                            .errorBody()
                        mViewInterface.onError(responseBody.toString(), code)
                    } else if (e is SocketTimeoutException) {
                        mViewInterface.onError(
                            "Server connection error",
                            RegistrationEnum.SERVER_ERROR.code
                        )
                    } else if (e is IOException) {
                        if (e.message != null) mViewInterface.onError(
                            e.message,
                            RegistrationEnum.REGISTRATION_FAILED.code
                        ) else mViewInterface.onError("IO Exception", RegistrationEnum.REGISTRATION_FAILED.code)
                    } else {
                        mViewInterface.onError("Unknown error", RegistrationEnum.REGISTRATION_FAILED.code)
                    }
                }
            })
    }

    private fun errorHandle(code: Int, errorType: Int, responseBody: ResponseBody?) {
        val errorCode = ErrorCode.getByCode(code)
        when (errorCode) {
            ErrorCode.ERRORCODE500 -> {
                mViewInterface.onError(APIErrors.get500ErrorMessage(responseBody!!), errorType)
                mViewInterface.onError(APIErrors.get406ErrorMessage(responseBody), errorType)
                mViewInterface.onError(APIErrors.get406ErrorMessage(responseBody), errorType)
            }
            ErrorCode.ERRORCODE406 -> {
                mViewInterface.onError(APIErrors.get406ErrorMessage(responseBody!!), errorType)
                mViewInterface.onError(APIErrors.get406ErrorMessage(responseBody), errorType)
            }
            else -> mViewInterface.onError(APIErrors.get406ErrorMessage(responseBody!!), errorType)
        }
    }

    init {
        if (mApiClient == null) {
            mApiClient = APIClient()
        }
    }
}