package com.myapp.myapplicationmuvie.database

import androidx.room.TypeConverter
import com.myapp.myapplicationmuvie.networkService.Rating

class ConverterList {

    @TypeConverter
    fun fromList(item: List<Rating>?): Rating? {
        return item?.first()
    }

    @TypeConverter
    fun toList(item: Rating?): List<Rating?> {
        return listOf(item)
    }
}

class ConverterRating {
    @TypeConverter
    fun fromRating(item: Rating?): String? {
        return item?.toString()
    }

    @TypeConverter
    fun toRating(source: String?): Rating? {
        return source?.let { Rating(source = it, rate = source) }
    }
}