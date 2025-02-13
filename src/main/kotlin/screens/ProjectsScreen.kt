package screens

import cafe.adriel.voyager.core.screen.Screen
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
import model.Project

class ProjectsScreen(private val userName: String, private val gestorId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        var projects by remember { mutableStateOf<List<Project>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var selectedFilter by remember { mutableStateOf("Todos") }

        // Cargar los proyectos según el filtro
        LaunchedEffect(selectedFilter) {
            isLoading = true
            projects = if (selectedFilter == "Míos") {
                ProjectApi.fetchMyProjects(gestorId)
            } else {
                ProjectApi.fetchProjects()
            }
            isLoading = false
        }

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Proyectos Activos", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(8.dp))

            // Filtro de proyectos
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(
                    selected = selectedFilter == "Todos",
                    onClick = { selectedFilter = "Todos" }
                )
                Text("Todos")
                Spacer(modifier = Modifier.width(16.dp))
                RadioButton(
                    selected = selectedFilter == "Míos",
                    onClick = { selectedFilter = "Míos" }
                )
                Text("Míos")
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn {
                    items(projects) { project -> // ✅ Cambiado de `filteredProjects` a `projects`
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            backgroundColor = Color(0xFFF5F5F5),
                            elevation = 4.dp
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(project.name, style = MaterialTheme.typography.h6)
                                    Text("Dueño: ${project.owner}", style = MaterialTheme.typography.body2)
                                }
                                Button(
                                    onClick = { navigator?.replace(ProjectScreen(project, userName, gestorId)) }
                                ) {
                                    Text("Abrir")
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navigator?.replace(WelcomeScreen(userName, gestorId)) },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
            ) {
                Text("Volver", color = Color.White)
            }
        }
    }
}
