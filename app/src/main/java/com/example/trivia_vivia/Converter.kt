package com.example.trivia_vivia

import androidx.room.TypeConverter
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
//Type Converters: Allows Room to handle complex data types (e.g., lists) by converting them to and from a supported format (e.g., JSON strings).
class Converter {
    @TypeConverter
    //Convert from JSON string to array of strings
    fun fromString(value: String?): List<String>? {
        return value?.let {
            val listType = object : TypeToken<List<String>>() {}.type
            Gson().fromJson(it, listType)
        }
    }

    @TypeConverter
    fun fromList(list: List<String>?): String? {
        return list?.let { Gson().toJson(it) }
    }

    @TypeConverter
    fun fromImageAnswerList(value: String): List<ImageAnswer> {
        val listType = object : TypeToken<List<ImageAnswer>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toImageAnswerList(list: List<ImageAnswer>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun fromImageAnswerListList(value: String): List<List<ImageAnswer>> {
        val listType = object : TypeToken<List<List<ImageAnswer>>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun toImageAnswerListList(list: List<List<ImageAnswer>>): String {
        return Gson().toJson(list)
    }
}