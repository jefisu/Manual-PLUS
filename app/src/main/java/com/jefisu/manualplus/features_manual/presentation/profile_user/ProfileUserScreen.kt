package com.jefisu.manualplus.features_manual.presentation.profile_user

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.presentation.components.AvatarImage
import com.jefisu.manualplus.core.presentation.components.CustomTextField
import com.jefisu.manualplus.core.presentation.ui.theme.spacing
import com.jefisu.manualplus.destinations.AuthScreenDestination
import com.jefisu.manualplus.destinations.AvatarsUserScreenDestination
import com.jefisu.manualplus.features_manual.presentation.SharedState
import com.jefisu.manualplus.features_manual.presentation.profile_user.components.BottomContentPattern
import com.jefisu.manualplus.features_manual.presentation.profile_user.components.GalleryUploader
import com.jefisu.manualplus.features_manual.presentation.profile_user.util.SettingsUser
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.navigate
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.rememberMessageBarState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun ProfileUserScreen(
    navController: NavController,
    sharedState: SharedState,
    viewModel: ProfileUserViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val messageBarState = rememberMessageBarState()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ProfileUserViewModel.UiEvent.HideBottomSheet -> sheetState.hide()
                is ProfileUserViewModel.UiEvent.ErrorMessage -> {
                    messageBarState.addError(
                        Exception(event.uiText?.asString(context))
                    )
                    sheetState.show()
                }
                is ProfileUserViewModel.UiEvent.SuccessMessage -> {
                    messageBarState.addSuccess(
                        event.uiText?.asString(context).orEmpty()
                    )
                }
            }
        }
    }

    ContentWithMessageBar(messageBarState = messageBarState) {
        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetElevation = 0.dp,
            sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
            sheetBackgroundColor = if (state.settings == SettingsUser.Logout) Color.Transparent else MaterialTheme.colors.background,
            sheetContent = {
                if (state.isLoading) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colors.onBackground)
                    }
                } else {
                    when (state.settings) {
                        SettingsUser.EditProfile -> {
                            BottomContentPattern(
                                title = stringResource(id = state.settings.res),
                                scope = scope,
                                sheetState = sheetState,
                                height = 219.dp,
                                onSaveClick = viewModel::saveInfoUpdated,
                                content = {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 32.dp)
                                    ) {
                                        CustomTextField(
                                            text = state.name,
                                            onTextChange = viewModel::enteredName,
                                            placeholderText = "John Doe",
                                            nameTextField = stringResource(R.string.name),
                                            iconView = R.drawable.ic_user,
                                            imeAction = ImeAction.Done,
                                            keyboardAction = focusManager::clearFocus
                                        )
                                    }
                                }
                            )
                        }

                        SettingsUser.ContactUs -> {
                            BottomContentPattern(
                                title = stringResource(id = state.settings.res),
                                scope = scope,
                                sheetState = sheetState,
                                height = 521.dp,
                                textButton = stringResource(R.string.send),
                                onSaveClick = viewModel::sendSupportRequest,
                                content = {
                                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                                    CustomTextField(
                                        text = state.hospitalName,
                                        onTextChange = viewModel::enteredHospitalName,
                                        placeholderText = stringResource(R.string.enter_the_name_hospital),
                                        nameTextField = stringResource(R.string.hospital),
                                        iconView = R.drawable.ic_hospital,
                                        imeAction = ImeAction.Next,
                                        keyboardAction = { focusManager.moveFocus(FocusDirection.Down) },
                                        modifier = Modifier.padding(horizontal = 32.dp)
                                    )
                                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                                    CustomTextField(
                                        text = state.hospitalAddress,
                                        onTextChange = viewModel::enteredHospitalAddress,
                                        placeholderText = stringResource(R.string.placeholder_address),
                                        nameTextField = stringResource(R.string.address),
                                        iconView = R.drawable.ic_location,
                                        imeAction = ImeAction.Next,
                                        keyboardAction = { focusManager.moveFocus(FocusDirection.Down) },
                                        modifier = Modifier.padding(horizontal = 32.dp)
                                    )
                                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                                    OutlinedTextField(
                                        value = state.supportMessage,
                                        onValueChange = viewModel::enteredSupportMessage,
                                        shape = RoundedCornerShape(16.dp),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            backgroundColor = Color.Transparent,
                                            textColor = MaterialTheme.colors.onBackground,
                                            focusedBorderColor = MaterialTheme.colors.onBackground,
                                            unfocusedBorderColor = MaterialTheme.colors.onBackground,
                                        ),
                                        placeholder = { Text(text = stringResource(R.string.write_your_problem_here)) },
                                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                        keyboardActions = KeyboardActions { focusManager.clearFocus() },
                                        modifier = Modifier
                                            .padding(horizontal = 32.dp)
                                            .fillMaxWidth()
                                            .height(170.dp)
                                    )
                                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                                    GalleryUploader(
                                        modifier = Modifier.padding(start = 32.dp),
                                        pickedImages = state.imagesToUpload,
                                        onSelectImages = viewModel::selectedImagesToUpload
                                    )
                                }
                            )
                        }

                        SettingsUser.Logout -> {
                            Column(
                                modifier = Modifier
                                    .padding(MaterialTheme.spacing.medium)
                                    .fillMaxWidth()
                                    .height(150.dp)
                                    .clip(RoundedCornerShape(MaterialTheme.spacing.medium))
                                    .background(MaterialTheme.colors.background)
                                    .padding(
                                        horizontal = 32.dp, vertical = MaterialTheme.spacing.medium
                                    )
                            ) {
                                Text(
                                    text = stringResource(R.string.are_you_sure_you_want_sign_out),
                                    style = MaterialTheme.typography.body1,
                                    color = MaterialTheme.colors.onBackground
                                )
                                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                                Text(
                                    text = stringResource(R.string.yes),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.logout()
                                            navController.backQueue.clear()
                                            navController.navigate(AuthScreenDestination)
                                        }
                                        .padding(vertical = MaterialTheme.spacing.small)
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(MaterialTheme.colors.onBackground)
                                )
                                Text(
                                    text = stringResource(R.string.no),
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.h6,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            scope.launch { sheetState.hide() }
                                        }
                                        .padding(vertical = MaterialTheme.spacing.small)
                                )
                            }
                        }
                    }
                }
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .background(MaterialTheme.colors.onBackground)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(start = 24.dp)
                            .fillMaxWidth()
                    ) {
                        AvatarImage(
                            image = sharedState.avatarUri,
                            iconAction = R.drawable.ic_edit,
                            size = 100.dp,
                            offsetY = 11.dp,
                            onClick = {
                                navController.navigate(AvatarsUserScreenDestination)
                            }
                        )
                        Spacer(modifier = Modifier.width(24.dp))
                        Column(
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                        ) {
                            Text(
                                text = sharedState.user?.name.orEmpty(),
                                style = MaterialTheme.typography.h4,
                                color = MaterialTheme.colors.background,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = sharedState.user?.email.orEmpty(),
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.background,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    IconButton(
                        onClick = navController::popBackStack,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(y = 25.dp)
                            .padding(end = 28.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_close_circle2),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground,
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colors.background)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(36.dp))
                SettingsUser.values().forEach { setting ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                viewModel.selectSetting(setting)
                                scope.launch {
                                    sheetState.show()
                                }
                            }
                            .padding(start = 24.dp, top = 12.dp, bottom = 12.dp)
                    ) {
                        Icon(
                            painter = painterResource(setting.icon),
                            contentDescription = null,
                            tint = MaterialTheme.colors.onBackground,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(modifier = Modifier.width(24.dp))
                        Text(
                            text = stringResource(id = setting.res),
                            style = MaterialTheme.typography.h5,
                            color = MaterialTheme.colors.onBackground
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}