package com.android.rojox.storitech.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.android.rojox.core.domain.request.LoginRequest
import com.android.rojox.core.domain.request.OnboardingRequest
import com.android.rojox.core.domain.response.AccountInfoResponse
import com.android.rojox.core.domain.response.AuthenticationResponse
import com.android.rojox.core.domain.response.MovementResponse
import com.android.rojox.core.remote.DbClient
import com.android.rojox.core.remote.StorageClient
import com.android.rojox.storitech.ui.navigation.NavItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


fun NavGraphBuilder.composable(
    navItem: NavItem,
    content: @Composable (NavBackStackEntry) -> Unit) {
    composable(
        route= navItem.baseRoute,
    ) {
        content(it)
    }
}


@Composable
fun <T> Flow<T>.observeAsActions(onEach: (T) -> Unit) {
    val flow = this
    LaunchedEffect(key1 = flow) {
        flow.onEach(onEach).collect()
    }
}


fun Double.amountFormat(): String {
    val roundOff = (this * 100.0).toInt() / 100.0
    val parts = "$roundOff".split(".")
    val numbers = parts[0]
        .reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()
    val decimals = if (parts[1].length == 1){
        parts[1]+"0"
    } else {
        parts[1]
    }
    return "$${numbers}.$decimals"
}


val mockDbClient = object : DbClient {
    override suspend fun registerUser(onboardingRequest: OnboardingRequest): AuthenticationResponse {
        TODO("Not yet implemented")
    }

    override suspend fun login(loginRequest: LoginRequest): AuthenticationResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountInfo(userId: String): AccountInfoResponse {
        TODO("Not yet implemented")
    }

    override suspend fun getAccountMovements(accountId: String): List<MovementResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun validateAccount(userId: String) {
        TODO("Not yet implemented")
    }
}

val mockStorageClient = object : StorageClient {
    override suspend fun uploadFile(userId: String, byteArray: ByteArray): String {
        TODO("Not yet implemented")
    }

}