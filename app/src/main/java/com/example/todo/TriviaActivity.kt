package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
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

class TriviaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TodoTheme {
                TriviaScreen(onBack = { finish() })
            }
        }
    }
}

data class Question(
    val text: String,
    val options: List<String>,
    val correctIndex: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TriviaScreen(onBack: () -> Unit) {

    val questions = remember {
        listOf(
            Question("¿Qué lenguaje usa Jetpack Compose?", listOf("Java", "Kotlin", "Swift", "Python"), 1),
            Question("¿Qué componente muestra listas?", listOf("RecyclerView", "LazyColumn", "ListView", "Grid"), 1),
            Question("¿Qué anotación define UI?", listOf("@Composable", "@View", "@Screen", "@UI"), 0),
            Question("¿Qué guarda estado al rotar?", listOf("remember", "rememberSaveable", "var", "lateinit"), 1),
            Question("¿Qué layout es vertical?", listOf("Row", "Box", "Column", "Stack"), 2),
            Question("¿Qué librería de diseño usa Compose?", listOf("Material3", "Bootstrap", "UIKit", "Flutter"), 0),
            Question("¿Qué función inicia UI?", listOf("setContent", "onCreate", "build", "startUI"), 0),
            Question("¿Qué componente tiene barra superior?", listOf("Scaffold", "Column", "LazyRow", "Card"), 0),
            Question("¿Qué muestra opción única?", listOf("Checkbox", "RadioButton", "Switch", "Slider"), 1),
            Question("¿Qué estructura repite elementos?", listOf("LazyColumn", "Box", "Spacer", "Padding"), 0),
            Question("¿Qué guarda lista observable?", listOf("ArrayList", "mutableStateListOf", "List", "Map"), 1),
            Question("¿Qué maneja navegación simple?", listOf("Intent", "Router", "Link", "URL"), 0),
            Question("¿Qué layout alinea horizontal?", listOf("Column", "Box", "Row", "Stack"), 2),
            Question("¿Qué muestra texto?", listOf("Text", "Label", "Paragraph", "Span"), 0),
            Question("¿Qué permite scroll eficiente?", listOf("LazyColumn", "Column", "Box", "GridLayout"), 0),
            Question("¿Qué componente permite mostrar botones en Compose?", listOf("Text", "Button", "Image", "Spacer"), 1)
        )
    }

    var currentIndex by rememberSaveable { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var score by rememberSaveable { mutableIntStateOf(0) }
    var showFeedback by remember { mutableStateOf(false) }
    var lives by rememberSaveable { mutableIntStateOf(3) }
    var quizFinished by rememberSaveable { mutableStateOf(false) }

    val currentQuestion = questions[currentIndex]
    val progress = (currentIndex + 1).toFloat() / questions.size
    val animatedProgress by animateFloatAsState(progress, label = "progress")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trivia App") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        if (quizFinished) {

            val percentage = (score * 100) / questions.size

            val emoji = when {
                percentage >= 80 -> "🏆🔥"
                percentage >= 50 -> "🙂👍"
                else -> "😅📚"
            }

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text("Resultado Final", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Text(emoji, style = MaterialTheme.typography.displayLarge)

                Spacer(modifier = Modifier.height(16.dp))

                Text("Puntaje: $score / ${questions.size}")
                Text("Porcentaje: $percentage%")
                Text("Vidas restantes: $lives")

                Spacer(modifier = Modifier.height(24.dp))

                Button(onClick = {
                    currentIndex = 0
                    score = 0
                    lives = 3
                    selectedAnswer = null
                    showFeedback = false
                    quizFinished = false
                }) {
                    Text("Reiniciar")
                }
            }

        } else {

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Text("Pregunta ${currentIndex + 1} de ${questions.size}")
                Text("Vidas: $lives")

                LinearProgressIndicator(
                    progress = animatedProgress,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    currentQuestion.text,
                    style = MaterialTheme.typography.titleLarge
                )

                currentQuestion.options.forEachIndexed { index, option ->

                    val backgroundColor =
                        if (showFeedback && index == currentQuestion.correctIndex)
                            Color(0xFFD4EDDA)
                        else if (showFeedback && selectedAnswer == index && selectedAnswer != currentQuestion.correctIndex)
                            Color(0xFFF8D7DA)
                        else
                            Color.Transparent

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = selectedAnswer == index,
                                onClick = { if (!showFeedback) selectedAnswer = index }
                            )
                            .padding(8.dp)
                    ) {
                        RadioButton(
                            selected = selectedAnswer == index,
                            onClick = { if (!showFeedback) selectedAnswer = index }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(option)
                    }
                }

                if (showFeedback) {
                    val correct = selectedAnswer == currentQuestion.correctIndex
                    Text(
                        if (correct) "✅ Correcto" else "❌ Incorrecto",
                        color = if (correct) Color(0xFF2E7D32) else Color.Red
                    )
                }

                Button(
                    onClick = {

                        if (!showFeedback) {
                            if (selectedAnswer == currentQuestion.correctIndex) {
                                score++
                            } else {
                                lives--
                            }
                            showFeedback = true
                        } else {

                            if (lives == 0 || currentIndex == questions.lastIndex) {
                                quizFinished = true
                            } else {
                                currentIndex++
                                selectedAnswer = null
                                showFeedback = false
                            }
                        }
                    },
                    enabled = selectedAnswer != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        if (showFeedback && currentIndex == questions.lastIndex)
                            "Ver resultados"
                        else if (showFeedback)
                            "Siguiente"
                        else
                            "Confirmar"
                    )
                }
            }
        }
    }
}