package com.jefisu.manualplus.features_manual.presentation.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.presentation.components.AvatarImage
import com.jefisu.manualplus.core.presentation.ui.theme.spacing
import com.jefisu.manualplus.features_manual.domain.model.User
import com.jefisu.manualplus.features_manual.presentation.home.components.ListItem
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import java.time.LocalTime

@OptIn(ExperimentalPagerApi::class)
@Destination
@Composable
fun HomeScreen(
    pairUser: Pair<User?, String>,
    state: HomeState,
    onDataLoaded: () -> Unit,
    navigateToDetail: (String, String) -> Unit,
    navigateToProfile: () -> Unit
) {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    var widthCategorySelected by remember { mutableStateOf(0.dp) }
    val collapseToolbarState = rememberCollapsingToolbarScaffoldState()

    var visibleContent by rememberSaveable { mutableStateOf(false) }
    val contentAlpha by animateFloatAsState(
        targetValue = if (visibleContent) 1f else 0f,
        label = "",
        animationSpec = tween(500)
    )
    val context = LocalContext.current
    val avatarPainter = rememberAsyncImagePainter(
        model = ImageRequest.Builder(context)
            .data(pairUser.second)
            .crossfade(true)
            .decoderDispatcher(Dispatchers.IO)
            .build(),
        onState = {
            onDataLoaded()
            if (it is AsyncImagePainter.State.Success) {
                visibleContent = true
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (state.error != null) {
            Text(text = state.error.asString())
            return@Box
        }
        if (state.isLoading || !visibleContent) {
            val lottieComposition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(R.raw.loading_files_9929)
            )
            val progress by animateLottieCompositionAsState(
                composition = lottieComposition,
                iterations = Int.MAX_VALUE,
                isPlaying = true
            )

            LottieAnimation(
                composition = lottieComposition,
                progress = progress,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        CollapsingToolbarScaffold(
            modifier = Modifier
                .fillMaxSize()
                .alpha(contentAlpha),
            state = collapseToolbarState,
            scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
            toolbar = {
                Column {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 12.dp,
                                end = 12.dp,
                                top = MaterialTheme.spacing.medium
                            )
                    ) {
                        Text(
                            text = buildAnnotatedString {
                                withStyle(
                                    style = SpanStyle(fontWeight = FontWeight.Bold)
                                ) {
                                    append(stringResource(R.string.hi, " "))
                                }
                                append(
                                    when (LocalTime.now().hour) {
                                        in 6..11 -> stringResource(R.string.good_morning)
                                        in 12..17 -> stringResource(R.string.good_afternoon)
                                        else -> stringResource(R.string.good_evening)
                                    } + "!"
                                )
                            },
                            style = MaterialTheme.typography.h4,
                            color = MaterialTheme.colors.onBackground
                        )
                        AvatarImage(
                            painter = avatarPainter,
                            isMirrored = true,
                            onClick = navigateToProfile
                        )
                    }

                    if (state.equipments.isNotEmpty()) {
                        LazyRow {
                            item {
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                            }
                            itemsIndexed(state.categories) { index, category ->
                                val selected = pagerState.currentPage == index
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = category,
                                        style = MaterialTheme.typography.body1,
                                        color = MaterialTheme.colors.onBackground,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier
                                            .onSizeChanged {
                                                widthCategorySelected =
                                                    with(density) { it.width.toDp() }
                                            }
                                            .clickable {
                                                scope.launch {
                                                    pagerState.animateScrollToPage(
                                                        index
                                                    )
                                                }
                                            }
                                            .padding(MaterialTheme.spacing.small)
                                    )
                                    if (selected) {
                                        Box(
                                            modifier = Modifier
                                                .width(widthCategorySelected - MaterialTheme.spacing.small)
                                                .height(2.dp)
                                                .padding(horizontal = MaterialTheme.spacing.extraSmall)
                                                .clip(CircleShape)
                                                .background(MaterialTheme.colors.onBackground)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(MaterialTheme.spacing.extraSmall))
                            }
                        }
                    }
                }
            }
        ) {

            HorizontalPager(
                count = state.categories.size,
                state = pagerState
            ) { currentPager ->
                val categorySelected =
                    if (state.categories.isNotEmpty()) state.categories[currentPager] else ""

                LazyColumn {
                    item {
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    }
                    items(
                        state.equipments.filter { it.category == categorySelected }
                    ) { equipment ->
                        ListItem(
                            equipment = equipment,
                            onClickNavigate = { imageUrl ->
                                navigateToDetail(equipment.id, imageUrl)
                            },
                            modifier = Modifier.padding(
                                start = 12.dp,
                                end = 12.dp,
                                bottom = 16.dp
                            )
                        )
                    }
                }
            }
        }
    }
}