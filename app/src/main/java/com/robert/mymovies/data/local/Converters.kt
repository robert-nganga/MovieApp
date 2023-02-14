package com.robert.mymovies.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.robert.mymovies.model.Genre

class Converters {

    @TypeConverter
    fun fromString(value: String): List<Genre> {
        val listType = object : TypeToken<List<Genre>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Genre>): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}