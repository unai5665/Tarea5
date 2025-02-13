import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import model.*
import network.NetworkUtils

object ProjectApi {

    // Función para obtener todos los proyectos
    suspend fun fetchProjects(): List<Project> {
        return try {
            val response: HttpResponse = NetworkUtils.httpClient.get("http://127.0.0.1:5000/proyecto/proyectos")
            if (response.status.isSuccess()) {
                response.body<List<Project>>() // Deserializa la respuesta a una lista de proyectos
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error al obtener proyectos: ${e.message}")
            emptyList() // Devuelve lista vacía si hay un error
        }
    }

    // Función para obtener el historial de proyectos terminados para un gestor
    suspend fun fetchFinishedProjects(gestorId: Int): List<Project> {
        return try {
            val response: HttpResponse = NetworkUtils.httpClient.get("http://127.0.0.1:5000/proyecto/historial_gestor") {
                parameter("id", gestorId)
            }
            if (response.status.isSuccess()) {
                response.body<List<Project>>() // Deserializa la respuesta a una lista de proyectos
            } else {
                emptyList() // Si la respuesta no es exitosa, retorna una lista vacía
            }
        } catch (e: Exception) {
            println("Error al obtener el historial de proyectos terminados: ${e.message}")
            emptyList() // Devuelve lista vacía en caso de error
        }
    }

    suspend fun fetchMyProjects(gestorId: Int): List<Project> {
        return try {
            val response: HttpResponse = NetworkUtils.httpClient.get("http://127.0.0.1:5000/proyecto/proyectos_gestor") {
                parameter("id", gestorId) // Enviamos el ID del gestor
            }
            if (response.status.isSuccess()) {
                response.body<List<Project>>() // Convertimos la respuesta a la lista de proyectos
            } else {
                emptyList() // Si hay error en la respuesta, devolvemos lista vacía
            }
        } catch (e: Exception) {
            println("Error al obtener proyectos del gestor: ${e.message}")
            emptyList()
        }
    }
    // Obtener tareas de un proyecto
    suspend fun fetchTasks(projectId: Int): List<Task> {
        return try {
            val response: HttpResponse = NetworkUtils.httpClient.get("http://127.0.0.1:5000/proyecto/tareas") {
                parameter("proyecto_id", projectId)
            }
            if (response.status.isSuccess()) {
                response.body<List<Task>>()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error al obtener tareas: ${e.message}")
            emptyList()
        }
    }

    // Obtener desarrolladores disponibles
    // Obtener programadores disponibles usando el endpoint correspondiente
    suspend fun fetchDevelopers(): List<Developer> {
        return try {
            val response: HttpResponse = NetworkUtils.httpClient.get("http://127.0.0.1:5000/empleado/programadores")
            if (response.status.isSuccess()) {
                response.body<List<Developer>>()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error al obtener programadores: ${e.message}")
            emptyList()
        }
    }


    // 🔹 Obtener gestores disponibles
    // Obtener gestores disponibles filtrando por ID (3 y 4)
    suspend fun fetchManagers(): List<Manager> {
        return try {
            val response: HttpResponse = NetworkUtils.httpClient.get("http://127.0.0.1:5000/empleado/empleados")
            if (response.status.isSuccess()) {
                val employees: List<Employee> = response.body()
                // Filtramos aquellos empleados cuyo ID sea 3 o 4
                employees.filter { it.id == 3 || it.id == 4 }
                    .map { Manager(id = it.id, name = it.nombre) }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println("Error al obtener gestores: ${e.message}")
            emptyList()
        }
    }


    // Crear una nueva tarea
    suspend fun assignTask(projectId: Int, taskName: String, taskDescription: String, developerId: Int?): Boolean {
        return try {
            // Nota: Se usa el endpoint definido en el backend para asignar tareas
            val response: HttpResponse = NetworkUtils.httpClient.post("http://127.0.0.1:5000/proyecto/asignar_tarea") {
                contentType(ContentType.Application.Json)
                setBody(
                    mapOf(
                        "nombre" to taskName,
                        "descripcion" to taskDescription,
                        "proyecto" to projectId,
                        "programador" to developerId
                    )
                )
            }
            response.status.isSuccess()
        } catch (e: Exception) {
            println("Error al asignar tarea: ${e.message}")
            false
        }
    }

}
