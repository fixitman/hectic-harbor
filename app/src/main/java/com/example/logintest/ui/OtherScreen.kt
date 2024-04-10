package com.example.logintest.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun OtherScreen(
        onNavigateToMain: () -> Unit
){
    Column(

        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(.5f)
                .padding(8.dp)
                .background(colorScheme.primary)
                .clickable {
                    onNavigateToMain()
                }

        ) {

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Red)
                .padding(16.dp)
                .background(colorScheme.secondary)
                .border(BorderStroke(8.dp,Color.Blue))


        ) {

        }
    }
}

@Preview(showBackground = true)
@Composable
fun Look(){
    OtherScreen({})
}