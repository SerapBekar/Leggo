package com.serapbekar.fprojectsb.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.serapbekar.fprojectsb.common.Resource
import com.serapbekar.fprojectsb.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private var _signUpState = MutableLiveData<SignUpState>()
    val signUpState: LiveData<SignUpState>
        get() = _signUpState

    val currentUser: FirebaseUser?
        get() = userRepository.currentUser


    fun signUpUser(email: String, password: String) {
        viewModelScope.launch {
            _signUpState.value = SignUpState.Loading

            when(val result = userRepository.signUpUser(email, password)) {
                is Resource.Success -> {
                    _signUpState.value = SignUpState.Success(result.data)
                }

                is Resource.Error -> {
                    _signUpState.value = SignUpState.Error(result.throwable)
                }

                else -> {}
            }
        }
    }
}

sealed interface SignUpState {
    object Loading : SignUpState
    data class Success(val user: FirebaseUser) : SignUpState
    data class Error(val throwable: Throwable) : SignUpState
}
