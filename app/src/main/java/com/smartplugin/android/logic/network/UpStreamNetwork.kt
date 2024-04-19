
import android.util.Log
import com.smartplugin.android.SunnyWeatherApplication
import com.smartplugin.android.logic.model.MqttStatusResponse
import com.smartplugin.android.logic.network.UpStreamService
import com.smartplugin.android.logic.network.MqttServiceCreator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object UpStreamNetwork {

    private val upStreamService = MqttServiceCreator.create<UpStreamService>()

    suspend fun uploadMqttMessage(msg:String, value:String) : MqttStatusResponse {
        Log.d(SunnyWeatherApplication.TAG, "uploadMqttMessage function called with query: ${value.toString()}")
        val result = upStreamService.uploadMqttMessage(msg,value).await()
        Log.d(SunnyWeatherApplication.TAG, "uploadMqttMessage function executed with result: ${result.toString()}")
        return result
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