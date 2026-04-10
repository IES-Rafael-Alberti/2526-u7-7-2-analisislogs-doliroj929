package general

import java.time.LocalDateTime

data class EntradaLog(
    val fechaHora: LocalDateTime,
    val nivel: NivelLog,
    val mensaje: String,
    val lineaOriginal: String
)