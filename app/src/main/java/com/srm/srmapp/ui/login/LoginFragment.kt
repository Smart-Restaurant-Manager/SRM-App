package com.srm.srmapp.ui.login

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.databinding.LoginFragmentBinding
import com.srm.srmapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment(), MainActivity.FragmentName, View.OnClickListener {
    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var binding: LoginFragmentBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        setupListeners()
        setupObservables()
        return binding.root
    }

    private fun setupListeners() {
        binding.bLogin.setOnClickListener(this)
    }

    private fun setupObservables() {
        viewModel.getLoginState().observe(requireActivity()) { response ->
            Timber.d("LoginState updated $response.m")
            when (response) {
                is Resource.Success -> {
                    binding.tvLoginStatus.apply {
                        text = response.data?.token
                    }
                }
                is Resource.Loading -> {
                    binding.tvLoginStatus.apply {
                        text = context.getString(R.string.loginLoading)
                    }
                }
                is Resource.Error -> {
                    binding.tvLoginStatus.apply {
                        text = response.message
                    }
                }
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.bLogin -> {
                val user = binding.edUsername.text.toString()
                val password = binding.edPassword.text.toString()
                viewModel.login(user, password)
            }
        }
    }

    override fun getName(): String {
        return "Login"
    }
}