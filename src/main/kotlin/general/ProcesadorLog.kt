package general

object ProcesadorLog {

    fun procesar(lineas: List<String>, config: Configuracion): List<EntradaLog> {
        return lineas
            .mapNotNull { AnalizadorLog.parsear(it) }
            .filter { filtrarPorNivel(it, config) }
            .filter { filtrarPorFecha(it, config) }
    }

    private fun filtrarPorNivel(entrada: EntradaLog, config: Configuracion): Boolean {
        // En tu imagen 'niveles' es una Lista. Si está vacía, pasan todos.
        return if (config.niveles.isEmpty()) true
        else config.niveles.contains(entrada.nivel)
    }

    private fun filtrarPorFecha(entrada: EntradaLog, config: Configuracion): Boolean {
        // Usamos 'fechaHora', 'fechaDesde' y 'fechaHasta' de tu imagen
        val despuesDe = config.fechaDesde?.let { entrada.fechaHora.isAfter(it) || entrada.fechaHora.isEqual(it) } ?: true
        val antesDe = config.fechaHasta?.let { entrada.fechaHora.isBefore(it) || entrada.fechaHora.isEqual(it) } ?: true

        return despuesDe && antesDe
    }
}