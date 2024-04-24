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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.logintest.MainViewModel


@Composable
fun OtherScreen(
    modifier: Modifier = Modifier,
    id: Int = -1,
    onNavigateToMain: () -> Unit = {},
    viewModel: MainViewModel = viewModel(),
    errorMgr: ErrorManager
){
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(.5f)
                .padding(8.dp)
                .background(colorScheme.primary)
                .clickable {
                    onNavigateToMain()
                }

        ) {
            Text("The Id is $id")
        }

        Box(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(Color.Red)
                .padding(16.dp)
                .background(colorScheme.secondary)
                .border(BorderStroke(16.dp, Color.Blue))
                .clickable {
                    errorMgr.showError("clicked!")
                }

        ) {

        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun Look(){
//    OtherScreen(snackbarHostState = null)
//}