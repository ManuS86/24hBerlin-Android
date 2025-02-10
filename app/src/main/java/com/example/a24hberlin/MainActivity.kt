package com.example.a24hberlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.a24hberlin.ui.screens.appnavigation.AppNavigation
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

@Preview(device = Devices.PIXEL_7, showBackground = true, showSystemUi = true)
@Composable
fun AppNavigationPreview() {
    _24hBerlinTheme {
        AuthWrapper()
    }
}