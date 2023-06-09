package com.jefisu.manualplus.features_manual.presentation.detail.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.presentation.ui.theme.light_Primary
import com.jefisu.manualplus.core.presentation.ui.theme.spacing
import com.jefisu.manualplus.core.util.fetchImageFromFirebase
import com.jefisu.manualplus.features_manual.domain.model.Configuration

@OptIn(ExperimentalAnimationApi::class)
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
    val textColor by animateColorAsState(
        targetValue = if (showDetail) MaterialTheme.colors.onBackground else MaterialTheme.colors.onBackground.copy(
            0.7f
        ),
        label = ""
    )
    val context = LocalContext.current

    LaunchedEffect(key1 = enabledClick) {
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
                color = textColor
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.small))
            Text(
                text = configurationStep.title,
                style = MaterialTheme.typography.body2,
                color = textColor,
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
            modifier = Modifier.padding(start = MaterialTheme.spacing.large)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                configurationStep.configItems.forEachIndexed { i, configItem ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "$index.${i + 1}.",
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground
                        )
                        Text(
                            text = formatTextWithAnnotations(configItem.title),
                            style = MaterialTheme.typography.body2,
                            color = MaterialTheme.colors.onBackground,
                            textAlign = TextAlign.Justify
                        )
                    }

                    val imagesUrl = remember { mutableStateListOf<String>() }
                    LaunchedEffect(key1 = Unit) {
                        if (imagesUrl.size < configItem.imagesRemotePath.size) {
                            configItem.imagesRemotePath.forEach { imagePath ->
                                fetchImageFromFirebase(
                                    remotePath = imagePath,
                                    onSuccess = { uri -> imagesUrl.add(uri.toString()) }
                                )
                            }
                        }
                    }
                    repeat(configItem.imagesRemotePath.size) { i ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    (if (isSystemInDarkTheme()) Color.DarkGray else Color.LightGray).copy(0.25f)
                                )
                                .animateContentSize()
                        ) {
                            var isLoading by remember { mutableStateOf(false) }
                            val lottieComposition by rememberLottieComposition(
                                LottieCompositionSpec.RawRes(R.raw.load_fae_2_lottie)
                            )
                            val progress by animateLottieCompositionAsState(
                                composition = lottieComposition,
                                reverseOnRepeat = true,
                                iterations = Int.MAX_VALUE,
                                isPlaying = isLoading
                            )

                            LottieAnimation(
                                composition = lottieComposition,
                                progress = progress,
                                modifier = Modifier
                                    .size(if (isLoading) 200.dp else 0.dp)
                                    .align(Alignment.Center)
                            )

                            if (imagesUrl.size == configItem.imagesRemotePath.size) {
                                AsyncImage(
                                    model = imagesUrl[i],
                                    contentDescription = null,
                                    onState = {
                                        isLoading = it is AsyncImagePainter.State.Loading
                                    },
                                    contentScale = ContentScale.FillWidth,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    if (configItem.attentionText.isNotEmpty()) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "$index.${i + 1}.",
                                style = MaterialTheme.typography.body2,
                                color = Color.Transparent
                            )
                            Text(
                                text = formatTextWithAnnotations(configItem.attentionText),
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
}

@Composable
fun formatTextWithAnnotations(input: String) = buildAnnotatedString {
    val boldPattern = ";([^;]+);".toRegex()
    val italicPattern = "_([^_]+)_".toRegex()
    var startIndex = 0
    boldPattern
        .findAll(input)
        .forEach { matchResult ->
            val matchValue = matchResult.value
            val matchIndex = matchResult.range.first
            if (matchIndex > startIndex) {
                append(input.substring(startIndex, matchIndex))
            }
            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                append(matchValue.substring(1, matchValue.lastIndex))
            }
            startIndex = matchResult.range.last + 1
        }
    italicPattern
        .findAll(input)
        .forEach { matchResult ->
            val matchIndex = matchResult.range.first
            val matchValue = matchResult.value
            if (matchIndex > startIndex) {
                append(input.substring(startIndex, matchIndex))
            }
            startIndex = matchResult.range.last + 1
            val matchValueWithoutDelimiter = matchValue.removeSurrounding("_")
            withStyle(
                style = SpanStyle(
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colors.secondary
                )
            ) {
                append(matchValueWithoutDelimiter)
            }
        }
    if (startIndex < input.length) {
        append(input.substring(startIndex))
    }
}