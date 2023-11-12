package com.android.rojox.storitech.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.rojox.core.domain.response.AuthenticationResponse
import com.android.rojox.core.model.DataState
import com.android.rojox.core.model.DataStatus
import com.android.rojox.core.model.UserSession
import com.android.rojox.core.remote.DbClient
import com.android.rojox.core.remote.StorageClient
import com.android.rojox.core.repository.impl.OnboardingRepositoryImpl
import com.android.rojox.core.utils.CoreUtils
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class OnboardingViewModel(
    dbClient: DbClient,
    storageClient: StorageClient
): ViewModel() {

    private val repository = OnboardingRepositoryImpl(dbClient, storageClient)


    val imagePath = MutableStateFlow(null as DataState<String>?)
    val userSession = MutableStateFlow(null as DataState<UserSession>?)
    val validateUser = MutableStateFlow(null as DataState<Unit>?)

    val isLoading: StateFlow<Boolean> = userSession.combine(validateUser) { register, validate ->
        register?.status == DataStatus.LOADING || validate?.status == DataStatus.LOADING
    }.combine(imagePath) { combined, imageUpload ->
        combined || imageUpload?.status == DataStatus.LOADING
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    val email = MutableStateFlow("")
    val name = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val password = MutableStateFlow("")
    val imageByteArray = MutableStateFlow(null as ByteArray?)

    val isRegisterBtnEnabled: StateFlow<Boolean> = name.combine(lastName) { name, lastName ->
        name.isNotBlank() && lastName.isNotBlank()
    }.combine(password) { combined, password ->
        combined && CoreUtils.isValidPassword(password)
    }.combine(email){ combined, email ->
        combined && CoreUtils.isValidEmail(email)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)


    fun registerUser() {
        userSession.value = DataState()
        viewModelScope.launch {
            userSession.value = repository.registerUser(
                name.value, lastName.value,
                email.value, password.value)
        }

    }

    fun uploadImage() {
        imagePath.value = DataState()
        viewModelScope.launch {
            imagePath.value = repository.uploadImage(
                userSession.value!!.data!!.token,
                imageByteArray.value!!
            )
            if (imagePath.value?.status == DataStatus.SUCCESS) {
                validateUser.value = repository.validateUser(userSession.value!!.data!!.token)
            }
        }
    }

    fun clearData() {
        email.value = ""
        name.value = ""
        lastName.value = ""
        password.value = ""
        imagePath.value = null
        userSession.value = null
        validateUser.value = null
    }
}