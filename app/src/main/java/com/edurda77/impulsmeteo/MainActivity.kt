package com.edurda77.impulsmeteo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.edurda77.impulsmeteo.navigation.NavController
import com.edurda77.impulsmeteo.ui.theme.ImpulsMeteoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ImpulsMeteoTheme {
               NavController()
            }
        }
    }
}