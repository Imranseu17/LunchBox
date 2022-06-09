package com.thelunchbox.bd.errors

import okhttp3.ResponseBody
import org.json.JSONObject
import org.json.JSONArray
import java.lang.Exception

object APIErrors {
    fun getErrorMessage(response: ResponseBody): String {
        return try {
            val jsonStr = response.string()
            val jsonObject = JSONObject(jsonStr)
            val jsonArray = jsonObject.getJSONArray("fieldErrors")
            val jsonError = jsonArray.getJSONObject(0)
            jsonError.getString("message")
        } catch (e: Exception) {
            e.printStackTrace()
            "Error occurred Please try again"
        }
    }

    fun get406ErrorMessage(response: ResponseBody): String {
        return try {
            val jsonStr = response.string()
            val jObjError = JSONObject(jsonStr)
            jObjError.getString("message")
        } catch (e: Exception) {
            "Error occurred Please try again"
        }
    }

    fun get500ErrorMessage(response: ResponseBody): String {
        return try {
            val jsonStr = response.string()
            val jsonObject = JSONObject(jsonStr)
            jsonObject.getString("message")
        } catch (e: Exception) {
            e.printStackTrace()
            "Error occurred Please try again"
        }
    }
}