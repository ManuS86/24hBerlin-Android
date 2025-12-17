package com.example.a24hberlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.a24hberlin.ui.screens.auth.AuthWrapper
import com.example.a24hberlin.ui.screens.components.utilityelements.ScheduleReminderEffect
import com.example.a24hberlin.ui.theme.AppTheme
import com.example.a24hberlin.ui.viewmodel.EventViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val eventVM: EventViewModel by viewModels()

                ScheduleReminderEffect(eventVM)

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentWindowInsets = WindowInsets.safeDrawing
                ) { innerPadding ->
                    Image(
                        painter = painterResource(R.drawable.background),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                    AuthWrapper(innerPadding)
                }
            }
        }
    }
}