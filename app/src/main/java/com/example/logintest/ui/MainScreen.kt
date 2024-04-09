package com.example.logintest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintest.dataaccess.Reminder
import com.example.logintest.ui.theme.LoginTestTheme


@Composable
fun MainScreen(
    modifier: Modifier = Modifier
){
    val viewModel = viewModel<MainViewModel>()
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize()
    ){
        Button(onClick = { viewModel.getReminders() }) {
            Text(text = "Click Me", style = typography.bodyMedium)
        }
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ){
            items(viewModel.reminders) {
                if(it.reminderText.isNotEmpty()){
                    ReminderItem(it, viewModel::onReminderClick)
                }
            }
        }
    }
    if(viewModel.showLoginDialog){
        LoginDialog(
            onDismiss = {  },
            onSubmit = viewModel::submitLogin,
            onExit = viewModel::onExit,
            username = viewModel.credentials.value.UserName,
            updateUsername =  viewModel::updateUser ,
            password = viewModel.credentials.value.password,
            updatePassword = viewModel::updatePassword,
            saveCreds = viewModel.credentials.value.remember,
            updateSaveCreds = viewModel::updateRemember,
        )
    }

}

@Composable
private fun ReminderItem(
    reminder: Reminder,
    onClick: (Reminder) -> Unit = {}
) {
    Card (
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        modifier = Modifier
            .background(Color.LightGray)
            .fillMaxWidth()
            .padding(24.dp)
            .clickable { onClick(reminder) }

    ){
        Text(
            text = reminder.reminderText,
            style = typography.headlineMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoginTestTheme {
        ReminderItem(
            reminder = Reminder(reminderText = "Some Text")
        )
    }
}