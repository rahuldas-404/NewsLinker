package com.example.newsarticlesdemo.db

import androidx.room.TypeConverter
import com.example.newsarticlesdemo.model.Article

class Converters {

    @TypeConverter
    fun fromSource(source: Article.ArticleItem.Source): String {
        return source.name?:""
    }

    @TypeConverter
    fun toSource(name: String): Article.ArticleItem.Source {
        return Article.ArticleItem.Source(name, name)
    }
}