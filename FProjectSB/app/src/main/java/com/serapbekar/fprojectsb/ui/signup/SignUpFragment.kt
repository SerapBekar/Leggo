package com.serapbekar.fprojectsb.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.serapbekar.fprojectsb.R
import com.serapbekar.fprojectsb.common.gone
import com.serapbekar.fprojectsb.common.visible
import com.serapbekar.fprojectsb.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel by viewModels<SignUpViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentUser?.let { findNavController().navigate(R.id.action_signUpFragment_to_homeFragment) }

        with(binding) {
            btnSignUp.setOnClickListener {
                val email = etEmail.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty() && password.length >= 6) {
                    viewModel.signUpUser(email, password)
                } else {
                    Toast.makeText(
                        requireContext(),
                        "\n" +
                                "Please fill in the fields and format appropriately!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                tvGoSignin.setOnClickListener {
                    findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                }
            }
            observeData()
        }
    }

    private fun observeData() = with(binding) {

        viewModel.signUpState.observe(viewLifecycleOwner) { signupstate ->
            when (signupstate) {
                is SignUpState.Success -> {
                    findNavController().navigate(R.id.action_signUpFragment_to_homeFragment)
                }
                is SignUpState.Error -> {
                    Snackbar.make(
                        requireView(),
                        signupstate.throwable.message.orEmpty(),
                        1000
                    ).show()
                }
                else -> {}
            }
        }
    }
}