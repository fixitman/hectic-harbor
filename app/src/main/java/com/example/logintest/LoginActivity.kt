package com.example.logintest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


class LoginActivity(

): ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LoginScreen()
        }
    }
}


@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
) {
    Column(
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(Color.Red)
    ) {
        Dialog(onDismissRequest = {  }) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    //.height(400.dp),
                    //.padding(16.dp),
                //shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .fillMaxWidth()
                ){
                    Text(
                        "Log in please",
                        style = typography.titleLarge,
                        modifier = modifier.padding(16.dp)
                    )
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = {Text("Username")}
                    )
                    OutlinedTextField(
                        value = "",
                        onValueChange = {},
                        label = {Text("Password")}
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ){
                        OutlinedButton(
                            onClick= {},
                            modifier = modifier

                        ){
                            Text("Ok")
                        }
                        OutlinedButton(
                            onClick= {},
                            modifier = modifier
                                .padding(start = 16.dp)

                        ){
                            Text("Cancel")
                        }
                    }
                }
            }
        }


    }

}

@Preview
@Composable
fun preview(){
    LoginScreen()
}
