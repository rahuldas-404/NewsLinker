package com.example.newsarticlesdemo.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsResponse(
    val articles: MutableList<Article.ArticleItem>? = null,
    val status: String? = null,
    val totalResults: Int? = null
) : Parcelable