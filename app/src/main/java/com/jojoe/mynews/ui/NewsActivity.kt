package com.jojoe.mynews.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jojoe.mynews.MyNewsApp
import com.jojoe.mynews.R
import com.jojoe.mynews.databinding.ActivityNewsBinding

class NewsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNewsBinding
    lateinit var viewModel: NewsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

       val container = (application as MyNewsApp).container
        val viewModelFactory=NewsViewModelFactory(container.provideRepository())
        viewModel= ViewModelProvider(this,viewModelFactory)[NewsViewModel::class.java]

        val navHostFrag = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navHostFrag.navController.apply {
            addOnDestinationChangedListener{
                _,listner,_->binding.bottomNavigationView.isVisible=listner.id !in listOf(R.id.signInFragment,R.id.signUpFragment)

            }
            binding.bottomNavigationView.setupWithNavController(this)
        }
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeNewsFragment, R.id.settingsFragment, R.id.searchNewsFragment,R.id.signUpFragment,R.id.signInFragment
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