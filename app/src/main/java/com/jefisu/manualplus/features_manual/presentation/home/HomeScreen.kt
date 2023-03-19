package com.jefisu.manualplus.features_manual.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jefisu.manualplus.core.ui.theme.spacing
import com.jefisu.manualplus.features_manual.presentation.destinations.DetailScreenDestination
import com.jefisu.manualplus.features_manual.presentation.home.components.ListItem
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import java.time.LocalTime

@OptIn(ExperimentalPagerApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val context = LocalContext.current
    var widthCategorySelected by remember { mutableStateOf(0.dp) }
    val collapseToolbarState = rememberCollapsingToolbarScaffoldState()

    CollapsingToolbarScaffold(
        modifier = Modifier.fillMaxSize(),
        state = collapseToolbarState,
        scrollStrategy = ScrollStrategy.EnterAlwaysCollapsed,
        toolbar = {
            Column {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 12.dp, end = 12.dp, top = MaterialTheme.spacing.medium)
                ) {
                    Text(
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(fontWeight = FontWeight.Bold)
                            ) {
                                append("Hi, ")
                            }
                            append(
                                when (LocalTime.now().hour) {
                                    in 6..11 -> "good morning"
                                    in 12..17 -> "good afternoon"
                                    else -> "good evening"
                                } + "!"
                            )
                        },
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onBackground
                    )
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colors.primary)
                    ) {
                        AsyncImage(
                            model = ImageRequest.Builder(context)
                                .data(state.avatarUser)
                                .crossfade(true)
                                .build(),
                            contentDescription = null,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .offset(y = 6.dp)
                                .scale(scaleX = -1f, scaleY = 1f)
                        )
                    }
                }

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
                                        widthCategorySelected = with(density) { it.width.toDp() }
                                    }
                                    .clickable {
                                        scope.launch { pagerState.animateScrollToPage(index) }
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
                        onClickNavigate = { imageURL ->
                            navigator.navigate(DetailScreenDestination(equipment, imageURL))
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