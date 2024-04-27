package com.smartplugin.android.logic.network



import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MqttServiceCreator {

    private const val BASE_URL = "http://192.168.3.32:8899/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    inline fun <reified T> create(): T = create(T::class.java)

}
