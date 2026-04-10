package general

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object AnalizadorLog {
    private val formateador = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun parsear(linea: String): EntradaLog? {
        return try {

            if (!linea.startsWith("[") || !linea.contains("] ")) return null

            val fechaStr = linea.substringAfter("[").substringBefore("]")

            val resto = linea.substringAfter("] ").trim()

            val partes = resto.split(" ", limit = 2)
            if (partes.size < 2) return null

            val nivelStr = partes[0]
            val mensaje = partes[1]

            val fechaHora = LocalDateTime.parse(fechaStr, formateador)
            val nivel = NivelLog.valueOf(nivelStr)

            EntradaLog(
                fechaHora = fechaHora,
                nivel = nivel,
                mensaje = mensaje,
                lineaOriginal = linea
            )
        } catch (e: Exception) {
            null
        }
    }
}