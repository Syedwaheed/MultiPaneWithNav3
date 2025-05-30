package com.example.multipanewithnav3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.navigation3.ListDetailSceneStrategy
import androidx.compose.material3.adaptive.navigation3.rememberListDetailSceneStrategy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.example.multipanewithnav3.screen.PlaceholderContent
import com.example.multipanewithnav3.screen.ProductDetailScreen
import com.example.multipanewithnav3.screen.ProductListScreen
import com.example.multipanewithnav3.screen.ProfileScreen
import com.example.multipanewithnav3.ui.theme.MultiPaneWithNav3Theme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MultiPaneWithNav3Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val backStack = rememberNavBackStack(AppDestination.ProductList)
                    val listDetailStrategy = rememberListDetailSceneStrategy<NavKey>()

                    NavDisplay(
                        backStack = backStack,
                        modifier = Modifier.padding(innerPadding),
                        onBack = { keysToRemove ->
                            repeat(keysToRemove) { backStack.removeLastOrNull() }
                        },
                        sceneStrategy = listDetailStrategy,
                        entryProvider = entryProvider {
                            entry<AppDestination.ProductList>(
                                metadata = ListDetailSceneStrategy.listPane(
                                    detailPlaceholder = {
                                        PlaceholderContent("Select a product")
                                    }
                                )
                            ) {
                                ProductListScreen(
                                    onProductClick = { productId ->
                                        backStack.add(AppDestination.ProductDetail(productId))
                                    }
                                )
                            }

                            entry<AppDestination.ProductDetail>(
                                metadata = ListDetailSceneStrategy.detailPane()
                            ) { detail ->
                                ProductDetailScreen(detail.id) {
                                    backStack.add(AppDestination.Profile)
                                }
                            }

                            entry<AppDestination.Profile>(
                                metadata =  ListDetailSceneStrategy.extraPane()
                                + NavDisplay.transitionSpec {
                                    slideInVertically(
                                        initialOffsetY = { it },
                                        animationSpec = tween(durationMillis = 1000)
                                    ) togetherWith ExitTransition.KeepUntilTransitionsFinished
                                } + NavDisplay.popTransitionSpec {
                                    EnterTransition.None.togetherWith(
                                        slideOutVertically(
                                            targetOffsetY = { it },
                                            animationSpec = tween(durationMillis = 1000)
                                        )
                                    )
                                } + NavDisplay.predictivePopTransitionSpec {
                                    EnterTransition.None.togetherWith(
                                        slideOutVertically(
                                            targetOffsetY = { it },
                                            animationSpec = tween(durationMillis = 1000)
                                        )
                                    )
                                }
                            ) {
                                ProfileScreen()
                            }
                        },
                        transitionSpec = {
                            slideInHorizontally(
                                initialOffsetX = { it }) togetherWith
                                    slideOutHorizontally(targetOffsetX = { -it })
                        },
                        popTransitionSpec = {
                            slideInHorizontally(
                                initialOffsetX = { -it }) togetherWith
                                    slideOutHorizontally(targetOffsetX = { it })
                        },
                        predictivePopTransitionSpec = {
                            slideInHorizontally(
                                initialOffsetX = { -it }) togetherWith
                                    slideOutHorizontally(targetOffsetX = { it })
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ProductList() {
    ProductListScreen {

    }

}