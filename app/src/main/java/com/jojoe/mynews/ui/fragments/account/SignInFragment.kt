package com.jojoe.mynews.ui.fragments.account

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jojoe.mynews.R
import com.jojoe.mynews.databinding.FragmentSignInBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception

class SignInFragment :Fragment(R.layout.fragment_sign_in){
    private lateinit var binding:FragmentSignInBinding

    lateinit var auth:FirebaseAuth
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding=FragmentSignInBinding.bind(view)

        auth=FirebaseAuth.getInstance()
        binding.cvToSignUp.setOnClickListener{
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }
        checkValidate()
        binding.btnLogin.setOnClickListener{
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            if(email.isNullOrEmpty()){
                binding.tilEmail.error = "Enter Your Email"
                binding.tilEmail.isErrorEnabled=true
                return@setOnClickListener
            }
            if(password.isNullOrEmpty()){
                binding.tilPassword.error="Enter Password"
                binding.tilPassword.isErrorEnabled = true
                return@setOnClickListener
            }
           else {
                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                    if(it.isSuccessful){
                        findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToHomeNewsFragment())
                    }else{
                        Toast.makeText(requireContext(),it.exception?.message.toString(), Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(auth.currentUser!=null){
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToHomeNewsFragment())
        }
    }
    private fun checkValidate(){
        binding.etEmail.doAfterTextChanged { s->
            if(!s.isNullOrEmpty()){
                    binding.tilEmail.isErrorEnabled = false
            }
        }
        binding.etPassword.doAfterTextChanged { s->
            if(!s.isNullOrEmpty()){
                binding.tilPassword.isErrorEnabled = false
            }
        }
    }

}