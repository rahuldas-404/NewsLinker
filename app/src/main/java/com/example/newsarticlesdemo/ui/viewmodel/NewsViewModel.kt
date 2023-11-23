package com.example.newsarticlesdemo.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsarticlesdemo.api.ApiConstants
import com.example.newsarticlesdemo.model.Article
import com.example.newsarticlesdemo.repository.NewsRepository
import com.example.newsarticlesdemo.util.Resource
import com.example.newsarticlesdemo.util.SortOrder
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NewsViewModel(
    app: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(app) {
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }
    val selectedSortOrder = MutableLiveData<SortOrder>()

    /** Breaking News*/
    private val _newsList = MutableLiveData<Resource<Article>>()
    val newsList: LiveData<Resource<Article>> get() = _newsList

    init {
        fetchNews()
    }

    fun setSelectedSortOrder(sortOrder: SortOrder) {
        selectedSortOrder.value = sortOrder
    }

    fun fetchNews() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _newsList.postValue(Resource.Loading())

            val url = URL(ApiConstants.BASE_URL + ApiConstants.TOP_HEADLINES)
            val connection = withContext(Dispatchers.IO) {
                url.openConnection()
            } as HttpURLConnection

            val newsItems = try {
                val inputStream = connection.inputStream

                /** Get JSON string*/
                val json = inputStream.bufferedReader().use { it.readText() }

                /**  Parse the JSON string into a JSONObject */
                val jsonMainObject = JSONObject(json)

                /** Get the JSON array from the JSON object */
                val jsonArray = jsonMainObject.getJSONArray("articles")

                /**  Iterate through the JSON array */
                (0 until jsonArray.length()).map { i ->
                    val jsonObject = jsonArray.getJSONObject(i)

                    /**  Get the nested JSON object of Source from the JSON object */
                    val sourceObject = jsonObject.getJSONObject("source")
                    val source = Article.ArticleItem.Source(
                        id = sourceObject.getString("id") ?: "",
                        name = sourceObject.getString("name") ?: ""
                    )

                    Article.ArticleItem(
                        author = jsonObject.getString("author") ?: "",
                        content = jsonObject.getString("content") ?: "",
                        description = jsonObject.getString("description") ?: "",
                        publishedAt = jsonObject.getString("publishedAt") ?: "",
                        source = source,
                        title = jsonObject.getString("title") ?: "",
                        url = jsonObject.getString("url") ?: "",
                        urlToImage = jsonObject.getString("urlToImage") ?: ""
                    )

                }
            } catch (e: Exception) {
                e.printStackTrace()
                ArrayList<Article.ArticleItem>()
            } finally {
                connection.disconnect()
            }

            if (newsItems != null) {
                _newsList.postValue(Resource.Success(Article(newsItems)))
            } else {
                _newsList.postValue(Resource.Error("Failed to fetch news"))
            }
        }
    }

    fun saveArticle(article: Article.ArticleItem) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article.ArticleItem) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    fun sortArticlesByOldToNew(newsList: ArrayList<Article.ArticleItem>): List<Article.ArticleItem> {
        return newsList.sortedBy { convertDateStringToDate(it.publishedAt) }
    }

    fun sortArticlesByNewToOld(newsList: ArrayList<Article.ArticleItem>): List<Article.ArticleItem> {
        return newsList.sortedByDescending { it.publishedAt }
    }

    private fun convertDateStringToDate(dateString: String?): Date {
        dateString?.let {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            return dateFormat.parse(it) ?: Date(0)
        }
        return Date(0)
    }
}












