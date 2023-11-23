package com.example.newsarticlesdemo.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.newsarticlesdemo.ui.viewmodel.NewsViewModelProviderFactory
import com.example.newsarticlesdemo.ui.viewmodel.NewsViewModel
import com.example.newsarticlesdemo.repository.NewsRepository
import com.example.newsarticlesdemo.R
import com.example.newsarticlesdemo.databinding.ActivityNewsBinding
import com.example.newsarticlesdemo.db.ArticleDatabase


class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)

        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory = NewsViewModelProviderFactory(application, newsRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory)[NewsViewModel::class.java]
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.newsNavHostFragment) as NavHostFragment?
        val navController = navHostFragment!!.navController
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);
      //  binding.bottomNavigationView.setupWithNavController(binding.newsNavHostFragment.findNavController())

    }
}