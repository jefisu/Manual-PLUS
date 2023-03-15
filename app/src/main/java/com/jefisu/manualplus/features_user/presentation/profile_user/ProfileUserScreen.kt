package com.jefisu.manualplus.features_user.presentation.profile_user

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.components.CustomTextField
import com.jefisu.manualplus.core.ui.theme.ManualPLUSTheme
import com.jefisu.manualplus.core.ui.theme.dark_background_text_field
import com.jefisu.manualplus.core.ui.theme.light_background_text_field
import com.jefisu.manualplus.core.ui.theme.spacing
import com.jefisu.manualplus.features_user.presentation.profile_user.components.BottomContentPattern
import com.jefisu.manualplus.features_user.presentation.profile_user.components.CustomRadioButton
import com.jefisu.manualplus.features_user.presentation.profile_user.components.GalleryUploader
import com.jefisu.manualplus.features_user.presentation.profile_user.util.SettingsUser
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileUserScreen(
    @DrawableRes avatar: Int,
    name: String,
    email: String
) {
    val languages = stringArrayResource(R.array.languages)
    val themeDevice = stringArrayResource(R.array.themeDevice)

    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    val scope = rememberCoroutineScope()
    var settingsClicked by remember { mutableStateOf(SettingsUser.Language) }
    var languageSelected by remember { mutableStateOf(languages.first()) }
    var themeSelected by remember { mutableStateOf(themeDevice.first()) }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetElevation = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        sheetBackgroundColor = if (settingsClicked == SettingsUser.Logout) Color.Transparent else MaterialTheme.colors.background,
        sheetContent = {
            when (settingsClicked) {
                SettingsUser.EditProfile -> {
                    BottomContentPattern(
                        title = settingsClicked.value,
                        scope = scope,
                        sheetState = sheetState,
                        height = 291.dp,
                        onSaveClick = { /*TODO*/ },
                        content = {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 32.dp)
                            ) {
                                CustomTextField(
                                    text = "",
                                    onTextChange = {},
                                    placeholderText = "John Doe",
                                    nameTextField = "Name",
                                    iconView = R.drawable.ic_user
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                CustomTextField(
                                    text = "",
                                    onTextChange = {},
                                    placeholderText = "john.doe@example.com",
                                    nameTextField = "Email",
                                    iconView = R.drawable.ic_email
                                )
                            }
                        }
                    )
                }

                SettingsUser.Language -> {
                    BottomContentPattern(
                        title = settingsClicked.value,
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
                        title = settingsClicked.value,
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
                        title = settingsClicked.value,
                        scope = scope,
                        sheetState = sheetState,
                        height = 268.dp,
                        onSaveClick = { /*TODO*/ },
                        content = {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxSize()
                            ) {
                                themeDevice.forEach { theme ->
                                    CustomRadioButton(
                                        text = theme,
                                        selected = themeSelected == theme,
                                        onSelectChange = { themeSelected = theme }
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
                                .clickable { }
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
            modifier = Modifier.fillMaxSize()
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
                            Image(
                                painter = painterResource(avatar),
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
                            text = name,
                            style = MaterialTheme.typography.h4,
                            color = MaterialTheme.colors.background,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = email,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.background,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                IconButton(
                    onClick = { /*TODO*/ },
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
                            settingsClicked = setting
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

@Preview
@Composable
fun PreviewProfileUserScreen() {
    ManualPLUSTheme {
        Surface {
            ProfileUserScreen(
                avatar = R.drawable.artboards_diversity_avatars_by_netguru_16,
                name = "John Doe",
                email = "john.doe@example.com",
            )
        }
    }
}