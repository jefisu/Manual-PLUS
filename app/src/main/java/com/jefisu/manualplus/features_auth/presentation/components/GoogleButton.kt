package com.jefisu.manualplus.features_auth.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jefisu.manualplus.BuildConfig
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.presentation.components.CustomButton
import com.jefisu.manualplus.core.util.Resource
import com.jefisu.manualplus.core.util.UiText
import com.stevdzasan.onetap.OneTapSignInWithGoogle
import com.stevdzasan.onetap.rememberOneTapSignInState

@Composable
fun GoogleButton(
    onResult: (Resource<String>) -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    val state = rememberOneTapSignInState()

    OneTapSignInWithGoogle(
        state = state,
        clientId = BuildConfig.client_id,
        onTokenIdReceived = { token ->
            onResult(Resource.Success(token))
        },
        onDialogDismissed = { errorMessage ->
            onResult(
                Resource.Error(UiText.DynamicString(errorMessage))
            )
        }
    )

    CustomButton(
        text = "Sign In with Google",
        icon = R.drawable.google_logo,
        isLoading = isLoading,
        modifier = modifier,
        onClick = state::open
    )
}