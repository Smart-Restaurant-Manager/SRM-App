package com.srm.srmapp.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.srm.srmapp.R
import com.srm.srmapp.Resource
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.databinding.FragmentLoginBinding
import com.srm.srmapp.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment(), MainActivity.FragmentName, View.OnClickListener {

    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var userSession: UserSession

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        setupView()
        setupObservables()
        return binding.root
    }

    private fun setupView() {
        binding.btLogin.setOnClickListener(this)
        binding.btFormToggle.setOnClickListener(this)
        binding.btSignup.setOnClickListener(this)
        binding.btLogout.setOnClickListener(this)
    }

    private fun setupObservables() {
        viewModel.getLoginState().observe(requireActivity()) { response ->
            Timber.d("LoginState updated $response.m")
            when (response) {
                is Resource.Success -> {
                    binding.tvStatus.text = "${response.data}"
                }
                is Resource.Loading -> {
                    binding.tvStatus.text = requireActivity().getString(R.string.loginLoading)
                }
                is Resource.Error -> {
                    binding.tvStatus.text = response.message
                }
            }
        }

        viewModel.getSignupState().observe(requireActivity()) { response ->
            Timber.d("Sign up updated $response.m")
            when (response) {
                is Resource.Success -> {
                    binding.tvStatus.text = response.data
                }
                is Resource.Loading -> {
                    binding.tvStatus.text = getString(R.string.singupLoading)
                }
                is Resource.Error -> {
                    binding.tvStatus.text = response.message
                }
            }
        }

        userSession.getUser().observe(requireActivity()) {
            Timber.d("User updated")
            if (it != null) {
                binding.btLogout.isEnabled = true
                binding.btLogin.isEnabled = false
                binding.tvLoginStatus.text = "Logged in ${it.email}"
            } else {
                binding.btLogout.isEnabled = false
                binding.btLogin.isEnabled = true
                binding.tvLoginStatus.text = "Not logged in"
            }
        }
    }

    override fun onClick(p0: View) {
        when (p0.id) {
            R.id.btLogin -> {
                val user = binding.edUsername.text.toString().ifBlank { "frank@srm.com" }
                val password = binding.edPassword.text.toString().ifBlank { "frank.srm" }
                viewModel.login(user, password)
            }

            R.id.btFormToggle -> {
                if (viewModel.getFormtoggle()) {
                    binding.formLogin.visibility = View.GONE
                    binding.formSignup.visibility = View.VISIBLE
                } else {
                    binding.formLogin.visibility = View.VISIBLE
                    binding.formSignup.visibility = View.GONE
                }
                viewModel.toggleForm()
            }

            R.id.btSignup -> {
                binding.apply {
                    val email = edEmail.text.toString().ifBlank { "frank@srm.com" }
                    val name = edName.text.toString().ifBlank { "Frank" }
                    val password = edPasswordSignup.text.toString().ifBlank { "frank.srm" }
                    val passwordCheck = edPasswordSignupCheck.text.toString().ifBlank { "frank.srm" }
                    viewModel.signup(email, name, password, passwordCheck)
                }
            }
            R.id.btLogout -> {
                viewModel.logout()
            }
        }
    }

    override fun getName(): String {
        return "Login"
    }
}