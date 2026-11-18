package com.example.ejerciciosadicionales

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ejerciciosadicionales.ui.HomeScreen
import com.example.ejerciciosadicionales.ui.theme.AnimacionesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AnimacionesTheme {
                HomeScreen()
            }
        }
    }
}
