package com.thelunchbox.bd.service

import android.os.Build
import com.google.gson.GsonBuilder
import com.thelunchbox.bd.utils.DebugLog
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class APIClient {
    private var retrofit: Retrofit? = null
    var BASE_URL = "http://192.168.0.118:7000/"

    @Synchronized
    fun getAPI(): ApiInterface? {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        retrofit = Retrofit. Builder()
            .baseUrl(BASE_URL)
            .client(getUnsafeOkHttpClient().build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return  retrofit?.create(ApiInterface::class.java)
    }

    @Synchronized
    fun getUnsafeOkHttpClient(): OkHttpClient.Builder {
        return try {
            // Create a trust manager that does not validate certificate chains
            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate>,
                        authType: String
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate> {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())

            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                builder.connectTimeout(1, TimeUnit.MINUTES)
            }
            builder.readTimeout(30, TimeUnit.SECONDS)
            builder.writeTimeout(30, TimeUnit.SECONDS)
            builder.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
            builder.addInterceptor { chain ->
                val request = chain.request()
                chain.proceed(request)
            }.hostnameVerifier { hostname, session ->
                DebugLog.e(hostname)
                true
            }

            return builder
        } catch (e:Exception) {
            throw  RuntimeException(e)
        }
    }
}