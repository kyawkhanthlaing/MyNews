package com.jojoe.mynews.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jojoe.mynews.R
import com.jojoe.mynews.adapters.NewsAdapter
import com.jojoe.mynews.databinding.FragmentBreakingNewsBinding
import com.jojoe.mynews.ui.NewsActivity
import com.jojoe.mynews.ui.NewsViewModel
import com.jojoe.mynews.util.Resource


class BreakingNewsFragment:Fragment(R.layout.fragment_breaking_news) {
    private lateinit var binding: FragmentBreakingNewsBinding
    lateinit var viewModel:NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentBreakingNewsBinding.bind(view)

        viewModel=(activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            Log.d("article", "onViewCreated: $it")
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }

            findNavController().navigate(
                BreakingNewsFragmentDirections.actionBreakingNewsFragment2ToArticleFragment(it)
            )
        }
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()

                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                        newsResponse.articles.onEachIndexed {i,v->
                            Log.d("success", "onViewCreated: $i ${newsResponse.articles}")
                        }
                    }
                }
                is Resource.Error->{
                    showProgressBar()
                    Log.d("success", "onViewCreated: error")
                    response.message?.let {message->
                        Log.e("Breaking News","An error occured:$message")
                    }
                }
                is Resource.Loading -> {
                    Log.d("success", "onViewCreated: Loading")
                    showProgressBar()
                }
            }

        })

    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility=View.INVISIBLE
    }
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility=View.VISIBLE
    }

    private fun setupRecyclerView(){
        newsAdapter= NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
        }
    }
}