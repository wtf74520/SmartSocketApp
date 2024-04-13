
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.UserResponse

class UserViewModel : ViewModel() {

    private val userSearchLiveData = MutableLiveData<String>()

    //val placeList = ArrayList<Place>()
    lateinit var  user : UserResponse
    val userLiveData = Transformations.switchMap(userSearchLiveData) { query ->
        Log.d(SunnyWeatherApplication.TAG, "Repository.searchUser(query) $query")

        Repository.searchUser(query)

    }

    fun searchUser(content: String) {
        userSearchLiveData.value = content
    }

}