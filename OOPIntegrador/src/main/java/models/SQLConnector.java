package models;

import interfaces.DBConnector;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Conector JDBC a MySQL.
 */
public class SQLConnector implements DBConnector {
    /** URL JDBC (host, puerto y schema). */
    private final String URL = "jdbc:mysql://localhost:3306/hoteldb";
    /** Usuario de BD. */
    private final String USER = "root";
    /** Contraseña de BD. */
    private final String PASSWORD = "root";

    /**
     * Abre una conexión con la base de datos.
     * @return conexión o {@code null} si falla
     */
    @Override
    public Connection Connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException ex) {
            Utils.ShowErr("Error en la conexion: " + ex.getMessage(), "Error");
            return null;
        }
    }
}
