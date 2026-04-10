package general

import java.time.format.DateTimeFormatter

class GeneradorInforme(
    private val stats: Estadisticas,
    private val entradas: List<EntradaLog>,
    private val config: Configuracion
) {
    private val fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    fun construir(incluirDetalle: Boolean): String {
        val sb = StringBuilder()
        val titulo = if (incluirDetalle) "INFORME DE LOGS" else "ESTADÍSTICAS DE LOGS"

        sb.appendLine(titulo).appendLine("=".repeat(titulo.length))
        sb.appendLine("Fichero analizado: ${config.rutaEntrada}")
        sb.appendLine("Resumen:")
        sb.appendLine("- Líneas procesadas: ${stats.totalProcesadas}")
        sb.appendLine("- Líneas validas: ${stats.validas}")
        sb.appendLine("- Líneas invalidas: ${stats.invalidas}")
        sb.appendLine("Conteo por nivel:")
        NivelLog.values().forEach { n -> sb.appendLine("- $n: ${stats.conteoPorNivel[n] ?: 0}") }
        sb.appendLine("Periodo detectado:")
        sb.appendLine("- Primera: ${stats.primeraFecha?.format(fmt) ?: "N/A"}")
        sb.appendLine("- Ultima: ${stats.ultimaFecha?.format(fmt) ?: "N/A"}")

        if (incluirDetalle) {
            sb.appendLine("\nEntradas encontradas:")
            entradas.forEach { sb.appendLine(it.lineaOriginal) }
        }
        return sb.toString()
    }
}