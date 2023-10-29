package com.serapbekar.fprojectsb.ui.signin

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
import com.serapbekar.fprojectsb.databinding.FragmentSignInBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignInFragment : Fragment(R.layout.fragment_sign_in) {

    private lateinit var binding: FragmentSignInBinding
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.currentUser?.let { findNavController().navigate(R.id.signInToHome) }

        with(binding) {
            btnSignIn.setOnClickListener {
                val email = etName.text.toString()
                val password = etPassword.text.toString()

                if (email.isNotEmpty() && password.isNotEmpty() && password.length >= 6) {
                    viewModel.signInUser(email, password)
                } else {
                    Toast.makeText(requireContext(), "Please fill in the fields and format appropriately!", Toast.LENGTH_SHORT).show()
                }
            }
            tvGoSignup.setOnClickListener {
                findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
            }
        }
        observeData()
    }

    private fun observeData() = with(binding) {
        viewModel.signinState.observe(viewLifecycleOwner) { signinstate ->

            when (signinstate) {
                is SignInState.Success -> {
                    findNavController().navigate(R.id.signInToHome)
                }
                is SignInState.Error -> {
                    Snackbar.make(requireView(), signinstate.throwable.message.orEmpty(), 1000).show()
                }
                else -> {}
            }
        }
    }
}