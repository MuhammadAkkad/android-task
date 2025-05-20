package com.company.androidtask.data.manager

import android.content.SharedPreferences
import android.util.Log
import androidx.core.content.edit
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CacheManager @Inject constructor(
    val prefs: SharedPreferences
) {

    inline fun <reified T : Any> get(key: String): T? {
        return kotlin.runCatching {
            if (!hasKey(key)) {
                Log.w("CACHE MANAGER", "data not found")
                return null
            }

            val data = when (T::class) {
                Boolean::class -> prefs.getBoolean(key, false) as T
                Int::class -> prefs.getInt(key, -1).takeIf {
                    it != -1
                } as T?

                String::class -> prefs.getString(key, null) as T?
                else -> null // TODO support other types
            }

            Log.i("CACHE MANAGER ", "Get key:$key with value: $data")
            return data
        }.onFailure {
            Log.e("CACHE MANAGER ", "Get fail with error: ${it.message}")
        }.getOrNull()
    }

    inline fun <reified T : Any> set(key: String, data: T?) {
        prefs.edit(true) {

            when (T::class) {
                Boolean::class -> putBoolean(key, data as Boolean? ?: false)
                Int::class -> putInt(key, data as Int? ?: -1)
                String::class -> putString(key, data as String?)
                Date::class -> putLong(key, (data as Date?)?.time ?: -1L)
                else -> {} // TODO support other types
            }
            Log.i("CACHE MANAGER ", "Set key:$key with value:$data")
        }
    }

    fun remove(key: String) {
        if (hasKey(key)) {
            prefs.edit { remove(key) }
            Log.i("CACHE MANAGER ", "data removed with key:$key")

        }
    }

    fun hasKey(key: String): Boolean = prefs.contains(key)

}