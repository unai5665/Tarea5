package screens

import cafe.adriel.voyager.core.screen.Screen
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.LocalNavigator

class WelcomeScreen(private val userName: String, private val gestorId: Int ) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE3F2FD)), // Fondo azul claro
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .background(Color.White, shape = MaterialTheme.shapes.medium)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("¡Bienvenido!, $userName", style = MaterialTheme.typography.h4)

                Spacer(modifier = Modifier.height(8.dp))

                // Mostrar el rol (Gestor)
                Text("Rol: Gestor", style = MaterialTheme.typography.body1)

                Spacer(modifier = Modifier.height(16.dp))

                // Botón para ver proyectos activos
                Button(onClick = { navigator?.push(ProjectsScreen(userName, gestorId))}) {
                    Text("Ver Proyectos Activos")
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Botón para ver historial de proyectos terminados
                Button(onClick = { navigator?.push(HistoryProjectsScreen(gestorId, userName)) }) {
                    Text("Historial de Proyectos")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botón de desconectar (volver al screens.LoginScreen)
                Button(
                    onClick = { navigator?.replace(LoginScreen()) },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text("Desconectar", color = Color.White)
                }
            }
        }
    }
}
