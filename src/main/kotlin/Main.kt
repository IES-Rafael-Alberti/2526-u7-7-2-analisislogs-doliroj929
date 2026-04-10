package general

import java.io.File

class LogTool {

    fun ejecutarConArgumentos(args: Array<String>) {
        val parser = ParserCli()
        val config = parser.parsear(args.toList(), Configuracion(rutaEntrada = ""))
        validarYEjecutar(config)
    }

    fun iniciarModoInteractivo() {
        println("===> LogTool <===")
        println("Escribe tus comandos (ej: logtool -i sample.log --stats) o 'salir'")
        bucleInteractivo(Configuracion(rutaEntrada = ""))
    }

    private tailrec fun bucleInteractivo(configActual: Configuracion) {
        print("logtool> ")
        val entrada = readLine()?.trim() ?: "salir"

        if (entrada.lowercase() == "salir") {
            println("Cerrando...")
            return
        }

        val tokens = entrada.split(" ").filter { it.isNotEmpty() }
        val argumentos = if (tokens.firstOrNull() == "logtool") tokens.drop(1) else tokens

        val parser = ParserCli()
        val nuevaConfig = parser.parsear(argumentos, Configuracion(rutaEntrada = ""))

        validarYEjecutar(nuevaConfig)
        bucleInteractivo(nuevaConfig)
    }

    private fun validarYEjecutar(config: Configuracion) {
        when {
            config.rutaEntrada.isEmpty() ->
                println("Error: Falta el fichero de entrada (-i).")

            config.modoStats && config.modoReport ->
                println("Error: --stats y --report son excluyentes.")

            !config.modoStats && !config.modoReport ->
                println("Error: Especifica --stats o --report.")

            else -> procesar(config)
        }
    }

    private fun procesar(config: Configuracion) {
        val archivo = File(config.rutaEntrada)
        if (!archivo.exists()) return println("Archivo no encontrado: ${config.rutaEntrada}")

        val lineas = archivo.readLines()
        val resultados = ProcesadorLog.procesar(lineas, config)

        val stats = Estadisticas(
            totalProcesadas = lineas.size,
            validas = resultados.size,
            invalidas = lineas.size - resultados.size,
            conteoPorNivel = resultados.groupBy { it.nivel }.mapValues { it.value.size },
            primeraFecha = resultados.firstOrNull()?.fechaHora,
            ultimaFecha = resultados.lastOrNull()?.fechaHora
        )

        val contenido = GeneradorInforme(stats, resultados, config)
            .construir(incluirDetalle = config.modoReport)

        EscritorSalida.enviar(contenido, config.rutaSalida, config.salidaEstandar)
    }
}

class ParserCli {

    tailrec fun parsear(args: List<String>, config: Configuracion): Configuracion {
        if (args.isEmpty()) return config

        val cabeza = args.first()
        val cola = args.drop(1)

        return when (cabeza) {
            "-i", "--input" ->
                parsear(cola.drop(1), config.copy(rutaEntrada = cola.firstOrNull()?.replace("\"", "") ?: ""))

            "-s", "--stats" -> parsear(cola, config.copy(modoStats = true))

            "-r", "--report" -> parsear(cola, config.copy(modoReport = true))

            "-p", "--stdout" -> parsear(cola, config.copy(salidaEstandar = true))

            "-l", "--level" -> {
                val niveles = cola.firstOrNull()?.split(",")?.mapNotNull {
                    try { NivelLog.valueOf(it.uppercase()) } catch(e: Exception) { null }
                } ?: emptyList()
                parsear(cola.drop(1), config.copy(niveles = niveles))
            }

            else -> parsear(cola, config)
        }
    }
}

fun main(args: Array<String>) {
    val app = LogTool()

    if (args.isNotEmpty()) {
        app.ejecutarConArgumentos(args)
    } else {
        app.iniciarModoInteractivo()
    }
}