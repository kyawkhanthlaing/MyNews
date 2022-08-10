package com.jojoe.mynews.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jojoe.mynews.R
import com.jojoe.mynews.adapters.NewsAdapter
import com.jojoe.mynews.databinding.FragmentHomeNewsBinding
import com.jojoe.mynews.ui.NewsActivity
import com.jojoe.mynews.ui.NewsViewModel
import com.jojoe.mynews.util.Resource


class BreakingNewsFragment:Fragment(R.layout.fragment_home_news) {
    private lateinit var binding: FragmentHomeNewsBinding
    lateinit var viewModel:NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var onSaveClicked:()->Unit

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentHomeNewsBinding.bind(view)

        viewModel=(activity as NewsActivity).viewModel

        onSaveClicked={
           // viewModel.saveArticle(article)
            Snackbar.make(view,"Saved successfully", Snackbar.LENGTH_SHORT).show()
        }
        setupNewsRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }

            findNavController().navigate(
                HomeNewsFragmentDirections.actionHomeNewsFragmentToArticleFragment(it)
            )
        }

        viewModel.breakingNews.observe(viewLifecycleOwner, Observer {response->
            when(response){
                is Resource.Success->{
                    hideProgressBar()

                    response.data?.let {newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error->{
                    showProgressBar()
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

    private fun setupNewsRecyclerView(){
        newsAdapter= NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter=newsAdapter
            layoutManager=LinearLayoutManager(activity)
        }
    }



}