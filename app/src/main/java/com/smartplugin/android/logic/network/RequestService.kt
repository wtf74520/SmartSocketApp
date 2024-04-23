package com.smartplugin.android.logic.network
import com.smartplugin.android.logic.model.MqttStatusResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UpStreamService {
    @GET("appUpStream")
    fun uploadMqttMessage(@Query("msg") msg: String,@Query("value") value: String): Call<MqttStatusResponse>

}
interface RequestDataService {
    @GET("appRequestData")
    fun requestDataService(): Call<MqttStatusResponse>

}