
import android.util.Log
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.network.UserService
import com.sunnyweather.android.logic.network.UserServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object UserNetwork {

    private val userService = UserServiceCreator.create<UserService>()

    suspend fun searchUser(id:String)  {
        Log.d(SunnyWeatherApplication.TAG, "searchUser function called with query: $id")
        val result = userService.searchUser(id).await()
        Log.d(SunnyWeatherApplication.TAG, "searchUser function executed with result: $result")
        return
    }



//    private suspend fun <T> Call<T>.await(): T {
//        return suspendCoroutine { continuation ->
//            enqueue(object : Callback<T> {
//                override fun onResponse(call: Call<T>, response: Response<T>) {
//                    val body = response.body()
//                    if (body != null){
//                        Log.d(SunnyWeatherApplication.TAG, "onResponse(call: Call<T>, response: Response<T>) body : ${body.toString()}")
//                        continuation.resume(body)
//                    }
//                    else continuation.resumeWithException(
//                        RuntimeException("response body is null"))
//                }
//
//                override fun onFailure(call: Call<T>, t: Throwable) {
//                    continuation.resumeWithException(t)
//                }
//            })
//        }
//    }
private suspend fun <T> Call<T>.await(): T {
    return suspendCoroutine { continuation ->
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (body != null){
                    Log.d(SunnyWeatherApplication.TAG, "onResponse: body is not null ${body.toString()}")
                    continuation.resume(body)
                }
                else {
                    Log.d(SunnyWeatherApplication.TAG, "onResponse: body is null")
                    continuation.resumeWithException(
                        RuntimeException("response body is null"))
                }
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.d(SunnyWeatherApplication.TAG, "onFailure: ${t.message}")
                continuation.resumeWithException(t)
            }
        })
    }
}


}