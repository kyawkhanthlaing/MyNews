package com.jojoe.mynews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jojoe.mynews.R
import com.jojoe.mynews.databinding.ActivityNewsBinding
import com.jojoe.mynews.db.ArticleDatabase
import com.jojoe.mynews.repository.NewsRepository

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val  newsRepository= NewsRepository(ArticleDatabase(this))
        val viewModelFactory=NewsViewModelFactory(newsRepository)
        viewModel= ViewModelProvider(this,viewModelFactory)[NewsViewModel::class.java]

        val navHostFrag = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navHostFrag.navController.apply {
            binding.bottomNavigationView.setupWithNavController(this)
        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.breakingNewsFragment, R.id.savedNewsFragment, R.id.searchNewsFragment
            )
        )
        setupActionBarWithNavController(navHostFrag.navController, appBarConfiguration)

    }


    override fun onSupportNavigateUp(): Boolean
    {
        val navController = this.findNavController(R.id.fragmentContainerView)
        return navController.navigateUp()
    }

}