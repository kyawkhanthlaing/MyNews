package com.jojoe.mynews.ui.fragments.search

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jojoe.mynews.R
import com.jojoe.mynews.adapters.NewsAdapter
import com.jojoe.mynews.databinding.FragmentSearchNewsBinding
import com.jojoe.mynews.ui.NewsActivity
import com.jojoe.mynews.ui.NewsViewModel
import com.jojoe.mynews.util.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchNewsFragment:Fragment(R.layout.fragment_search_news) {

    private lateinit var binding: FragmentSearchNewsBinding
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentSearchNewsBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            val bundle=Bundle().apply {
                putSerializable("article",it)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment2_to_articleFragment,
                bundle
            )
        }

        var job: Job?=null

        binding.searchView.apply {
            clearFocus()
            setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    job?.cancel()
                    job = MainScope().launch {
                        delay(300L)
                         newText?.let {
                            if(newText.isNotEmpty()){
                                viewModel.searchNews(newText.toString())
                            }
                        }
                    }
                    return true
                }

            })
        }
//
        viewModel.searchNews.observe(viewLifecycleOwner, Observer {response->
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
                        Log.e("Search News","An error occured:$message")
                    }
                }
                is Resource.Loading -> {
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
        newsAdapter= NewsAdapter(){
            viewModel.saveArticle(it)
        }
        binding.rvSearchNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
}