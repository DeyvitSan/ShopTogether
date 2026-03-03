package com.deyvieat.shoptogether

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import com.deyvieat.shoptogether.core.navigation.AppNavigation
import com.deyvieat.shoptogether.core.ui.theme.ShopTogetherTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ShopTogetherTheme {
                AppNavigation()
            }
        }
    }
}