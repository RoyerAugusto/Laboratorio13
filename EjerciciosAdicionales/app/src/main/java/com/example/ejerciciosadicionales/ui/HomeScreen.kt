package com.example.ejerciciosadicionales.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ejerciciosadicionales.ui.theme.Dogwood
import com.example.ejerciciosadicionales.ui.theme.Green
import com.example.ejerciciosadicionales.ui.theme.GreenLight
import com.example.ejerciciosadicionales.ui.theme.PaleDogwood
import com.example.ejerciciosadicionales.ui.theme.Seashell
import kotlinx.coroutines.delay

enum class TabPage(val titulo: String) { Home("Casa"), Work("Trabajo") }

private val temas = listOf(
    "Compras" to "Leche, pan, frutas y verduras para la semana. Toca de nuevo para cerrar.",
    "Estudio" to "Terminar el laboratorio 13 de Compose y repasar las animaciones del codelab.",
    "Deporte" to "Correr 30 minutos por la mañana y estirar al terminar."
)

private val tareasCasa = listOf("Lavar la ropa", "Regar las plantas", "Sacar la basura", "Cocinar el almuerzo")
private val tareasTrabajo = listOf("Responder correos", "Enviar el informe", "Reunión de equipo", "Revisar pull requests")

@Composable
fun HomeScreen() {
    var tabPage by remember { mutableStateOf(TabPage.Home) }
    var cargando by remember { mutableStateOf(false) }
    var mensajeVisible by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val tareasHome = remember { mutableStateListOf(*tareasCasa.toTypedArray()) }
    val tareasWork = remember { mutableStateListOf(*tareasTrabajo.toTypedArray()) }
    val tareas = if (tabPage == TabPage.Home) tareasHome else tareasWork

    // TODO 1: animar el color de fondo al cambiar de pestaña
    val backgroundColor by animateColorAsState(
        targetValue = if (tabPage == TabPage.Home) Seashell else GreenLight,
        label = "background color"
    )

    LaunchedEffect(mensajeVisible) {
        if (mensajeVisible) {
            delay(2500)
            mensajeVisible = false
        }
    }
    LaunchedEffect(cargando) {
        if (cargando) {
            delay(3000)
            cargando = false
        }
    }

    // El FAB se extiende solo cuando la lista está arriba del todo
    val extendido = listState.firstVisibleItemIndex == 0

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            Box(Modifier.background(backgroundColor).statusBarsPadding()) {
                HomeTabBar(backgroundColor, tabPage) { tabPage = it }
            }
        },
        floatingActionButton = {
            HomeFloatingActionButton(extendido) { mensajeVisible = true }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Hoy, ${tabPage.titulo}",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Button(onClick = { cargando = true }, enabled = !cargando) { Text("Actualizar") }
                    }
                }
                item { if (cargando) LoadingRow() }
                item { Text("Temas (toca para expandir)", style = MaterialTheme.typography.titleMedium) }
                items(temas, key = { it.first }) { (titulo, cuerpo) -> TopicRow(titulo, cuerpo) }
                item {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            "Tareas (desliza para descartar)",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.weight(1f)
                        )
                        TextButton(onClick = {
                            val base = if (tabPage == TabPage.Home) tareasCasa else tareasTrabajo
                            tareas.clear()
                            tareas.addAll(base)
                        }) { Text("Restaurar") }
                    }
                }
                items(tareas.toList(), key = { "${tabPage.name}-$it" }) { tarea ->
                    TaskRow(tarea, onDismissed = { tareas.remove(tarea) })
                }
                item { VariantesCard() }
            }
            EditMessage(mensajeVisible)
        }
    }
}

/** Sección 4 (TODO 2-1): AnimatedVisibility en lugar de if (extended). */
@Composable
private fun HomeFloatingActionButton(extended: Boolean, onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = Green,
        contentColor = Color.White
    ) {
        Icon(Icons.Default.Edit, contentDescription = "Editar")
        AnimatedVisibility(extended) {
            Text("EDITAR", modifier = Modifier.padding(start = 8.dp))
        }
    }
}

/** Sección 4 (TODO 2-2): mensaje que entra y sale deslizándose desde arriba. */
@Composable
private fun EditMessage(shown: Boolean) {
    AnimatedVisibility(
        visible = shown,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
        )
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Dogwood),
            shape = RoundedCornerShape(0.dp)
        ) {
            Text(
                "La edición aún no está disponible",
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

/** Sección 5 (TODO 3): animateContentSize al expandir/colapsar. */
@Composable
private fun TopicRow(topic: String, body: String) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        onClick = { expanded = !expanded }
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Text(topic, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            if (expanded) {
                Text(body, modifier = Modifier.padding(top = 8.dp))
            }
        }
    }
}

/** Sección 6 (TODO 4): updateTransition animando borde izquierdo, derecho y color. */
@Composable
private fun HomeTabBar(background: Color, tabPage: TabPage, onTabSelected: (TabPage) -> Unit) {
    TabRow(
        selectedTabIndex = tabPage.ordinal,
        containerColor = background,
        indicator = { tabPositions -> HomeTabIndicator(tabPositions, tabPage) },
        divider = {}
    ) {
        TabPage.entries.forEach { page ->
            Tab(
                selected = page == tabPage,
                onClick = { onTabSelected(page) },
                modifier = Modifier.padding(vertical = 8.dp),
                text = { Text(page.titulo, fontWeight = FontWeight.Medium) }
            )
        }
    }
}

@Composable
private fun HomeTabIndicator(tabPositions: List<TabPosition>, tabPage: TabPage) {
    val transition = updateTransition(tabPage, label = "Tab indicator")
    // Efecto elástico: el borde que se acerca al destino se mueve más rápido
    val indicatorLeft by transition.animateDp(
        transitionSpec = {
            if (TabPage.Home isTransitioningTo TabPage.Work) {
                spring(stiffness = Spring.StiffnessVeryLow)
            } else {
                spring(stiffness = Spring.StiffnessMedium)
            }
        },
        label = "Indicator left"
    ) { page -> tabPositions[page.ordinal].left }
    val indicatorRight by transition.animateDp(
        transitionSpec = {
            if (TabPage.Home isTransitioningTo TabPage.Work) {
                spring(stiffness = Spring.StiffnessMedium)
            } else {
                spring(stiffness = Spring.StiffnessVeryLow)
            }
        },
        label = "Indicator right"
    ) { page -> tabPositions[page.ordinal].right }
    val color by transition.animateColor(label = "Border color") { page ->
        if (page == TabPage.Home) PaleDogwood else Green
    }
    Box(
        Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.BottomStart)
            .offset(x = indicatorLeft)
            .width(indicatorRight - indicatorLeft)
            .padding(4.dp)
            .fillMaxSize()
            .border(BorderStroke(2.dp, color), RoundedCornerShape(4.dp))
    )
}

/** Sección 7 (TODO 5): animación infinita con keyframes para el placeholder de carga. */
@Composable
private fun LoadingRow() {
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = 1000
                0.7f at 500
            },
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    Row(
        Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(Modifier.size(40.dp).alpha(alpha).background(Color.LightGray, RoundedCornerShape(20.dp)))
        Box(
            Modifier
                .padding(start = 12.dp)
                .fillMaxWidth()
                .height(24.dp)
                .alpha(alpha)
                .background(Color.LightGray, RoundedCornerShape(4.dp))
        )
    }
}

/** Sección 8: tarea que se descarta con el gesto swipeToDismiss. */
@Composable
private fun TaskRow(task: String, onDismissed: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().swipeToDismiss(onDismissed),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Text(task, modifier = Modifier.fillMaxWidth().padding(16.dp))
    }
}

private enum class Variante(val nombre: String) { Tween("tween"), Spring("spring"), Keyframes("keyframes") }

/** Variantes de animationSpec (Secciones 3 a 7): se comparan sobre una misma animación. */
@Composable
private fun VariantesCard() {
    var variante by remember { mutableStateOf(Variante.Tween) }
    var alFinal by remember { mutableStateOf(false) }
    val x by animateDpAsState(
        targetValue = if (alFinal) 180.dp else 0.dp,
        animationSpec = when (variante) {
            Variante.Tween -> tween(durationMillis = 800, easing = LinearOutSlowInEasing)
            Variante.Spring -> spring(dampingRatio = Spring.DampingRatioHighBouncy, stiffness = Spring.StiffnessLow)
            Variante.Keyframes -> keyframes {
                durationMillis = 800
                100.dp at 200
                60.dp at 400
            }
        },
        label = "variante x"
    )
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(Modifier.padding(16.dp)) {
            Text("Variantes de animación", style = MaterialTheme.typography.titleMedium)
            Row(Modifier.padding(vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Variante.entries.forEach { v ->
                    FilterChip(selected = variante == v, onClick = { variante = v }, label = { Text(v.nombre) })
                }
            }
            Box(Modifier.offset(x = x).size(48.dp).background(Green, RoundedCornerShape(12.dp)))
            Button(onClick = { alFinal = !alFinal }, modifier = Modifier.padding(top = 12.dp)) {
                Text("Mover")
            }
        }
    }
}
