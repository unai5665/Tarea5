package screens

import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator
import model.Project
import model.Task

class TaskScreen(private val task: Task, private val project: Project, private val userName: String, private val gestorId: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Detalles de la Tarea", style = MaterialTheme.typography.h4)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Nombre: ${task.name}")
            Text("Asignado a: ${task.assignedTo}")
            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(
            onClick = { navigator?.replace(ProjectScreen(project, userName, gestorId)) },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
        ) {
            Text("Volver", color = Color.White)
        }
    }
}
