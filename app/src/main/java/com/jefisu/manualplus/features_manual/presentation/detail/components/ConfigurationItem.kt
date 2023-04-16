package com.jefisu.manualplus.features_manual.presentation.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.presentation.ui.theme.spacing
import com.jefisu.manualplus.core.util.fetchImageFromFirebase
import com.jefisu.manualplus.features_manual.domain.Configuration

@Composable
fun ConfigurationItem(
    index: Int,
    configurationStep: Configuration,
    modifier: Modifier = Modifier,
    enabledClick: Boolean = true
) {
    var showDetail by rememberSaveable { mutableStateOf(false) }
    val rotateAnim by animateFloatAsState(
        targetValue = if (showDetail) 180f else 0f,
        label = "",
        animationSpec = tween(500)
    )
    val sizeAnim by animateDpAsState(
        targetValue = if (enabledClick) 20.dp else 0.dp,
        label = ""
    )
    var imageUrl by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(key1 = Unit, enabledClick) {
        if (imageUrl.isEmpty()) {
            fetchImageFromFirebase(
                remotePath = configurationStep.imagePath,
                response = { imageUrl = it.toString() }
            )
        }
        if (!enabledClick && showDetail) {
            showDetail = false
        }
    }

    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = enabledClick,
                    onClick = { showDetail = !showDetail }
                )
                .padding(vertical = MaterialTheme.spacing.small)
        ) {
            Text(
                text = "$index.",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground.copy(0.7f),
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Text(
                text = configurationStep.title,
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground.copy(0.7f),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Justify,
                maxLines = 2,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(if (enabledClick) MaterialTheme.spacing.medium else 0.dp))
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.arrow_down),
                contentDescription = null,
                tint = MaterialTheme.colors.onBackground.copy(0.7f),
                modifier = Modifier
                    .size(sizeAnim)
                    .rotate(rotateAnim)
            )
        }
        AnimatedVisibility(
            visible = showDetail,
            modifier = Modifier.padding(start = MaterialTheme.spacing.medium)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                configurationStep.stepByStep.forEachIndexed { i, _step ->
                    if (imageUrl.isNotBlank() && i == 1) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .padding(start = 24.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.LightGray)
                        ) {
                            val painter = rememberAsyncImagePainter(model = imageUrl)

                            if (painter.state is AsyncImagePainter.State.Loading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            Image(
                                painter = painter,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.matchParentSize()
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                top = if (i == 0) 0.dp else MaterialTheme.spacing.small,
                                bottom = MaterialTheme.spacing.small
                            )
                    ) {
                        Text(
                            text = "$index.${i + 1}.",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground
                        )
                        Text(
                            text = _step,
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground,
                            textAlign = TextAlign.Justify
                        )
                    }
                }
            }
        }
    }
}