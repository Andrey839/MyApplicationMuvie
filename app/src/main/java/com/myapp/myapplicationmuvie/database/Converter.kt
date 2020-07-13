package com.myapp.myapplicationmuvie.database

import androidx.room.TypeConverter

class ConverterRating {

    @TypeConverter
    fun fromList(item: List<String>?): String? {
       return item?.joinToString ( "," ) ?: ""
    }

    @TypeConverter
    fun toList(item: String?): List<String?> {
        return item?.split(",") ?: listOf()
    }
}