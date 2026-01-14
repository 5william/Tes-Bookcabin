package com.virakumaro.testbookcabin.core.helper

import android.content.Context
import java.io.IOException

object Helper {
    fun loadJsonFromAssets(context: Context, fileName: String): String? {
        return try {
            context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
    }
}