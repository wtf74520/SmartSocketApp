
import android.util.Log
import androidx.lifecycle.liveData
import com.smartplugin.android.SunnyWeatherApplication

import com.smartplugin.android.logic.model.UserResponse
import kotlinx.coroutines.Dispatchers


object Repository {


    fun uploadMqttMessage(msg:String, value: String) = liveData(Dispatchers.IO) {
        val result = try {
            Log.d(SunnyWeatherApplication.TAG, "liveData(Dispatchers.IO) msg : $msg value : $value")

            val mqttResponse = UpStreamNetwork.uploadMqttMessage(msg,value)
            Log.d(SunnyWeatherApplication.TAG, "Response : ${mqttResponse.toString()} ")
            if (true) {
                Result.success(mqttResponse)
            } else {
                Result.failure<UserResponse>(RuntimeException("response name is ${mqttResponse.toString()}"))
            }
        } catch (e: Exception) {
            Result.failure<UserResponse>(e)
        }
        emit(result)
    }
    fun requestData() = liveData(Dispatchers.IO) {
        val result = try {


            val mqttResponse = UpStreamNetwork.requestData()
            Log.d(SunnyWeatherApplication.TAG, "Response : ${mqttResponse.toString()} ")
            if (true) {
                Result.success(mqttResponse)
            } else {
                Result.failure<UserResponse>(RuntimeException("response name is ${mqttResponse.toString()}"))
            }
        } catch (e: Exception) {
            Result.failure<UserResponse>(e)
        }
        emit(result)
    }

}