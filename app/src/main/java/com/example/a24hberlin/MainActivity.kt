package com.example.a24hberlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.a24hberlin.ui.screens.auth.AuthWrapper
import com.example.a24hberlin.ui.theme._24hBerlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _24hBerlinTheme {
                AuthWrapper()
            }
        }
    }
}