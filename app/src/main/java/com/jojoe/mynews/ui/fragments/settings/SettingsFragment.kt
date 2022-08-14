package com.jojoe.mynews.ui.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jojoe.mynews.R
import com.jojoe.mynews.databinding.FragmentSettingsBinding
import com.jojoe.mynews.ui.fragments.account.SignUpFragmentDirections

class SettingsFragment :Fragment(R.layout.fragment_settings){

    private lateinit var binding:FragmentSettingsBinding

    lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding= FragmentSettingsBinding.bind(view)

        auth=FirebaseAuth.getInstance()
        binding.apply {
            cvAnotherLogin.setOnClickListener{
                auth.signOut()
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToSignInFragment())
            }
            cvCreateNew.setOnClickListener{
                auth.signOut()
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToSignUpFragment())
            }
            cvSavedNews.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToSavedNewsFragment())
            }
            btnLogout.setOnClickListener{
                auth.signOut()
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToSignInFragment())
            }

        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser==null){
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToSignInFragment())
        }
    }
}