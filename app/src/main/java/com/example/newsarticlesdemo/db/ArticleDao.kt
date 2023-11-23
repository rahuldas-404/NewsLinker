package com.example.newsarticlesdemo.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.newsarticlesdemo.model.Article

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article.ArticleItem): Long

    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article.ArticleItem>>

    @Delete
    suspend fun deleteArticle(article: Article.ArticleItem)
}