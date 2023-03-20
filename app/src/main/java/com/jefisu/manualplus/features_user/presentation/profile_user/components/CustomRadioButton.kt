package com.jefisu.manualplus.features_user.presentation.profile_user.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.jefisu.manualplus.core.presentation.ui.theme.spacing

@Composable
fun CustomRadioButton(
    text: String,
    selected: Boolean,
    onSelectChange: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 24.dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelectChange() }
            .padding(
                horizontal = 20.dp,
                vertical = MaterialTheme.spacing.small
            )
            .height(height)
            .then(modifier)
    ) {
        RadioButton(
            selected = selected,
            onClick = { onSelectChange() },
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colors.onBackground,
                unselectedColor = MaterialTheme.colors.onBackground.copy(0.6f)
            )
        )
        Text(
            text = text,
            style = MaterialTheme.typography.body2,
            color = MaterialTheme.colors.onBackground,
        )
    }
}