package com.serapbekar.fprojectsb.ui.success

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.serapbekar.fprojectsb.R
import com.serapbekar.fprojectsb.common.gone
import com.serapbekar.fprojectsb.common.viewBinding
import com.serapbekar.fprojectsb.databinding.FragmentPaymentSuccessBinding
import com.serapbekar.fprojectsb.ui.home.HomeState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PaymentSuccessFragment : Fragment(R.layout.fragment_payment_success ) {

    private val binding by viewBinding(FragmentPaymentSuccessBinding::bind)
    private val viewModel by viewModels<PaymentSuccessViewModel>()
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth

        playAnimation()

        binding.btnHome.setOnClickListener {
            onGoToHomeClick()
            viewModel.clearCart(auth.currentUser?.uid.toString())
        }
    }

    private fun playAnimation() = with(binding) {
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            lottieCargo.playAnimation()
        }, 2000)
    }

    private fun onGoToHomeClick() = with(binding) {
        findNavController().navigate(R.id.homeFragment,
                null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.cartFragment, true)
                    .build()
            )
    }
}