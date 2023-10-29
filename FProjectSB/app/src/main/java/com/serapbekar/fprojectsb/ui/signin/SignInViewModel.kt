package com.serapbekar.fprojectsb.ui.signin

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
class SignInViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    private var _signinState = MutableLiveData<SignInState>()
    val signinState: LiveData<SignInState>
        get() = _signinState

    val currentUser: FirebaseUser?
        get() = userRepository.currentUser


    fun signInUser(email: String, password: String) {
        viewModelScope.launch {
            _signinState.value = SignInState.Loading

            val result = userRepository.signInUser(email, password)

            if (result is Resource.Success) {
                _signinState.value = SignInState.Success(result.data)
            } else if (result is Resource.Error) {
                _signinState.value = SignInState.Error(result.throwable)
            }
        }
    }
    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }
}

sealed interface SignInState {
    data class Success(val user: FirebaseUser) : SignInState
    data class Error(val throwable: Throwable) : SignInState
    object Loading : SignInState
}