package general

import java.io.File


object EscritorSalida {

    fun enviar(contenido: String, rutaFichero: String?, forzarConsola: Boolean) {
        if (forzarConsola || rutaFichero == null) {
            println(contenido)
        }


        if (rutaFichero != null) {
            try {
                File(rutaFichero).writeText(contenido)
                println("\n[INFO] El informe ha sido guardado en: $rutaFichero")
            } catch (e: Exception) {
                println("\n[ERROR] No se pudo escribir en el fichero: ${e.message}")
            }
        }
    }
}
