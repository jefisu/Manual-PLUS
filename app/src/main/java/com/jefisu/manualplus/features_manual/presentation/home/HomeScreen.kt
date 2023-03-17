package com.jefisu.manualplus.features_manual.presentation.home

import androidx.compose.foundation.Image
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.ui.theme.ManualPLUSTheme
import com.jefisu.manualplus.core.ui.theme.spacing
import com.jefisu.manualplus.features_manual.domain.Equipment
import com.jefisu.manualplus.features_manual.domain.Instruction
import com.jefisu.manualplus.features_manual.presentation.home.components.ListItem
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@OptIn(ExperimentalPagerApi::class)
@Destination
@Composable
fun HomeScreen() {
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    var widthCategorySelected by remember { mutableStateOf(0.dp) }
    val collapseToolbarState = rememberCollapsingToolbarScaffoldState()
    val categories = listOf(
        "Ultrassom",
        "Tomografia",
        "Categorie 1",
        "Categorie 2"
    )
    val categorySelected = categories[pagerState.currentPage]

    val equipments = (1..10).map {
        Equipment(
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
    }

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
                            append("good morning!")
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
                        Image(
                            painter = painterResource(R.drawable.artboards_diversity_avatars_by_netguru_16),
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
                    itemsIndexed(categories) { index, category ->
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
            count = categories.size,
            state = pagerState
        ) {
            LazyColumn {
                item {
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                }
                items(
                    equipments.filter { it.category == categorySelected }
                ) { equipment ->
                    ListItem(
                        equipment = equipment,
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

@Preview
@Composable
fun PreviewHomeScreen() {
    ManualPLUSTheme {
        Surface {
            HomeScreen()
        }
    }
}