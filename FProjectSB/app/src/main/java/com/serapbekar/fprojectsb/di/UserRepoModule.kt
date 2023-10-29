package com.serapbekar.fprojectsb.di

import com.google.firebase.auth.FirebaseAuth
import com.serapbekar.fprojectsb.data.repository.User
import com.serapbekar.fprojectsb.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UserRepoModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideUserRepository(userRepository: UserRepository) : User = userRepository

}