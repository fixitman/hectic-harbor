package com.example.logintest.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintest.ui.theme.LoginTestTheme


@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(MainViewModel::class.java)
){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
    ){
        viewModel.reminders.forEach {
            if(it.reminderText.length > 0){
                Text(
                    text= it.reminderText,
                    style = typography.bodyLarge
                    )
            }
        }


        Button(onClick = { viewModel.getReminders() }) {
            Text(text = "Click Me", style = typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LoginTestTheme {
        MainScreen()
    }
}