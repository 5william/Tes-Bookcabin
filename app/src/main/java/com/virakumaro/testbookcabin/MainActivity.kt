package com.virakumaro.testbookcabin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.virakumaro.testbookcabin.presentation.onlinecheckin.OnlineCheckInScreen
import com.virakumaro.testbookcabin.ui.theme.TestBookcabinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestBookcabinTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}