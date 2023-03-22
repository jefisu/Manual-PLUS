package com.jefisu.manualplus.features_user.presentation.profile_user.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.presentation.components.CustomButton
import com.jefisu.manualplus.core.presentation.ui.theme.spacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BottomContentPattern(
    title: String,
    scope: CoroutineScope,
    sheetState: ModalBottomSheetState,
    onSaveClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit = {},
    textButton: String = "Save",
    height: Dp = 100.dp,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .padding(top = 20.dp, bottom = MaterialTheme.spacing.medium)
            .then(modifier)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 20.dp, end = 32.dp)
        ) {
            IconButton(onClick = {
                scope.launch { sheetState.hide() }
                onCloseClick()
            }) {
                Icon(
                    painter = painterResource(R.drawable.ic_close_circle),
                    contentDescription = null,
                    tint = MaterialTheme.colors.onBackground,
                    modifier = Modifier.size(30.dp)
                )
            }
            Text(
                text = title,
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.onBackground
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            content()
        }
        CustomButton(
            text = textButton,
            buttonShape = RoundedCornerShape(8.dp),
            padding = PaddingValues(horizontal = 32.dp),
            onClick = onSaveClick
        )
    }
}