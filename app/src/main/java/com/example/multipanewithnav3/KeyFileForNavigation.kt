package com.example.multipanewithnav3

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed interface AppDestination : NavKey {
    @Serializable
    data object ProductList : AppDestination

    @Serializable
    data class ProductDetail(val id: String) : AppDestination

    @Serializable
    data object Profile : AppDestination
}