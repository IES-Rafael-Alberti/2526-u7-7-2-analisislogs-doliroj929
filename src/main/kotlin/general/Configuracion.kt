package general

import java.time.LocalDateTime

data class Configuracion(
    val rutaEntrada: String,
    val fechaDesde: LocalDateTime? = null,
    val fechaHasta: LocalDateTime? = null,
    val niveles: List<NivelLog> = emptyList(),
    val modoStats: Boolean = false,
    val modoReport: Boolean = false,
    val rutaSalida: String? = null,
    val salidaEstandar: Boolean = true,
    val ignorarInvalidas: Boolean = false
)