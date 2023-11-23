package com.example.newsarticlesdemo.model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Article(

    @SerializedName("articles") val articles: List<ArticleItem?>? = null,
    @SerializedName("status") val status: String? = null // ok
) : Parcelable {
    @Entity(
        tableName = "articles"
    )
    @Parcelize
    data class ArticleItem(
        @PrimaryKey(autoGenerate = true)
        var id: Int? = null, //Manually added id for database,it is not the part of api response
        @SerializedName("author") val author: String? = null, // Alex Wilhelm
        @SerializedName("content") val content: String? = null, // Hello and welcome back to our regular morning look at private companies, public markets and the gray space in between.Today we’re exploring some fascinating data from Silicon Valley Bank markets report for Q1 2020. We’re digging into two charts that deal wi… [+648 chars]
        @SerializedName("description") val description: String? = null, // Hello and welcome back to our regular morning look at private companies, public markets and the gray space in between. Today we’re exploring some fascinating data from Silicon Valley Bank markets report for Q1 2020. We’re digging into two charts that deal wit…
        @SerializedName("publishedAt") val publishedAt: String? = null, // 2020-02-10T17:06:42Z
        @SerializedName("source") val source: Source? = Source(),
        @SerializedName("title") val title: String? = null, // Is this what an early-stage slowdown looks like?
        @SerializedName("url") val url: String? = null, // http://techcrunch.com/2020/02/10/is-this-what-an-early-stage-slowdown-looks-like/
        @SerializedName("urlToImage") val urlToImage: String? = null, // https://techcrunch.com/wp-content/uploads/2020/02/GettyImages-dv1637047.jpg?w=556
        var isSaved: Boolean? = false
    ) : Parcelable {
        @Parcelize
        data class Source(
            @SerializedName("id") val id: String? = null, // techcrunch
            @SerializedName("name") val name: String? = null // TechCrunch
        ) : Parcelable

        fun isEqual(other: ArticleItem): Boolean {
            return this.author == other.author &&
                    this.content == other.content &&
                    this.description == other.description &&
                    this.publishedAt == other.publishedAt &&
                    this.source == other.source &&
                    this.title == other.title &&
                    this.url == other.url &&
                    this.urlToImage == other.urlToImage
        }
    }
}