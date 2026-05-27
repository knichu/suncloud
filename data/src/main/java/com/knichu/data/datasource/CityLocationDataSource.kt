package com.knichu.data.datasource

import android.content.Context
import com.google.gson.Gson
import com.knichu.data.dto.response.CityLocationResponseDTO
import java.io.IOException
import javax.inject.Inject

class CityLocationDataSource @Inject constructor(
    private val context: Context
) : BaseNetworkDataSource() {

    suspend fun getAllCityLocation(): CityLocationResponseDTO {
        val inputStream = context.assets.open("json/cityCode.json")
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        val json = String(buffer, Charsets.UTF_8)
        return Gson().fromJson(json, CityLocationResponseDTO::class.java)
    }
}
