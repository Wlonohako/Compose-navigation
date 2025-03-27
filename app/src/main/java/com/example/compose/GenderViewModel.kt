import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

data class GenderInfo(
    val name: String,
    val gender: String,
    val probability: Double,
    val count: Int
)

sealed class GenderState {
    object Loading : GenderState()
    data class Success(val data: GenderInfo) : GenderState()
    data class Error(val message: String) : GenderState()
}

class GenderViewModel : ViewModel() {
    private var client = OkHttpClient()
    var genderState by mutableStateOf<GenderState>(GenderState.Loading)
        private set

    fun getGender(name: String) {

        genderState = GenderState.Loading

        viewModelScope.launch {
            val url = "https://api.genderize.io"
            val request = Request.Builder()
                .url("$url?name=$name")
                .build()
            withContext(Dispatchers.IO) {
                try {
                    client.newCall(request).execute().use { response ->
                        val responseBody = response.body?.string()
                        if (response.isSuccessful && responseBody != null) {
                            val json = JSONObject(responseBody)
                            val genderInfo = GenderInfo(
                                name = json.getString("name"),
                                gender = json.getString("gender"),
                                probability = json.getDouble("probability"),
                                count = json.getInt("count")
                            )

                            genderState = GenderState.Success(genderInfo)
                        } else {

                            genderState =
                                GenderState.Error("$response")
                        }
                    }
                } catch (e: Exception) {

                    genderState = GenderState.Error("Exception: ${e.localizedMessage}")
                }
            }
        }
    }
}
