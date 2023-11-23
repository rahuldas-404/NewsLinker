package com.example.newsarticlesdemo.ui.fragments

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.example.newsarticlesdemo.R
import com.example.newsarticlesdemo.base.BaseFragment
import com.example.newsarticlesdemo.databinding.FragmentArticleBinding
import com.example.newsarticlesdemo.model.Article
import com.example.newsarticlesdemo.ui.NewsActivity
import com.example.newsarticlesdemo.ui.viewmodel.NewsViewModel

class ArticleFragment : BaseFragment() {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var viewModel: NewsViewModel
    private val args: ArticleFragmentArgs by navArgs()
    private var article = Article.ArticleItem()
    private var isSaved: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        setUpObserver()
        setUpClickListener()
        setAricleData()
    }

    private fun setAricleData() {
        article = args.articleData
        isSaved = args.isSaved
        binding.webView.apply {
            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    // Show the progress bar when page starts loading
                    binding.progressBar.visibility = ProgressBar.VISIBLE
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // Hide the progress bar when page finishes loading
                    binding.progressBar.visibility = ProgressBar.GONE
                }
            }

            // Enable JavaScript (optional)
            val webSettings: WebSettings = settings
            webSettings.javaScriptEnabled = true

            loadUrl(article.url ?: "")

        }

        if (isSaved) {
            binding.fab.setColorFilter(ContextCompat.getColor(requireContext(),R.color.red), PorterDuff.Mode.SRC_IN)

        }
    }

    override fun setUpViewModel() {
        viewModel = (activity as NewsActivity).viewModel

    }

    override fun setUpObserver() {

    }

    override fun setUpClickListener() {
        binding.apply {
            fab.setOnClickListener {
                if (isSaved) {
                    viewModel.deleteArticle(article)
                    fab.setColorFilter(ContextCompat.getColor(requireContext(),R.color.black), PorterDuff.Mode.SRC_IN)
                } else {
                    viewModel.saveArticle(article)
                    fab.setColorFilter(ContextCompat.getColor(requireContext(),R.color.red), PorterDuff.Mode.SRC_IN)
                }
            }
        }
    }

}