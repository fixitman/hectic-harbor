package com.example.logintest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.logintest.ui.AddEditReminderScreen
import com.example.logintest.ui.LoadingIndicator
import com.example.logintest.ui.MainScreen
import com.example.logintest.ui.theme.LoginTestTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginTestTheme {
                val viewModel: MainViewModel = viewModel()
                val navController = rememberNavController()
                val snackbarHostState = remember {SnackbarHostState()}
                val scope = rememberCoroutineScope()
                LaunchedEffect(Unit) {
                    viewModel.uiEvents.collect{
                        when(it){
                            is MainViewModel.UIEvent.SnackbarEvent -> {
                                scope.launch {
                                    snackbarHostState.showSnackbar(it.message, duration = SnackbarDuration.Short)
                                }
                            }
                        }
                    }
                }
                Scaffold(
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState)}
                ) { paddingValues ->
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ){
                        NavHost(navController, startDestination = "Main") {
                            composable(route = "Main") {
                                MainScreen(
                                    viewModel = viewModel,
                                    modifier = Modifier.padding(paddingValues),
                                    onNavigateToOther = { id -> navController.navigate("AddEdit/$id") }
                                )
                            }
                            composable(
                                route = "AddEdit/{id}",
                                arguments = listOf(navArgument("id") { type = NavType.IntType })
                            ) { backStack ->
                                val id = backStack.arguments?.getInt("id") ?: -1
                                AddEditReminderScreen(id = id, viewModel = viewModel)
                            }
                        }
                        if(viewModel.isLoading && !viewModel.showLoginDialog){
                            LoadingIndicator(color = MaterialTheme.colorScheme.onPrimary, background = Color(0f,0f,0f,.5f))
                        }
                    }
                }
            }
        }
    }
}



