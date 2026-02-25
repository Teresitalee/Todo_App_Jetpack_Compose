package com.example.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todo.ui.theme.TodoTheme

class TodoAppActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TodoTheme {
                TodoApp(onBack = { finish() })
            }
        }
    }
}

data class TodoItem(
    val id: Int,
    val text: String,
    val done: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoApp(onBack: () -> Unit) {

    var newTaskText by rememberSaveable { mutableStateOf("") }

    val todos = remember {
        mutableStateListOf<TodoItem>()
    }

    val pendingTasks = todos.filter { !it.done }
    val completedTasks = todos.filter { it.done }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todo - Tareas") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (newTaskText.trim().isNotEmpty()) {

                        val newId =
                            if (todos.isEmpty()) 1
                            else todos.maxOf { it.id } + 1

                        todos.add(
                            TodoItem(
                                id = newId,
                                text = newTaskText.trim()
                            )
                        )

                        newTaskText = ""
                    }
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Input
            item {
                OutlinedTextField(
                    value = newTaskText,
                    onValueChange = { newTaskText = it },
                    label = { Text("Nueva tarea") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Pendientes
            if (pendingTasks.isNotEmpty()) {

                item {
                    Text(
                        text = "Pendientes (${pendingTasks.size})",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                items(pendingTasks, key = { it.id }) { task ->
                    TodoCard(task, todos)
                }
            }

            // Completadas
            if (completedTasks.isNotEmpty()) {

                item {
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Completadas (${completedTasks.size})",
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                items(completedTasks, key = { it.id }) { task ->
                    TodoCard(task, todos)
                }
            }
        }
    }
}

@Composable
fun TodoCard(task: TodoItem, todos: MutableList<TodoItem>) {

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val index = todos.indexOf(task)
                todos[index] = task.copy(done = !task.done)
            }
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = task.text,
                modifier = Modifier.weight(1f),
                color = if (task.done)
                    Color(0xFF2E7D32)
                else
                    MaterialTheme.colorScheme.onSurface,
                textDecoration =
                    if (task.done)
                        TextDecoration.LineThrough
                    else
                        TextDecoration.None
            )

            IconButton(
                onClick = { todos.remove(task) }
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color(0xFFC62828)
                )
            }
        }
    }
}