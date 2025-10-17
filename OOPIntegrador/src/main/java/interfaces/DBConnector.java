package interfaces;

import java.sql.Connection;

/**
 * Interfaz para manejar conexiones con la base de datos.
 * <p>
 * Define el contrato para las clases que implementen un conector de base de datos.
 * Permite cambiar fácilmente la implementación sin afectar el resto del sistema.
 * </p>
 *
 * <p>Ejemplo de implementación: {@link models.SQLConnector}</p>
 *
 * @author
 *   Mateo Santarsiero
 * @version 1.0
 */
public interface DBConnector {

    /**
     * Establece una conexión con la base de datos.
     *
     * @return una instancia de {@link Connection} si la conexión es exitosa,
     *         o {@code null} si ocurre algún error.
     */
    Connection Connect();
}
