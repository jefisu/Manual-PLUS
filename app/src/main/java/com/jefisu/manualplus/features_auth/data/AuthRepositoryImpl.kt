package com.jefisu.manualplus.features_auth.data

import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.SimpleResource
import com.jefisu.manualplus.core.util.UiText
import com.jefisu.manualplus.features_auth.domain.AuthRepository
import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.exceptions.InvalidCredentialsException
import io.realm.kotlin.mongodb.exceptions.UserAlreadyExistsException

class AuthRepositoryImpl(
    private val app: App
) : AuthRepository {

    override suspend fun login(email: String, password: String): SimpleResource {
        return try {
            val isLogged = app
                .login(Credentials.emailPassword(email, password))
                .loggedIn
            if (isLogged) Resource.Success(Unit) else Resource.Error(UiText.unknownError())
        } catch (e: InvalidCredentialsException) {
            Resource.Error(UiText.StringResource(R.string.invalid_e_mail_or_password))
        } catch (_: Exception) {
            Resource.Error(UiText.unknownError())
        }
    }

    override suspend fun loginGoogle(token: String): SimpleResource {
        return try {
            val isLogged = app
                .login(Credentials.jwt(token))
                .loggedIn
            if (isLogged) Resource.Success(Unit) else Resource.Error(UiText.unknownError())
        } catch (_: Exception) {
            Resource.Error(UiText.unknownError())
        }
    }

    override suspend fun signUp(email: String, password: String): SimpleResource {
        return try {
            app.emailPasswordAuth.registerUser(email, password)
            login(email, password)
        } catch (_: UserAlreadyExistsException) {
            Resource.Error(UiText.StringResource(R.string.email_already_used_by_another_user))
        } catch (_: Exception) {
            Resource.Error(UiText.unknownError())
        }
    }
}