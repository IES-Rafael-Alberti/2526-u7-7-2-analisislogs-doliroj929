package general

import java.time.LocalDateTime

data class Estadisticas(
    val totalProcesadas: Int,
    val validas: Int,
    val invalidas: Int,
    val conteoPorNivel: Map<NivelLog, Int>,
    val primeraFecha: LocalDateTime?,
    val ultimaFecha: LocalDateTime?
)