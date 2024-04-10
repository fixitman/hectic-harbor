package com.example.logintest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavDirections
import androidx.navigation.NavType
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.logintest.ui.MainScreen
import com.example.logintest.ui.OtherScreen
import com.example.logintest.ui.theme.LoginTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginTestTheme {
                // A surface container using the 'background' color from the theme
                val navController = rememberNavController()
                NavHost(navController, startDestination = "Main"){
                    composable("Main" ){MainScreen(onNavigateToOther = {id -> navController.navigate("Other/$id") })}

                    composable(
                        route = "Other/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.IntType })
                    ){
                        val id: Int = it.arguments?.getInt("id") ?: -1
                        OtherScreen(
                            id = id,
                            onNavigateToMain = {
                                navController.popBackStack("Main",false)
                            }
                        )
                    }
                }
            }
        }
    }
}




