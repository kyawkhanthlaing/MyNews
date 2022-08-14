package com.jojoe.mynews.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.jojoe.mynews.R
import com.jojoe.mynews.adapters.NewsAdapter
import com.jojoe.mynews.databinding.FragmentHomeBinding
import com.jojoe.mynews.ui.NewsActivity
import com.jojoe.mynews.ui.NewsViewModel
import com.jojoe.mynews.util.Resource


class HomeNewsFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        viewModel = (activity as NewsActivity).viewModel

        binding.tapLayout.addTab(binding.tapLayout.newTab().setText("Breaking News"))
        binding.tapLayout.addTab(binding.tapLayout.newTab().setText("Business"))
        binding.tapLayout.addTab(binding.tapLayout.newTab().setText("Entertainment"))
        binding.tapLayout.addTab(binding.tapLayout.newTab().setText("Health"))
        binding.tapLayout.addTab(binding.tapLayout.newTab().setText("Science"))
        binding.tapLayout.addTab(binding.tapLayout.newTab().setText("Sports"))
        binding.tapLayout.addTab(binding.tapLayout.newTab().setText("Technology"))

        binding.tapLayout.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when(tab.text){
                        "Breaking News"->{viewModel.getNews(category = "general")}
                        "Business"->{viewModel.getNews(category = "business")}
                        "Entertainment"->{viewModel.getNews(category = "entertainment")}
                        "Health"->{viewModel.getNews(category = "health")}
                        "Science"->{viewModel.getNews(category = "science")}
                        "Sports"->{viewModel.getNews(category = "sports")}
                        "Technology"->{viewModel.getNews(category = "technology")}
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })

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
        newsAdapter= NewsAdapter(){
            viewModel.saveArticle(it)

        }
        binding.rvBreakingNews.apply {
            adapter=newsAdapter
            layoutManager= LinearLayoutManager(activity)
        }
    }
}
