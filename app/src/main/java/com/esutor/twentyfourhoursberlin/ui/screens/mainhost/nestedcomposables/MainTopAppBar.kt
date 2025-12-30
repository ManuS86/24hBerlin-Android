package com.esutor.twentyfourhoursberlin.ui.screens.mainhost.nestedcomposables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.esutor.twentyfourhoursberlin.R
import com.esutor.twentyfourhoursberlin.navigation.Screen
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.PopSpeed
import com.esutor.twentyfourhoursberlin.ui.screens.components.animations.expressivePop
import com.esutor.twentyfourhoursberlin.ui.screens.components.utilitybars.SearchBar
import com.esutor.twentyfourhoursberlin.ui.theme.smallPadding
import com.esutor.twentyfourhoursberlin.ui.theme.slightRounding
import com.esutor.twentyfourhoursberlin.ui.viewmodel.EventViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    titleResId: Int,
    isMainTab: Boolean,
    scrollBehavior: TopAppBarScrollBehavior,
    currentRoute: String?,
    showSearchBar: Boolean,
    onSearchIconClick: () -> Unit,
    onSearchClosed: () -> Unit,
    navController: NavHostController,
    eventVM: EventViewModel
) {
    val searchTextValue by eventVM.searchTextFieldValue.collectAsStateWithLifecycle()

    val backButtonRoutes = remember { setOf(Screen.ReAuthWrapper.route) }
    val hideSearchRoutes = remember { setOf(Screen.Settings.route, Screen.ReAuthWrapper.route) }
    val showBackButton = currentRoute in backButtonRoutes
    val showSearchComponent = currentRoute !in hideSearchRoutes && !showBackButton

    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = CenterStart
            ) {
                AnimatedVisibility(
                    visible = showSearchBar && showSearchComponent,
                    enter = fadeIn(animationSpec = tween(200)) + scaleIn(
                        initialScale = 0.8f,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    ),
                    exit = fadeOut(animationSpec = tween(150)) + slideOutHorizontally(
                        targetOffsetX = { -it },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioNoBouncy,
                            stiffness = Spring.StiffnessMedium
                        )
                    )
                ) {
                    SearchBar(
                        searchText = searchTextValue,
                        onSearchTextChanged = { eventVM.updateSearchText(it) },
                        onSearchClosed = onSearchClosed
                    )
                }

                // Title Row
                AnimatedVisibility(
                    visible = !showSearchBar || !showSearchComponent,
                    enter = fadeIn(tween(200)),
                    exit = fadeOut(tween(100))
                ) {
                    AppLogo(titleResId, isMainTab)
                }
            }
        },
        navigationIcon = {
            if (showBackButton) {
                val backInteractionSource = remember { MutableInteractionSource() }

                IconButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.expressivePop(backInteractionSource, PopSpeed.Fast),
                    interactionSource = backInteractionSource
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(R.string.back))
                }
            }
        },
        actions = {
            if (showSearchComponent && !showSearchBar) {
                val searchInteractionSource = remember { MutableInteractionSource() }

                IconButton(
                    onClick = onSearchIconClick,
                    modifier = Modifier.expressivePop(searchInteractionSource, PopSpeed.Fast),
                    interactionSource = searchInteractionSource
                ) {
                    Icon(Icons.Filled.Search, stringResource(R.string.search))
                }
            }
        },
        colors = topAppBarColors(
            containerColor = Black,
            scrolledContainerColor = Black,
            navigationIconContentColor = White,
            titleContentColor = White,
            actionIconContentColor = White
        ),
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun AppLogo(titleResId: Int, isMainTab: Boolean) {
    val titleContent = if (isMainTab) {
        buildAnnotatedString {
            withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold, letterSpacing = (-1).sp)) {
                append("24")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)) {
                append("h")
            }
            withStyle(SpanStyle(fontWeight = FontWeight.Light, letterSpacing = 1.sp)) {
                append("BERLIN")
            }
        }
    } else {
        buildAnnotatedString { append(stringResource(titleResId)) }
    }

    Row(
        verticalAlignment = CenterVertically,
        horizontalArrangement = spacedBy(smallPadding)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(color = White, shape = slightRounding),
            contentAlignment = Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.app_logo),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                tint = null
            )
        }

        Text(text = titleContent, color = White)
    }
}