package com.example.logintest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintest.MainViewModel
import com.example.logintest.dataaccess.Reminder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun MainScreen(
    onNavigateToOther: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
){
    val credentials = viewModel.credentials.value
    if(viewModel.showAuthScreen){
        AuthScreen(viewModel = viewModel)
    }else{
        MainContent(modifier,viewModel,onNavigateToOther)
    }

    if (viewModel.showLoginDialog) {
        LoginDialog(
            onDismiss = { },
            onSubmit = viewModel::onSubmit,
            onExit = viewModel::onExit,
            username = credentials.UserName,
            updateUsername = viewModel::updateUser,
            password = credentials.password,
            updatePassword = viewModel::updatePassword,
            saveCreds = credentials.remember,
            updateSaveCreds = viewModel::updateRemember,
        )
    }
}

/*********************************************************************************************************/

@Composable
fun AuthScreen(viewModel: MainViewModel = viewModel()){
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
             viewModel.getToken()
        }
    }
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Authenticating...",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

/************************************************************************************************************/

@Composable
fun MainContent(modifier: Modifier = Modifier, viewModel: MainViewModel = viewModel(), onNavigateToOther: (Int) -> Unit){
    LaunchedEffect(Unit) {
        viewModel.getReminders()
    }
    Box(
        modifier = modifier.fillMaxSize()
    ){
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(colorScheme.primary)
        ) {
            items(viewModel.reminders) {
                if (it.reminderText.isNotEmpty()) {
                    ReminderItem(it, onNavigateToOther, viewModel)
                }
            }
        }
        if(viewModel.isLoading && !viewModel.showLoginDialog){
            LoadingIndicator(color = colorScheme.onPrimary, background = Color.Transparent)
        }
    }
}

/**********************************************************************************************/

@Composable
private fun LoadingIndicator(
    color: Color = colorScheme.onSurface,
    background: Color = colorScheme.surface
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        CircularProgressIndicator(
            strokeWidth = 8.dp,
            color = color,
            modifier = Modifier
                .width(100.dp)
                .align(Alignment.CenterHorizontally)
                .background(Color.Transparent)
        )
        Spacer(modifier = Modifier.height(100.dp))
        Text(
            "Loading...",
            style = typography.headlineMedium,
            color = color,
            modifier = Modifier
        )
    }
}

/***********************************************************************************************/

@Composable
private fun ReminderItem(reminder: Reminder, onNavigateToOther: (Int) -> Unit, viewModel: MainViewModel) {
    Card (
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxWidth()
            .padding(24.dp)
            .clickable {
                //onNavigateToOther(reminder.id)
                viewModel.showSnackbar(reminder.toString())
            }
    ){
        Text(
            text = reminder.reminderText,
            style = typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .background(colorScheme.secondaryContainer)
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

/*********************************************************************************************/

@Preview(showBackground = false)
@Composable
fun SpinnyPreview() {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.secondary)
    ) {
        Column(
            verticalArrangement=Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            CircularProgressIndicator(
                strokeWidth = 8.dp,
                modifier = Modifier
                    .width(200.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(100.dp))
            Text(
                "Loading...",
                style= typography.headlineMedium,
                modifier = Modifier

            )
        }
    }
}