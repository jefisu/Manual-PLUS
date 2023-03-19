package com.jefisu.manualplus.features_manual.presentation.detail

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import coil.compose.AsyncImage
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.ui.theme.spacing
import com.jefisu.manualplus.features_manual.domain.Equipment
import com.jefisu.manualplus.features_manual.presentation.home.components.EquipmentInfo
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMotionApi::class)
@Destination
@Composable
fun DetailScreen(
    equipment: Equipment,
    imageURL: String,
    navigator: DestinationsNavigator
) {
    var animateToEnd by rememberSaveable { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (animateToEnd) 1f else 0f,
        animationSpec = tween(1500),
        label = ""
    )
    val context = LocalContext.current
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.detail_screen_motion_scene)
            .readBytes()
            .decodeToString()
    }

    val infos = listOf(
        EquipmentInfo(equipment.serialNumber.toString(), R.drawable.ic_hash),
        EquipmentInfo(equipment.releaseYear.toString(), R.drawable.ic_calendar),
        EquipmentInfo(equipment.category, R.drawable.ic_category)
    )
    val bottomInfo =
        listOf("Read 10 min", "Text", "Text")

    MotionLayout(
        motionScene = MotionScene(content = motionScene),
        progress = progress,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.onBackground)
    ) {
        val containerProp by motionProperties("container")
        val navigationIconProp by motionProperties("navigationIcon")
        val nameProp by motionProperties("name")

        Box(
            modifier = Modifier
                .layoutId(containerProp.id())
                .clip(CutCornerShape(topStart = containerProp.distance("corner")))
                .background(MaterialTheme.colors.background)
        )
        IconButton(
            onClick = navigator::navigateUp,
            modifier = Modifier.layoutId(navigationIconProp.id())
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = null,
                tint = if (isSystemInDarkTheme()) navigationIconProp.color("darkColor") else navigationIconProp.color(
                    "lightColor"
                ),
                modifier = Modifier.size(31.dp)
            )
        }
        Box(
            modifier = Modifier
                .layoutId("image")
                .width(155.dp)
                .height(194.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colors.primary)
                .padding(12.dp)
        ) {
            AsyncImage(
                model = imageURL,
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(
            text = equipment.name.uppercase(),
            style = MaterialTheme.typography.h5,
            color = if (isSystemInDarkTheme()) nameProp.color("darkTextColor") else nameProp.color("lightTextColor"),
            modifier = Modifier.layoutId(nameProp.id())
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            modifier = Modifier.layoutId("infos")
        ) {
            infos.forEachIndexed { i, info ->
                val color = if (i == 0) {
                    MaterialTheme.colors.background
                } else MaterialTheme.colors.onBackground

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(info.icon),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = color
                    )
                    Spacer(modifier = Modifier.width(24.dp))
                    Text(
                        text = info.name, style = MaterialTheme.typography.body2, color = color
                    )
                }
            }
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            modifier = Modifier.layoutId("description")
        ) {
            Text(
                text = "Description".uppercase(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = equipment.description,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground.copy(0.7f),
                maxLines = 5,
                textAlign = TextAlign.Justify,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
            modifier = Modifier.layoutId("stepByStep")
        ) {
            Text(
                text = "Step by Step".uppercase(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
                userScrollEnabled = progress == 1f
            ) {
                itemsIndexed(equipment.stepByStep) { i, step ->
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "${i + 1}.",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground,
                        )
                        Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
                        Text(
                            text = step,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground.copy(0.7f),
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .layoutId("seeMore")
                .background(MaterialTheme.colors.background)
        ) {
            Text(
                text = "See more",
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(8.dp)
                    .clickable { animateToEnd = true }
                    .padding(4.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .layoutId("bottomAction")
                .height(52.dp)
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(MaterialTheme.colors.onBackground)
                .padding(horizontal = 12.dp)
        ) {
            Button(
                onClick = { animateToEnd = false },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.background,
                    contentColor = MaterialTheme.colors.onBackground
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(),
                modifier = Modifier
                    .weight(1f)
                    .height(34.dp)
            ) {
                Text(
                    text = "See less",
                    style = MaterialTheme.typography.body2,
                    fontWeight = FontWeight.Bold,
                )
            }
            bottomInfo.forEach { info ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(34.dp)
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colors.background,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Text(
                        text = info,
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.background,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}