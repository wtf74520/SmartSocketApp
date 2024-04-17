package com.sunnyweather.android.logic.network
import com.sunnyweather.android.logic.model.MqttStatusResponse
import com.sunnyweather.android.logic.model.MsgOnResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UpStreamService {
    @GET("appUpStream")
    fun uploadMqttMessage(@Query("msg") msg: String,@Query("value") value: String): Call<MqttStatusResponse>

}
