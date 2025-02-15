package com.example.compose

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

class GenderViewModel : ViewModel() {
    private var client = OkHttpClient()
    var genderInfo by mutableStateOf<GenderInfo?>(null)
        private set

    fun getGender(name: String) {
        viewModelScope.launch {
            val url = "https://api.genderize.io"
            val request = Request.Builder()
                .url("$url?name=$name")
                .build()
            withContext(Dispatchers.IO) {
                try {

                    client.newCall(request).execute().use { response ->
                        response.body?.string().let { responseBody ->
                            val json = JSONObject(responseBody!!)
                            genderInfo = GenderInfo(
                                name = json.getString("name"),
                                gender = json.getString("gender"),
                                probability = json.getDouble("probability"),
                                count = json.getInt("count")
                            )

                        }
                    }
                } catch (e: Exception) {
                    genderInfo = null
                }

            }
        }

    }
}