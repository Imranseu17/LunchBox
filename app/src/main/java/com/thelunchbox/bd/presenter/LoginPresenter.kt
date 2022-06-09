package com.thelunchbox.bd.presenter


import com.thelunchbox.bd.utils.DebugLog.e
import com.thelunchbox.bd.callback.LoginView
import com.thelunchbox.bd.service.APIClient
import com.google.gson.JsonObject
import com.thelunchbox.bd.enum_class.ErrorCode
import com.thelunchbox.bd.models.Login
import com.thelunchbox.bd.utils.DebugLog
import com.thelunchbox.bd.enum_class.LoginEnum
import okhttp3.ResponseBody
import com.thelunchbox.bd.errors.APIErrors
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.util.HashMap

class LoginPresenter(private val mViewInterface: LoginView) {
    private var mApiClient: APIClient? = null
    fun attemptLogin(phone: String?, password: String?) {
        val map: MutableMap<String?, String?> = HashMap()
        map["Content-Type"] = "application/json"
        val jsonObject = JsonObject()
        jsonObject.addProperty("phone", phone)
        jsonObject.addProperty("password", password)
        mApiClient!!.getAPI()
            ?.getLogin(map, jsonObject)
            ?.enqueue(object : Callback<Login?> {
                override fun onResponse(call: Call<Login?>, response: Response<Login?>) {
                    e("CODE: " + response.code())
                    if (response.isSuccessful) {
                        val login = response.body()
                        if (login != null) {
                            mViewInterface.onSuccess(login, LoginEnum.LOGIN_SUCCESS.code)
                        } else {
                            mViewInterface.onError("Error fetching data", response.code())
                        }
                    } else errorHandle(
                        response.code(),
                        LoginEnum.LOGIN_FAILED.code,
                        response.errorBody()
                    )
                }

                override fun onFailure(call: Call<Login?>, e: Throwable) {
                    e(call.request().toString())
                    e(e.message!!)
                    e.stackTrace
                    if (e is HttpException) {
                        val code = e.response()!!.code()
                        val responseBody = e.response()!!
                            .errorBody()
                        mViewInterface.onError(responseBody.toString(), code)
                    } else if (e is SocketTimeoutException) {
                        mViewInterface.onError(
                            "Server connection error",
                            LoginEnum.SERVER_ERROR.code
                        )
                    } else if (e is IOException) {
                        if (e.message != null) mViewInterface.onError(
                            e.message,
                            LoginEnum.LOGIN_FAILED.code
                        ) else mViewInterface.onError("IO Exception", LoginEnum.LOGIN_FAILED.code)
                    } else {
                        mViewInterface.onError("Unknown error", LoginEnum.LOGIN_FAILED.code)
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