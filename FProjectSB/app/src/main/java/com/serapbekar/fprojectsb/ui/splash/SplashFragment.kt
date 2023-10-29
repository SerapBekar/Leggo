package com.serapbekar.fprojectsb.ui.splash

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.serapbekar.fprojectsb.MainActivity
import com.serapbekar.fprojectsb.R
import com.serapbekar.fprojectsb.common.viewBinding
import com.serapbekar.fprojectsb.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val viewModel by viewModels<SplashViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000)
            viewModel.checkUserLogin()
        }

        initObservers()
    }
        private fun initObservers() {
            viewModel.splashState.observe(viewLifecycleOwner) {
                when (it) {
                    is SplashState.GoToHome -> { findNavController().navigate(R.id.splashToHome) }

                    is SplashState.GoToSignIn -> { findNavController().navigate(R.id.splashToSignIn) }
                }
            }
        }
    }