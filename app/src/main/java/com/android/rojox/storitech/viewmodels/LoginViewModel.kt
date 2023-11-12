package com.android.rojox.storitech.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.rojox.core.CoreModule
import com.android.rojox.core.domain.response.AuthenticationResponse
import com.android.rojox.core.model.DataState
import com.android.rojox.core.model.UserSession
import com.android.rojox.core.remote.DbClient
import com.android.rojox.core.repository.impl.LoginRepositoryImpl
import com.android.rojox.core.utils.CoreUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel(dbClient: DbClient): ViewModel() {

    private val repository = LoginRepositoryImpl(dbClient)

    var userSession = MutableStateFlow(null as DataState<UserSession>?)

    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val isLoginEnabled: StateFlow<Boolean> = combine(email, password) { email, password ->
        CoreUtils.isValidPassword(password) && CoreUtils.isValidEmail(email)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)


    fun login() {
        userSession.value = DataState()
        viewModelScope.launch {
            userSession.value = repository.login(email.value, password.value)
            Log.i("LOGIN", userSession.value.toString())
        }
    }
}