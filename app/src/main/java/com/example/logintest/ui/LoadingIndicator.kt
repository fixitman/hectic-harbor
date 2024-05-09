package com.example.logintest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingIndicator(
    color: Color = MaterialTheme.colorScheme.onSurface,
    background: Color = MaterialTheme.colorScheme.surface
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
            style = MaterialTheme.typography.headlineMedium,
            color = color,
            modifier = Modifier
        )
    }
}