
import android.util.Log
import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.smartplugin.android.SunnyWeatherApplication
import com.smartplugin.android.logic.model.MqttStatusResponse

class MqttMessageViewModel : ViewModel() {

    private val upStreamMqttLiveData = MutableLiveData<Pair<String, String>>()

    lateinit var mqttStatusResponse: MqttStatusResponse

    val mqttLiveData = upStreamMqttLiveData.switchMap { pair ->
        val msg = pair.first
        val value = pair.second

        Log.d(SunnyWeatherApplication.TAG, "Repository.uploadMqttMessage(msg, value) $msg, $value")

        Repository.uploadMqttMessage(msg, value)


    }

    fun uploadMqttMessage(msg: String, value: String) {
        upStreamMqttLiveData.value = Pair(msg, value)
    }
}