package com.example.newsarticlesdemo.repository

import com.example.newsarticlesdemo.db.ArticleDatabase
import com.example.newsarticlesdemo.model.Article

class NewsRepository(
    private val db: ArticleDatabase
) {
    suspend fun upsert(article: Article.ArticleItem) = db.getArticleDao().upsert(article)

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article.ArticleItem) =
        db.getArticleDao().deleteArticle(article)
}