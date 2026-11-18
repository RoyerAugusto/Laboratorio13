
package com.example.laboratorio13

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
                    SizePositionExercise(
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
fun SizePositionExercise(modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }

    val boxSize by animateDpAsState(
        targetValue = if (expanded) 180.dp else 100.dp,
        label = "Tamaño"
    )

    val boxOffset by animateDpAsState(
        targetValue = if (expanded) 80.dp else 0.dp,
        label = "Posición"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { expanded = !expanded }) {
            Text("Mover y cambiar tamaño")
        }

        Box(
            modifier = Modifier
                .offset(x = boxOffset)
                .size(boxSize)
                .background(Color(0xFF2196F3))
        )
    }
}
