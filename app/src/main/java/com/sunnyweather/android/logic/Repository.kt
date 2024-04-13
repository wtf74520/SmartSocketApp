
import android.util.Log
import androidx.lifecycle.liveData
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.model.UserResponse
import kotlinx.coroutines.Dispatchers


object Repository {

    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
            if (placeResponse.status == "ok") {
                val places = placeResponse.places
                Result.success(places)
            } else {
                Result.failure(RuntimeException("response status is${placeResponse.status}"))
            }
        } catch (e: Exception) {
            Result.failure<List<Place>>(e)
        }
        emit(result)
    }
    fun searchUser(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            Log.d(SunnyWeatherApplication.TAG, "liveData(Dispatchers.IO) query : $query")

            val userResponse = UserNetwork.searchUser(query)
            Log.d(SunnyWeatherApplication.TAG, "userResponse : $userResponse ")
            if (true) {
                Result.success(userResponse)
            } else {
                Result.failure<UserResponse>(RuntimeException("response name is ${userResponse}"))
            }
        } catch (e: Exception) {
            Result.failure<UserResponse>(e)
        }
        emit(result)
    }


}