package com.android.rojox.core.remote.impl

import com.android.rojox.core.domain.request.LoginRequest
import com.android.rojox.core.domain.request.OnboardingRequest
import com.android.rojox.core.domain.response.AccountInfoResponse
import com.android.rojox.core.domain.response.AuthenticationResponse
import com.android.rojox.core.domain.response.MovementResponse
import com.android.rojox.core.remote.DbClient
import com.android.rojox.core.utils.CodeException
import com.android.rojox.core.utils.Constants
import com.android.rojox.core.utils.CoreException
import com.android.rojox.core.utils.CoreUtils
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class DbClientImpl(private val db: FirebaseFirestore) : DbClient {

    override suspend fun registerUser(onboardingRequest: OnboardingRequest): AuthenticationResponse {

        findUserByEmail(onboardingRequest.email)?.apply {
            if (this.get(Constants.VALIDATED) as Boolean)
                throw CoreException(CodeException.USER_REGISTERED)
        }

        val snapshot = db.collection(Constants.USER_COLLECTION)
            .add(onboardingRequest)
            .await()

        return AuthenticationResponse(
            snapshot.id,
            onboardingRequest.name,
            onboardingRequest.email
        )
    }

    override suspend fun login(loginRequest: LoginRequest): AuthenticationResponse {

        findUserByEmail(loginRequest.email, loginRequest.password)?.apply {

            if (!(this.data?.get(Constants.VALIDATED) as Boolean)) {
                throw CoreException(CodeException.ACCOUNT_NOT_FOUND)
            }

            return AuthenticationResponse(
                this.id,
                this.get(Constants.NAME).toString(),
                loginRequest.email
            )
        }
        throw CoreException(CodeException.USER_NOT_FOUND)
    }

    override suspend fun getAccountInfo(userId: String): AccountInfoResponse {
        val snapshot = db.collection(Constants.ACCOUNT_COLLECTION)
            .whereEqualTo(Constants.USER_ID, userId)
            .get()
            .await()

        if (snapshot.documents.isEmpty()) {
            throw CoreException(CodeException.ACCOUNT_NOT_FOUND)
        }

        val data = HashMap(snapshot.documents[0].data!!)
        data["id"] = snapshot.documents[0].id

        return AccountInfoResponse.fromMap(data)
    }

    override suspend fun getAccountMovements(accountId: String): List<MovementResponse> {

        val am = db.collection(Constants.MOVEMENT_COLLECTION)
            .whereEqualTo(Constants.ACCOUNT_ID, accountId)
            .orderBy(Constants.TIMESTAMP, Query.Direction.DESCENDING)

        val snapshot = am.get().await()

        return snapshot.documents.map {
            MovementResponse.fromMap(it.data!!)
        }


    }

    override suspend fun validateAccount(userId: String) {

        val validated = hashMapOf(Constants.VALIDATED to true as Any)
        db.collection(Constants.USER_COLLECTION)
            .document(userId)
            .update(validated).await()
        val account = hashMapOf(
            Constants.NAME to "Cuenta corriente",
            Constants.BALANCE to 1000L,
            Constants.NUMBER to CoreUtils.generateAccountNumber(),
            Constants.USER_ID to userId
        )
        db.collection(Constants.ACCOUNT_COLLECTION)
            .add(account).await()
    }


    private suspend fun findUserByEmail(email: String, password: String? = null): DocumentSnapshot? {
        val snapshot = (
                password?.let {
                    db.collection(Constants.USER_COLLECTION)
                        .whereEqualTo(Constants.EMAIL, email)
                        .whereEqualTo(Constants.PASSWORD, password)
                }?: db.collection(Constants.USER_COLLECTION)
                    .whereEqualTo(Constants.EMAIL, email)
                ).get().await()


        return if (snapshot.documents.isNotEmpty()) {
            snapshot.documents[0]
        } else {
            null
        }
    }

}