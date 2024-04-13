package com.sunnyweather.android.logic.network

import com.sunnyweather.android.logic.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("user")
    fun searchUser(@Query("id") query: String): Call<UserResponse>

}
