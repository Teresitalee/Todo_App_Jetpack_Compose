package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.todo.ui.theme.TodoTheme

class CounterAppActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TodoTheme {
                CounterApp(
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterApp(onBack: () -> Unit) {

    var count by rememberSaveable { mutableIntStateOf(0) }

    //  Animación del número
    val animatedCount by animateIntAsState(
        targetValue = count,
        animationSpec = spring(dampingRatio = 0.4f),
        label = "counterAnimation"
    )

    //  Color dinámico
    val numberColor = when {
        count > 0 -> Color(0xFF2E7D32) // verde
        count < 0 -> Color(0xFFC62828) // rojo
        else -> MaterialTheme.colorScheme.onSurface
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Counter - Contador") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Presiona Btn:")

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "$animatedCount",
                style = MaterialTheme.typography.displayLarge,
                color = numberColor
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                OutlinedButton(onClick = { count-- }) {
                    Text("-")
                }

                OutlinedButton(onClick = { count = 0 }) {
                    Text("Reset")
                }

                OutlinedButton(onClick = { count++ }) {
                    Text("+")
                }
            }
        }
    }
}