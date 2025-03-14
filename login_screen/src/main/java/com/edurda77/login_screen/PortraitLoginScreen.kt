package com.edurda77.login_screen

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiTextField

@Composable
fun PortraitLoginScreen(
    modifier: Modifier = Modifier,
    snakeBarHostState: SnackbarHostState,
    isLoading: Boolean,
    email: String,
    password: String,
    isShowPassword: Boolean,
    onSetEmail: (String) -> Unit,
    onSetPassword: (String) -> Unit,
    onLogin: () -> Unit,
    onClickShowPassword: () -> Unit,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
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
                        .clip(shape = MaterialTheme.shapes.medium)
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.meteo_bk),
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
                        onSetEmail(it)
                    }
                )
                Spacer(modifier = modifier.height(10.dp))
                UiTextField(
                    content = password,
                    label = stringResource(id = R.string.password_required),
                    keyboardType = KeyboardType.Text,
                    visualTransformation = if (isShowPassword) VisualTransformation.None else PasswordVisualTransformation(),
                    imeAction = ImeAction.Done,
                    keyboardActions = KeyboardActions(
                        onDone = {
                            onLogin()
                        }
                    ),
                    onClickContent = {
                        onSetPassword(it)
                    },
                    trailingIcon = if (isShowPassword) ImageVector.vectorResource(R.drawable.baseline_visibility_off_24) else ImageVector.vectorResource(
                        R.drawable.baseline_visibility_24
                    ),
                    onClickTrailingIcon = onClickShowPassword
                )
                Spacer(modifier = modifier.height(15.dp))
                Button(
                    modifier = modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                    shape = MaterialTheme.shapes.medium,
                    onClick = {
                        onLogin()
                    }) {
                    Text(
                        color = MaterialTheme.colorScheme.background,
                        text = stringResource(id = R.string.enter),
                        style = Typography.bodyLarge,
                    )
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UiAlertDialogView1() {
    ImpulsMeteoTheme {
        PortraitLoginScreen(
            snakeBarHostState = remember { SnackbarHostState() },
            isLoading = false,
            email = "ert@ya.ru",
            password = "1234",
            isShowPassword = true,
            onClickShowPassword = {},
            onLogin = {},
            onSetEmail = {},
            onSetPassword = {}
        )
    }
}

@Preview
@Composable
private fun UiAlertDialogdView2() {
    ImpulsMeteoTheme {
        PortraitLoginScreen(
            snakeBarHostState = remember { SnackbarHostState() },
            isLoading = false,
            email = "ert@ya.ru",
            password = "1234",
            isShowPassword = false,
            onClickShowPassword = {},
            onLogin = {},
            onSetEmail = {},
            onSetPassword = {}
        )
    }
}