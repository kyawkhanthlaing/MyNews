package com.jojoe.mynews.ui.fragments.details

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.jojoe.mynews.R
import com.jojoe.mynews.databinding.FragmentArticleBinding
import com.jojoe.mynews.ui.NewsActivity
import com.jojoe.mynews.ui.NewsViewModel

class ArticleFragment:Fragment(R.layout.fragment_article) {
    lateinit var viewModel: NewsViewModel
    private lateinit var binding: FragmentArticleBinding
    val args:ArticleFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentArticleBinding.bind(view)
        viewModel = (activity as NewsActivity).viewModel

        requireActivity().onBackPressedDispatcher.addCallback(this){
            if (binding.webView.canGoBack()){
                binding.webView.goBack()
            }
            else{
                findNavController().popBackStack()
            }
        }
        val article=args.article
        binding.webView.apply {
            settings.javaScriptEnabled=true
            webViewClient= object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    showProgressBar()
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    hideProgressBar()
                    super.onPageFinished(view, url)
                }

            }
            loadUrl(article.url)
        }

        binding.fab.setOnClickListener{
            viewModel.saveArticle(article)
            Snackbar.make(view,"Saved successfully",Snackbar.LENGTH_SHORT).show()

        }
    }

    private fun hideProgressBar() {
        binding.paginationProgressBar.visibility=View.INVISIBLE
    }
    private fun showProgressBar() {
        binding.paginationProgressBar.visibility=View.VISIBLE
    }
}