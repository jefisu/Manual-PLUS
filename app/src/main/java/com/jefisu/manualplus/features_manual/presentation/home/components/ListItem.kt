package com.jefisu.manualplus.features_manual.presentation.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.ui.theme.spacing
import com.jefisu.manualplus.features_manual.domain.Equipment
import com.jefisu.manualplus.features_manual.domain.Instruction

data class EquipmentInfo(
    val name: String,
    @DrawableRes val icon: Int
)

@Composable
fun ListItem(
    equipment: Equipment,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onBackground
) {
    val infos = listOf(
        EquipmentInfo(equipment.serialNumber.toString(), R.drawable.ic_hash),
        EquipmentInfo(equipment.releaseYear.toString(), R.drawable.ic_calendar),
        EquipmentInfo(equipment.category, R.drawable.ic_category)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .width(155.dp)
                .height(194.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.primary)
                .padding(12.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.logiq_e9_removebg),
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

@Preview
@Composable
fun PreviewListItem() {
    ListItem(
        equipment = Equipment(
            id = "",
            name = "LOGIQ E9",
            image = "",
            description = "",
            serialNumber = 56425,
            releaseYear = 2023,
            category = "Ultrassom",
            instruction = Instruction(
                id = "",
                instructions = (1..10).map {
                    "Lorem Ipsum is simply " +
                        "dummy text of the printing and typesetting industry"
                },
                timeForReading = 10
            )
        )
    )
}