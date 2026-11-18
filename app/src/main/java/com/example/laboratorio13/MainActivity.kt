
package com.example.laboratorio13

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.laboratorio13.ui.theme.Laboratorio13Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Laboratorio13Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AnimatedContentExercise(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AnimatedContentExercise(modifier: Modifier = Modifier) {
    var state by remember { mutableStateOf("Loading") }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AnimatedContent(
            targetState = state,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "Cambio de estado"
        ) { targetState ->
            when (targetState) {
                "Loading" -> Box(
                    modifier = Modifier
                        .size(180.dp)
                        .background(Color(0xFFFFC107)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Cargando...")
                }

                "Content" -> Box(
                    modifier = Modifier
                        .size(180.dp)
                        .background(Color(0xFF4CAF50)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Contenido listo", color = Color.White)
                }

                "Error" -> Box(
                    modifier = Modifier
                        .size(180.dp)
                        .background(Color(0xFFF44336)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Ocurrió un error", color = Color.White)
                }
            }
        }

        Button(onClick = {
            state = when (state) {
                "Loading" -> "Content"
                "Content" -> "Error"
                else -> "Loading"
            }
        }) {
            Text("Cambiar estado")
        }
    }
}
