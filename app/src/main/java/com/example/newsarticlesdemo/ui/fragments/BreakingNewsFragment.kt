package com.example.newsarticlesdemo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsarticlesdemo.adapters.NewsAdapter
import com.example.newsarticlesdemo.base.BaseFragment
import com.example.newsarticlesdemo.databinding.FragmentBreakingNewsBinding
import com.example.newsarticlesdemo.model.Article
import com.example.newsarticlesdemo.ui.NewsActivity
import com.example.newsarticlesdemo.ui.viewmodel.NewsViewModel
import com.example.newsarticlesdemo.util.Resource
import com.example.newsarticlesdemo.util.SortOrder

private const val TAG = "BreakingNewsFragment"

class BreakingNewsFragment : BaseFragment() {
    private lateinit var binding: FragmentBreakingNewsBinding

    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private var newsList = ArrayList<Article.ArticleItem>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentBreakingNewsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()

        setupRecyclerView()
        setUpObserver()
        setUpClickListener()
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.paginationProgressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun hideErrorMessage() {
        binding.itemErrorMessage.root.visibility = View.INVISIBLE
        isError = false
    }

    private fun showErrorMessage(message: String) {
        binding.itemErrorMessage.root.visibility = View.VISIBLE
        binding.itemErrorMessage.tvErrorMessage.text = message
        isError = true
    }

    private var isError = false
    private var isLoading = false

    private fun setupRecyclerView() {

        newsAdapter = NewsAdapter(requireContext())

        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity,RecyclerView.VERTICAL,false)
        }
    }

    override fun setUpViewModel() {
        viewModel = (activity as NewsActivity).viewModel
    }

    override fun setUpObserver() {
        viewModel.selectedSortOrder.observe(viewLifecycleOwner, Observer { sortOrder ->
            val sortedArticles = when (sortOrder) {
                SortOrder.OLD_TO_NEW -> viewModel.sortArticlesByOldToNew(newsList)
                SortOrder.NEW_TO_OLD -> viewModel.sortArticlesByNewToOld(newsList)
            }
            newsAdapter.submitList(sortedArticles)
            binding.rvBreakingNews.scrollToPosition(0)
        })
        viewModel.newsList.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    hideErrorMessage()
                    response.data?.let { newsResponse ->
                        newsList = ArrayList(
                            newsResponse.articles?.filterNotNull()
                                ?: ArrayList<Article.ArticleItem>()
                        )
                        newsAdapter.submitList(newsList.toList())
                    }
                }

                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(
                            activity,
                            "An error occurred: $message",
                            Toast.LENGTH_LONG
                        )
                            .show()
                        showErrorMessage(message)
                    }
                }

                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })


    }

    override fun setUpClickListener() {
        binding.newRadioButton.setOnClickListener {
            viewModel.setSelectedSortOrder(SortOrder.NEW_TO_OLD)
        }
        binding.oldRadioButton.setOnClickListener {
            viewModel.setSelectedSortOrder(SortOrder.OLD_TO_NEW)
        }
        binding.itemErrorMessage.btnRetry.setOnClickListener {
            viewModel.fetchNews()
        }
        newsAdapter.setOnItemClickListener { article, pos ->
            val action =
                BreakingNewsFragmentDirections.actionBreakingNewsFragmentToArticleFragment(
                    article
                )
            findNavController().navigate(action)
        }
    }

}








