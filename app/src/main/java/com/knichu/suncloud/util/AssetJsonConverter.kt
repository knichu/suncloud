package com.knichu.suncloud.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

class AssetJsonConverter(private val context: Context) : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        val adapter: TypeAdapter<*> = Gson().getAdapter(TypeToken.get(type))
        return AssetJsonResponseBodyConverter(context, adapter)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): AssetJsonRequestBodyConverter<out Any> {
        val adapter: TypeAdapter<*> = Gson().getAdapter(TypeToken.get(type))
        return AssetJsonRequestBodyConverter(context, adapter)
    }

    private class AssetJsonResponseBodyConverter<T>(
        private val context: Context,
        private val adapter: TypeAdapter<T>
    ) : Converter<ResponseBody, T> {

        override fun convert(value: ResponseBody): T? {
            value.use {
                val json = it.string()
                return adapter.fromJson(json)
            }
        }
    }

    class AssetJsonRequestBodyConverter<T : Any>(
        private val context: Context,
        private val adapter: TypeAdapter<T>
    ) : Converter<T, RequestBody> {

        override fun convert(value: T): RequestBody {
            val json = adapter.toJson(value)
            return json.toByteArray(StandardCharsets.UTF_8)
                .toRequestBody("application/json; charset=UTF-8".toMediaTypeOrNull())
        }
    }
}