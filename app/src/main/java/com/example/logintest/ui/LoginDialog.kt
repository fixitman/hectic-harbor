package com.example.logintest.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog


@Composable
fun LoginDialog(
    username: String = "",
    updateUsername: (String) -> Unit = {},
    password: String = "",
    updatePassword: (String)-> Unit = {},
    saveCreds: Boolean = false,
    updateSaveCreds: (Boolean)-> Unit = {},
    onDismiss: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
    ){
        Card(
          shape = RoundedCornerShape(16.dp),

        ){
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    "Please Log In",
                    style = typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextField(
                    value = username,
                    onValueChange = updateUsername,
                    label = { Text("Username") },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextField(
                    value = password,
                    onValueChange = updatePassword,
                    label = { Text("Password") },
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ){
                    Switch(
                        checked = saveCreds,
                        onCheckedChange = updateSaveCreds,
                        modifier = Modifier
                            .padding(end = 16.dp)
                    )
                    Text("Remember Credentials")
                }
                Button(
                    onClick=onSubmit,

                ){
                    Text("Submit")
                }
            }
        }

    }
}

@Preview
@Composable
fun preview(){
    LoginDialog(  )
}