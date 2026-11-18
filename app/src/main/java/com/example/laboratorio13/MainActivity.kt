
package com.example.laboratorio13

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalDensity
import com.example.laboratorio13.ui.theme.Laboratorio13Theme
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random

private val Fondo = Color(0xFF080D20)
private val Panel = Color(0xFF111D38)
private val Cian = Color(0xFF00E5FF)
private val Violeta = Color(0xFF9C5CFF)

data class Meteor(
    val id: Int,
    val x: Float,
    val y: Float,
    val speed: Float,
    val symbol: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Laboratorio13Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Fondo
                ) { innerPadding ->
                    BalloonGame(
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
fun BalloonGame(modifier: Modifier = Modifier) {
    var playing by remember { mutableStateOf(false) }
    var balloonX by remember { mutableFloatStateOf(0.5f) }
    var score by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var explosion by remember { mutableStateOf(false) }
    var flash by remember { mutableStateOf(false) }

    val meteors = remember { mutableStateListOf<Meteor>() }
    var nextId by remember { mutableIntStateOf(0) }

    val flashAlpha by animateFloatAsState(
        targetValue = if (flash) 0.55f else 0f,
        animationSpec = tween(350),
        label = "Flash de daño"
    )

    val explosionScale by animateFloatAsState(
        targetValue = if (explosion) 1.5f else 0.2f,
        animationSpec = tween(250),
        label = "Tamaño de explosión"
    )

    val explosionAlpha by animateFloatAsState(
        targetValue = if (explosion) 1f else 0f,
        animationSpec = tween(250),
        label = "Transparencia de explosión"
    )

    val balloonColor by animateColorAsState(
        targetValue = when (lives) {
            3 -> Cian
            2 -> Color(0xFFFFB74D)
            else -> Color(0xFFFF4081)
        },
        label = "Color del globo"
    )

    val infinite = rememberInfiniteTransition(label = "Animaciones")
    val balloonBounce by infinite.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(
            tween(900, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "Rebote del globo"
    )

    LaunchedEffect(lives) {
        if (lives < 3 && lives > 0) {
            flash = true
            delay(220)
            flash = false
        }
    }

    LaunchedEffect(playing) {
        var ticks = 0

        while (playing && lives > 0) {
            delay(30)
            ticks++

            for (i in meteors.indices.reversed()) {
                val meteor = meteors[i]
                val updated = meteor.copy(
                    y = meteor.y + meteor.speed
                )

                if (
                    updated.y >= 0.76f &&
                    updated.y <= 0.88f &&
                    abs(updated.x - balloonX) < 0.12f
                ) {
                    meteors.removeAt(i)
                    lives--
                    explosion = true

                    if (lives <= 0) {
                        playing = false
                    }
                } else if (updated.y >= 1f) {
                    meteors.removeAt(i)
                    score += 10
                } else {
                    meteors[i] = updated
                }
            }

            if (ticks % 22 == 0 && meteors.size < 9) {
                nextId++
                meteors.add(
                    Meteor(
                        id = nextId,
                        x = Random.nextFloat() * 0.85f + 0.075f,
                        y = 0f,
                        speed = 0.008f + (score / 10000f).coerceAtMost(0.008f)
                                + Random.nextFloat() * 0.005f,
                        symbol = listOf("☄️", "🪨", "🔥", "💫").random()
                    )
                )
            }

            if (ticks % 12 == 0) {
                explosion = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Fondo)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "NEON DEFENDER",
            color = Cian,
            fontSize = 25.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 2.sp
        )

        Text(
            "PROTECT THE BALLOON",
            color = Color.LightGray,
            fontSize = 10.sp,
            letterSpacing = 3.sp
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            HudItem("PUNTOS", score)

            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                repeat(3) { index ->
                    HeartItem(index < lives)
                }
            }
        }

        Spacer(Modifier.height(14.dp))

        BoxWithConstraints(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Panel)
        ) {
            val fieldWidth = maxWidth
            val fieldHeight = maxHeight

            Text(
                "✦       ·        ✧       ·       ✦",
                color = Violeta,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
            )

            meteors.forEach { meteor ->
                Text(
                    meteor.symbol,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .offset(
                            x = fieldWidth * meteor.x - 18.dp,
                            y = fieldHeight * meteor.y
                        )
                        .graphicsLayer {
                            rotationZ = meteor.y * 360f
                        }
                )
            }

            Text(
                "🎈",
                fontSize = 52.sp,
                modifier = Modifier
                    .offset(
                        x = fieldWidth * balloonX - 25.dp,
                        y = fieldHeight * 0.77f + balloonBounce.dp
                    )
            )

            Text(
                "🛡️",
                fontSize = 35.sp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 14.dp)
            )

            if (explosion) {
                Text(
                    "💥",
                    fontSize = 65.sp,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .graphicsLayer {
                            scaleX = explosionScale
                            scaleY = explosionScale
                            alpha = explosionAlpha
                        }
                )
            }

            AnimatedContent(
                targetState = playing,
                transitionSpec = {
                    fadeIn() togetherWith fadeOut()
                },
                modifier = Modifier.align(Alignment.Center),
                label = "Estado del juego"
            ) { active ->
                if (!active) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            if (lives <= 0) "GAME OVER" else "READY?",
                            color = Cian,
                            fontSize = 27.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            if (lives <= 0) "Puntuación: $score"
                            else "Protege tu globo",
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Red.copy(alpha = flashAlpha))
            )
        }

        Spacer(Modifier.height(12.dp))

        Text(
            "MUEVE EL GLOBO PARA ESQUIVAR",
            color = Color.LightGray,
            fontSize = 10.sp,
            letterSpacing = 1.sp
        )

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = {
                    balloonX = (balloonX - 0.13f).coerceAtLeast(0.08f)
                },
                enabled = playing,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Violeta
                )
            ) {
                Text("◀ IZQUIERDA", fontSize = 11.sp)
            }

            Button(
                onClick = {
                    balloonX = (balloonX + 0.13f).coerceAtMost(0.92f)
                },
                enabled = playing,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Cian,
                    contentColor = Fondo
                )
            ) {
                Text("DERECHA ▶", fontSize = 11.sp)
            }
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                score = 0
                lives = 3
                balloonX = 0.5f
                meteors.clear()
                explosion = false
                playing = true
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF00C853)
            )
        ) {
            Text(
                if (lives <= 0) "VOLVER A JUGAR"
                else if (playing) "REINICIAR PARTIDA"
                else "INICIAR PARTIDA",
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HudItem(title: String, score: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Panel, RoundedCornerShape(12.dp))
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {
        Text(title, color = Cian, fontSize = 10.sp)

        AnimatedContent(
            targetState = score,
            transitionSpec = {
                fadeIn() togetherWith fadeOut()
            },
            label = "Puntuación"
        ) { value ->
            Text(
                value.toString(),
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun HeartItem(active: Boolean) {
    val infinite = rememberInfiniteTransition(label = "Corazón")
    val wave by infinite.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            tween(650, easing = FastOutSlowInEasing),
            RepeatMode.Reverse
        ),
        label = "Movimiento del corazón"
    )

    val color by animateColorAsState(
        targetValue = if (active) Color(0xFFFF405C)
        else Color(0xFF39445A),
        label = "Color del corazón"
    )

    Text(
        "♥",
        color = color,
        fontSize = 25.sp,
        modifier = Modifier.offset(y = wave.dp)
    )
}
