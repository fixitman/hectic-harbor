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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.logintest.ui.AddEditReminderScreen
import com.example.logintest.ui.ErrorManager
import com.example.logintest.ui.MainScreen
import com.example.logintest.ui.OtherScreen
import com.example.logintest.ui.theme.LoginTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            LoginTestTheme {
                val snackbarHostState = remember {SnackbarHostState()}
                val navController = rememberNavController()
                val scope = rememberCoroutineScope()
                val viewModel: MainViewModel = viewModel()
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
                ) { paddingValues ->
                    NavHost(navController, startDestination = "Main",){
                        composable(route = "Main"){
                            MainScreen(
                                viewModel = viewModel,
                                modifier = Modifier.padding(paddingValues),
                                onNavigateToOther = {id -> navController.navigate("AddEdit/$id") }
                            )
                        }
                        composable(
                            route="AddEdit/{id}",
                            arguments = listOf(navArgument("id") {type = NavType.IntType} )
                        ){ backStack ->
                            val id = backStack.arguments?.getInt("id")?: -1
                            AddEditReminderScreen(id = id, viewModel = viewModel)
                        }
                        composable(
                            route = "Other/{id}",
                            arguments = listOf(navArgument("id") { type = NavType.IntType })
                        ){ backStackEntry ->
                            val id = backStackEntry.arguments?.getInt("id") ?: -1
                            OtherScreen(
                                modifier = Modifier.padding(paddingValues),
                                id = id,
                                onNavigateToMain = {
                                    navController.popBackStack("Main",false)
                                },
                                errorMgr = ErrorManager(snackbarHostState, scope)
                            )
                        }
                    }
                }
            }
        }
    }
}



