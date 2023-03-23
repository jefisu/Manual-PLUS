package com.jefisu.manualplus.features_manual.presentation.home.components

import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.presentation.ui.theme.spacing
import com.jefisu.manualplus.features_manual.domain.Equipment

data class EquipmentInfo(
    val name: String,
    @DrawableRes val icon: Int
)

@Composable
fun ListItem(
    equipment: Equipment,
    imageUri: Uri,
    onClickNavigate: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground
) {
    val infos = listOf(
        EquipmentInfo(equipment.serialNumber.toString(), R.drawable.ic_hash),
        EquipmentInfo(equipment.releaseYear.toString(), R.drawable.ic_calendar),
        EquipmentInfo(equipment.category, R.drawable.ic_category)
    )
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
            .clickable { onClickNavigate() }
    ) {
        Box(
            modifier = Modifier
                .width(155.dp)
                .height(194.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.primary)
                .padding(12.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(imageUri)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Spacer(modifier = Modifier.width(24.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.spacing.medium,
                Alignment.CenterVertically
            ),
            modifier = Modifier
                .weight(1f)
                .padding(top = MaterialTheme.spacing.small)
        ) {
            Text(
                text = equipment.name.uppercase(),
                style = MaterialTheme.typography.h5,
                color = color
            )
            for (info in infos) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(info.icon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = color
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = info.name,
                        style = MaterialTheme.typography.body2,
                        color = color
                    )
                }
            }
        }
    }
}