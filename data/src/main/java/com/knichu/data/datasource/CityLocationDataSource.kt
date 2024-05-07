package com.knichu.data.datasource

import com.knichu.data.dto.response.CityLocationResponseDTO
import io.reactivex.rxjava3.core.Single
import java.io.IOException
import javax.inject.Inject
import android.content.Context
import com.google.gson.Gson

class CityLocationDataSource @Inject constructor(
    private val context: Context
) : BaseNetworkDataSource() {
    fun getAllCityLocation(): Single<CityLocationResponseDTO> {
        return Single.create { emitter ->
            try {
                val inputStream = context.assets.open("json/cityCode.json")
                val size = inputStream.available()
                val buffer = ByteArray(size)
                inputStream.read(buffer)
                inputStream.close()
                val json = String(buffer, Charsets.UTF_8)
                val cityLocationResponseDTO = Gson().fromJson(json, CityLocationResponseDTO::class.java)
                emitter.onSuccess(cityLocationResponseDTO)
            } catch (ex: IOException) {
                emitter.onError(ex)
            }
        }
    }

}