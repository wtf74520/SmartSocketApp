package com.smartplugin.android.logic.network



import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MqttServiceCreator {

    private const val BASE_URL = "http://10.101.6.38:8899/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

}
