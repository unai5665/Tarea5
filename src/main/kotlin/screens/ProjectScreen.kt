package screens

import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import kotlinx.coroutines.launch
import model.Project
import model.Task
import model.Employee
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProjectScreen(
    private val project: Project,
    private val userName: String,
    private val gestorId: Int
) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val coroutineScope = rememberCoroutineScope()

        // Variables para cargar datos
        var tasks by remember { mutableStateOf(emptyList<Task>()) }
        var employees by remember { mutableStateOf(emptyList<Employee>()) }

        // Variables para agregar una nueva tarea
        var newTaskName by remember { mutableStateOf("") }
        var newTaskDescription by remember { mutableStateOf("") }
        var selectedTaskDeveloper by remember { mutableStateOf<Employee?>(null) }
        var isTaskDevDropdownExpanded by remember { mutableStateOf(false) }

        // Variables para asignar un desarrollador al proyecto
        var selectedProjectDeveloper by remember { mutableStateOf<Employee?>(null) }
        var isProjectDevDropdownExpanded by remember { mutableStateOf(false) }

        // Variables para asignar un gestor al proyecto
        var selectedProjectManager by remember { mutableStateOf<Employee?>(null) }
        var isProjectManagerDropdownExpanded by remember { mutableStateOf(false) }

        // Cargar datos iniciales
        LaunchedEffect(Unit) {
            tasks = ProjectApi.fetchTasks(project.id)
            employees = ProjectApi.fetchEmployees()
        }

        // Filtrar empleados
        val developers = employees.filter { it.id == 1 || it.id == 2 } // IDs 1 y 2 son programadores
        val managers = employees.filter { it.id == 3 || it.id == 4 } // IDs 3 y 4 son gestores

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Botón para volver
            Button(
                onClick = { navigator?.pop() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text("Volver", color = Color.White)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar datos del proyecto
            Text("Proyecto: ${project.name}", style = MaterialTheme.typography.h4)
            Text("Dueño: ${project.owner}", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Descripción: ${project.description}", style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(16.dp))

            // Lista de tareas del proyecto
            Text("Tareas del Proyecto", style = MaterialTheme.typography.h5)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                items(tasks) { task ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                navigator?.push(TaskDetailScreen(task))
                            },
                        backgroundColor = Color(0xFFF5F5F5),
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(task.name, style = MaterialTheme.typography.h6)
                                Text(
                                    "Asignado a: ${task.assignedTo ?: "Sin asignar"}",
                                    style = MaterialTheme.typography.body2
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección para agregar una nueva tarea
            Text("Agregar nueva tarea", style = MaterialTheme.typography.h5)
            OutlinedTextField(
                value = newTaskName,
                onValueChange = { newTaskName = it },
                label = { Text("Nombre de la tarea") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = newTaskDescription,
                onValueChange = { newTaskDescription = it },
                label = { Text("Descripción de la tarea") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Selector para asignar un desarrollador a la tarea
            Text(
                "Asignar a (Desarrollador): ${selectedTaskDeveloper?.name ?: "Ninguno"}",
                style = MaterialTheme.typography.body1
            )
            Box {
                Button(onClick = { isTaskDevDropdownExpanded = true }) {
                    Text("Seleccionar Desarrollador")
                }
                DropdownMenu(
                    expanded = isTaskDevDropdownExpanded,
                    onDismissRequest = { isTaskDevDropdownExpanded = false }
                ) {
                    developers.forEach { dev ->
                        DropdownMenuItem(onClick = {
                            selectedTaskDeveloper = dev
                            isTaskDevDropdownExpanded = false
                        }) {
                            Text(dev.name)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (newTaskName.isNotEmpty() && newTaskDescription.isNotEmpty()) {
                        coroutineScope.launch {
                            val success = ProjectApi.assignTask(
                                projectId = project.id,
                                name = newTaskName,
                                description = newTaskDescription,
                                developerId = selectedTaskDeveloper?.id
                            )
                            if (success) {
                                tasks = ProjectApi.fetchTasks(project.id)
                                newTaskName = ""
                                newTaskDescription = ""
                                selectedTaskDeveloper = null
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF4CAF50))
            ) {
                Text("Agregar Tarea", color = Color.White)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sección para asignar un gestor al proyecto
            Text("Asignar Gestor al Proyecto", style = MaterialTheme.typography.h5)
            Text(
                "Gestor asignado: ${selectedProjectManager?.name ?: "Ninguno"}",
                style = MaterialTheme.typography.body1
            )
            Box {
                Button(onClick = { isProjectManagerDropdownExpanded = true }) {
                    Text("Seleccionar Gestor")
                }
                DropdownMenu(
                    expanded = isProjectManagerDropdownExpanded,
                    onDismissRequest = { isProjectManagerDropdownExpanded = false }
                ) {
                    managers.forEach { manager ->
                        DropdownMenuItem(onClick = {
                            selectedProjectManager = manager
                            isProjectManagerDropdownExpanded = false
                        }) {
                            Text(manager.name)
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    selectedProjectManager?.let { manager ->
                        coroutineScope.launch {
                            val currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
                            val success = ProjectApi.assignManagerToProject(
                                projectId = project.id,
                                managerId = manager.id,
                                assignmentDate = currentDate
                            )
                            if (success) {
                                // Notificación de éxito
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF9C27B0))
            ) {
                Text("Asignar Gestor al Proyecto", color = Color.White)
            }
        }
    }
}
