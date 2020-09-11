package com.myapp.myapplicationmuvie.database

import androidx.room.TypeConverter
import com.myapp.myapplicationmuvie.networkService.Rating

class ConverterList {

    @TypeConverter
    fun fromList(item: List<Rating>?): Rating? {
       return item?.get(0)
    }

    @TypeConverter
    fun toList(item: Rating?): List<Rating?> {
        return listOf(item)
    }
}

class ConverterRating{
    @TypeConverter
    fun fromRating(item: Rating?): String?{
        return item?.toString()
    }

    @TypeConverter
    fun toRating(source: String?): Rating?{
        return source?.let { Rating(source = it, rate = source) }
    }
}

class ConverterListString{
    @TypeConverter
    fun withListTrailer(item: List<String>?): String?{
        return if (item!!.isEmpty()) ""
        else item[0]
    }

    @TypeConverter
    fun toListTrailer(item: String?): List<String?>{
        return listOf(item)
    }
}