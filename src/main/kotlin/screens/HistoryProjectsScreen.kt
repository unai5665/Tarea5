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
import kotlinx.coroutines.launch
import model.Project

/**
 * Screen para mostrar el historial de proyectos terminados asignados a un gestor.
 * Se espera que se reciba el id del gestor (por ejemplo, obtenido tras el login)
 * y el nombre de usuario para la visualización.
 */
class HistoryProjectsScreen(private val gestorId: Int, private val userName: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        val scope = rememberCoroutineScope()

        var historyProjects by remember { mutableStateOf<List<Project>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf("") }

        // Cargar los proyectos terminados desde la API al iniciar
        LaunchedEffect(Unit) {
            try {
                historyProjects = ProjectApi.fetchFinishedProjects(gestorId)
            } catch (e: Exception) {
                errorMessage = "Error al cargar proyectos: ${e.message}"
            }
            isLoading = false
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Historial de Proyectos Terminados", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                errorMessage.isNotEmpty() -> {
                    Text(errorMessage, color = Color.Red)
                }
                historyProjects.isEmpty() -> {
                    Text("No se encontraron proyectos terminados.")
                }
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(historyProjects) { project ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                elevation = 4.dp
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text("Proyecto: ${project.name}", style = MaterialTheme.typography.h6)
                                    Text("Cliente: ${project.owner}", style = MaterialTheme.typography.body2)
                                    project.fechaFinalizacion?.let { fechaFin ->
                                        Text("Finalizado el: $fechaFin", style = MaterialTheme.typography.body2)
                                    }
                                    // Puedes agregar más detalles si lo requieres
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navigator?.pop() },
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Volver", color = Color.White)
            }
        }
    }
}
