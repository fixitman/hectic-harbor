package com.example.logintest.ui

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.PlatformImeOptions
import androidx.compose.ui.text.input.VisualTransformation
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
    onSubmit: () -> Unit = {},
    onExit: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
    ){
        var passwordVisible by remember {
            mutableStateOf(false)
        }
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
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Password),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = password,
                    onValueChange = updatePassword,
                    label = { Text("Password") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Password,
                        autoCorrect = false),
                    keyboardActions = KeyboardActions(onDone = {onSubmit()}),
                    visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        val image = if(!passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, "Show/Hide")
                        }
                    }
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
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ){
                    Button(
                        onClick = onExit,
                        modifier = Modifier
                            .padding(end = 16.dp)
                    ) {
                        Text("Exit")
                    }
                    Button(
                        onClick = onSubmit
                    ){
                        Text("Submit")
                    }
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