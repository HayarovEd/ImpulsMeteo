package com.edurda77.login_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiTextField

@Composable
fun PortraitLoginScreen(
    modifier: Modifier = Modifier,
    snakeBarHostState: SnackbarHostState,
    isLoading: Boolean,
    email: String,
    password: String,
    onEvent: (LoginEvent) -> Unit,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            modifier = modifier.fillMaxHeight(),
            painter = if (isSystemInDarkTheme()) painterResource(R.drawable.night_cloud) else painterResource(
                R.drawable.cloud
            ),
            contentDescription = "",
            contentScale = ContentScale.FillHeight
        )
        Scaffold(
            modifier = modifier
                .fillMaxSize(),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snakeBarHostState) },
        ) { paddings ->
            if (isLoading) {
                Box(
                    modifier = modifier
                        .padding(paddings)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = modifier.size(100.dp)
                    )
                }
            } else {
                Column(
                    modifier = modifier
                        .padding(paddings)
                        .fillMaxSize()
                        .padding(horizontal = 15.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth(0.4f),
                        painter = painterResource(id = R.drawable.favicon_3),
                        contentDescription = "",
                        contentScale = ContentScale.FillWidth
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    UiTextField(
                        content = email,
                        label = stringResource(id = R.string.email_required),
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                        onClickContent = {
                            onEvent(LoginEvent.SetEmail(it))
                        }
                    )
                    Spacer(modifier = modifier.height(10.dp))
                    UiTextField(
                        content = password,
                        label = stringResource(id = R.string.password_required),
                        keyboardType = KeyboardType.Password,
                        visualTransformation = PasswordVisualTransformation(),
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(
                            onDone = {
                                onEvent(LoginEvent.OnLogin)
                            }
                        ),
                        onClickContent = {
                            onEvent(LoginEvent.SetPassword(it))
                        }
                    )
                    Spacer(modifier = modifier.height(15.dp))
                    Button(
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        onClick = {
                            onEvent(LoginEvent.OnLogin)
                        }) {
                        Text(
                            text = stringResource(id = R.string.enter),
                            style = Typography.bodyLarge,
                        )
                    }
                }
            }
        }
    }
}