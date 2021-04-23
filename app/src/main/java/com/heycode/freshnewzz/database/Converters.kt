package com.heycode.freshnewzz.database

import androidx.room.TypeConverter
import com.heycode.freshnewzz.models.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}