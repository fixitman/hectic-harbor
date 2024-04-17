package com.example.logintest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.logintest.ui.MainScreen
import com.example.logintest.ui.OtherScreen
import com.example.logintest.ui.theme.LoginTestTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginTestTheme {
                val snackbarHostState = remember {SnackbarHostState()}
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
                ) { paddingValues ->
                    NavHost(navController, startDestination = "Main",){
                        composable(route = "Main"){
                            MainScreen(
                                modifier = Modifier.padding(paddingValues),
                                onNavigateToOther = {id -> navController.navigate("Other/$id") },
                                onError = { msg: String ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message = msg)
                                    }
                                }
                            )
                        }
                        composable(
                            route = "Other/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ){ backStackEntry ->
                            val id: Int = backStackEntry.arguments?.getInt("id") ?: -1
                            OtherScreen(
                                modifier = Modifier.padding(paddingValues),
                                id = id,
                                onNavigateToMain = {
                                    navController.popBackStack("Main",false)
                                },
                                onError = { msg: String ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(message = msg)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}




