package kz.zeme.skycast.core.network

import com.google.gson.annotations.SerializedName
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class EnumRetrofitConverterFactory : Converter.Factory() {

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<*, String>? {
        val clazz = type as? Class<*> ?: return null
        if (!clazz.isEnum) return null

        return Converter<Enum<*>, String> { enum ->
            val field = enum.javaClass.getField(enum.name)
            field.getAnnotation(SerializedName::class.java)?.value ?: enum.name
        }
    }
}
