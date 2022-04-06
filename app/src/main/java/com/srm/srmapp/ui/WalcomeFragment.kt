package com.srm.srmapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.srm.srmapp.R
import com.srm.srmapp.data.UserSession
import com.srm.srmapp.databinding.FragmentWalcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WalcomeFragment : Fragment() {
    private lateinit var binding: FragmentWalcomeBinding

    @Inject
    lateinit var userSession: UserSession


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentWalcomeBinding.inflate(inflater, container, false)
        setupView()
        return binding.root
    }

    private fun setupView() {
        userSession.getUser().observe(viewLifecycleOwner) { user ->
            binding.apply {
                if (user == null) { // not logged in
                    btLogout.isEnabled = false
                    btTapToEnter.setOnClickListener {
                        findNavController().navigate(R.id.action_walcomeFragment_to_loginregisterFragment)
                    }
                } else { // logged in
                    btLogout.isEnabled = true
                    btTapToEnter.setOnClickListener {
                        findNavController().navigate(R.id.action_walcomeFragment_to_managerFragment)
                    }
                }
            }
        }
        binding.btLogout.setOnClickListener {
            userSession.logout()
        }
    }
}