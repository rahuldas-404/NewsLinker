package com.example.newsarticlesdemo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsarticlesdemo.R
import com.example.newsarticlesdemo.databinding.ItemArticlePreviewBinding
import com.example.newsarticlesdemo.model.Article

class NewsAdapter(private val context: Context) :
    ListAdapter<Article.ArticleItem, NewsAdapter.ViewHolder>(DifferCallback()) {


    class ViewHolder(val binding: ItemArticlePreviewBinding) : RecyclerView.ViewHolder(binding.root)
    class DifferCallback : DiffUtil.ItemCallback<Article.ArticleItem>() {
        override fun areItemsTheSame(
            oldItem: Article.ArticleItem,
            newItem: Article.ArticleItem
        ): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(
            oldItem: Article.ArticleItem,
            newItem: Article.ArticleItem
        ): Boolean {
            return oldItem == newItem
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemArticlePreviewBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_article_preview, parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = currentList[position]

        holder.binding.apply {
            Glide.with(context)
                .load(article.urlToImage)
                .error(R.drawable.ic_news)
                .placeholder(R.drawable.ic_news)
                .into(ivArticleImage)
            tvSource.text = article.source?.name
            tvTitle.text = article.title
            tvDescription.text = article.description
            tvPublishedAt.text = article.publishedAt

            root.setOnClickListener {
                onItemClickListener?.let { it(article, position) }
            }

        }
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    private var onItemClickListener: ((Article.ArticleItem, Int) -> Unit)? = null


    fun setOnItemClickListener(listener: (Article.ArticleItem, Int) -> Unit) {
        onItemClickListener = listener
    }

}













