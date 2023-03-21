package com.jefisu.manualplus.features_user.presentation.profile_user

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.presentation.SharedViewModel
import com.jefisu.manualplus.core.presentation.ThemeViewModel
import com.jefisu.manualplus.core.presentation.components.CustomTextField
import com.jefisu.manualplus.core.presentation.ui.theme.dark_background_text_field
import com.jefisu.manualplus.core.presentation.ui.theme.light_background_text_field
import com.jefisu.manualplus.core.presentation.ui.theme.spacing
import com.jefisu.manualplus.core.util.Theme
import com.jefisu.manualplus.features_user.presentation.profile_user.components.BottomContentPattern
import com.jefisu.manualplus.features_user.presentation.profile_user.components.CustomRadioButton
import com.jefisu.manualplus.features_user.presentation.profile_user.components.GalleryUploader
import com.jefisu.manualplus.features_user.presentation.profile_user.util.SettingsUser
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.stevdzasan.messagebar.ContentWithMessageBar
import com.stevdzasan.messagebar.rememberMessageBarState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Destination
@Composable
fun ProfileUserScreen(
    logout: () -> Unit,
    navigator: DestinationsNavigator,
    sharedViewModel: SharedViewModel,
    themeViewModel: ThemeViewModel,
    viewModel: ProfileUserViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val sharedState by sharedViewModel.state.collectAsState()

    val languages = stringArrayResource(R.array.languages)

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val messageBarState = rememberMessageBarState()
    val focusManager = LocalFocusManager.current
    var languageSelected by remember { mutableStateOf(languages.first()) }

    LaunchedEffect(key1 = viewModel.uiEvent) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ProfileUserViewModel.UiEvent.HideBottomSheet -> sheetState.hide()
                is ProfileUserViewModel.UiEvent.ErrorMessage -> {
                    messageBarState.addError(
                        Exception(event.uiText?.asString(context))
                    )
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
                when (state.settings) {
                    SettingsUser.EditProfile -> {
                        BottomContentPattern(
                            title = state.settings.value,
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
                                        nameTextField = "Name",
                                        iconView = R.drawable.ic_user,
                                        imeAction = ImeAction.Done,
                                        keyboardAction = focusManager::clearFocus
                                    )
                                }
                            }
                        )
                    }

                    SettingsUser.Language -> {
                        BottomContentPattern(
                            title = state.settings.value,
                            scope = scope,
                            sheetState = sheetState,
                            height = 229.dp,
                            onSaveClick = { /*TODO*/ },
                            content = {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    languages.forEach { language ->
                                        CustomRadioButton(
                                            text = language,
                                            selected = language == languageSelected,
                                            onSelectChange = { languageSelected = language }
                                        )
                                    }
                                }
                            }
                        )
                    }

                    SettingsUser.ContactUs -> {
                        BottomContentPattern(
                            title = state.settings.value,
                            scope = scope,
                            sheetState = sheetState,
                            height = 396.dp,
                            textButton = "Send",
                            onSaveClick = { /*TODO*/ },
                            content = {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(
                                        space = 12.dp,
                                        alignment = Alignment.CenterVertically
                                    ),
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    OutlinedTextField(
                                        value = "",
                                        onValueChange = { },
                                        shape = RoundedCornerShape(16.dp),
                                        colors = TextFieldDefaults.outlinedTextFieldColors(
                                            backgroundColor = if (isSystemInDarkTheme()) dark_background_text_field else light_background_text_field,
                                            textColor = MaterialTheme.colors.onBackground,
                                            focusedBorderColor = Color.Transparent,
                                            unfocusedBorderColor = Color.Transparent
                                        ),
                                        placeholder = { Text(text = "Write your problem here...") },
                                        modifier = Modifier
                                            .padding(horizontal = 32.dp)
                                            .fillMaxWidth()
                                            .height(170.dp)
                                            .border(
                                                width = 1.dp,
                                                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                    )
                                    GalleryUploader(
                                        imagesSelected = { uris -> },
                                        modifier = Modifier.padding(start = 32.dp)
                                    )
                                }
                            }
                        )
                    }

                    SettingsUser.Theme -> {
                        BottomContentPattern(
                            title = state.settings.value,
                            scope = scope,
                            sheetState = sheetState,
                            height = 268.dp,
                            onSaveClick = {
                                viewModel.saveTheme()
                                themeViewModel.selectTheme(state.theme)
                            },
                            onCloseClick = viewModel::resetOptionThemeSelected,
                            content = {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    modifier = Modifier.fillMaxSize()
                                ) {
                                    Theme.values().forEach {
                                        CustomRadioButton(
                                            text = stringResource(id = it.res),
                                            selected = state.theme == it,
                                            onSelectChange = { viewModel.selectTheme(it) }
                                        )
                                    }
                                }
                            }
                        )
                    }

                    SettingsUser.Logout -> {
                        Column(
//                        verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .padding(MaterialTheme.spacing.medium)
                                .fillMaxWidth()
                                .height(150.dp)
                                .clip(RoundedCornerShape(MaterialTheme.spacing.medium))
                                .background(MaterialTheme.colors.background)
                                .padding(
                                    horizontal = 32.dp,
                                    vertical = MaterialTheme.spacing.medium
                                )
                        ) {
                            Text(
                                text = stringResource(R.string.are_you_sure_you_want_sign_out),
                                style = MaterialTheme.typography.body1,
                                color = MaterialTheme.colors.onBackground
                            )
                            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                            Text(
                                text = "Yes",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.h6,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        viewModel.logout()
                                        logout()
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
                                text = "No",
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
                        Box {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colors.primary)
                            ) {
                                AsyncImage(
                                    model = ImageRequest.Builder(context)
                                        .data(sharedState.avatarUri)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .offset(y = 11.dp)
                                )
                            }
                            Icon(
                                painter = painterResource(R.drawable.ic_edit),
                                contentDescription = null,
                                tint = MaterialTheme.colors.background,
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.BottomEnd)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colors.onBackground)
                            )
                        }
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
                        onClick = navigator::popBackStack,
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
                            text = setting.value,
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