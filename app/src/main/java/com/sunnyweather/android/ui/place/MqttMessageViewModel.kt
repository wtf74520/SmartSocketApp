
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.MqttResponse

class MqttMessageViewModel : ViewModel() {

    private val upStreamMqttLiveData = MutableLiveData<Pair<String, String>>()

    lateinit var mqttResponse: MqttResponse

    val mqttLiveData = Transformations.switchMap(upStreamMqttLiveData) { pair ->
        val msg = pair.first
        val value = pair.second

        Log.d(SunnyWeatherApplication.TAG, "Repository.searchUser(msg, value) $msg, $value")

        Repository.uploadMqttMessage(msg, value)
    }

    fun uploadMqttMessage(msg: String, value: String) {
        upStreamMqttLiveData.value = Pair(msg, value)
    }
}