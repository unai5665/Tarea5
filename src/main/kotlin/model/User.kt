package model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class User(
    @SerialName("id_empleado") var idEmpleado: Int,
    @SerialName("id_gestor") var idGestor: Int,
    @SerialName("nombre") var nombre: String,
    @SerialName("email") var email: String
)

@Serializable
data class Project(
    val id: Int,
    @SerialName("nombre") val name: String,
    @SerialName("cliente") val owner: Int,
    @SerialName("descripcion") val description: String,
    @SerialName("fecha_creacion") val fechaCreacion: String? = null,
    @SerialName("fecha_finalizacion") val fechaFinalizacion: String? = null,
    @SerialName("fecha_inicio") val fechaInicio: String? = null
)

@Serializable
data class Task(
    val id: Int,
    @SerialName("nombre") val name: String,
    @SerialName("descripcion") val description: String,
    @SerialName("estimacion") val estimation: Double,  // Puede ser Double o Int según la BD
    @SerialName("fecha_creacion") val fechaCreacion: String? = null,
    @SerialName("fecha_finalizacion") val fechaFinalizacion: String? = null,
    @SerialName("programador") val assignedTo: Int? = null,
    @SerialName("proyecto") val projectId: Int  // Relación con la tabla proyecto
)

// Nueva clase para representar empleados (tanto programadores como gestores)
@Serializable
data class Employee(
    val id: Int,
    @SerialName("nombre") val name: String,
    @SerialName("email") val email: String,
    @SerialName("area") val area: String,
    @SerialName("clase") val clase: String,
    @SerialName("nivel") val nivel: String
)
