/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.oopintegrador;

import views.MainView;
import java.sql.*;
import models.SQLConnector;

/**
 * Clase principal del proyecto OOPIntegrador.
 * <p>Se encarga de iniciar la aplicación conectando a la base de datos
 * y mostrando la ventana principal.</p>
 * 
 * @author Mateo Santarsiero
 */
public class OOPIntegrador {

    /**
     * Punto de entrada de la aplicación.
     * <p>Establece la conexión con la base de datos y lanza la interfaz principal.
     * Si la conexión falla, el programa se detiene.</p>
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {

        // Crear el conector a la base de datos
        SQLConnector connector = new SQLConnector();

        // Intentar establecer conexión
        Connection conn = connector.Connect();

        // Si no hay conexión, terminar ejecución
        if (conn == null) {
            return;
        }

        // Crear y mostrar la ventana principal
        MainView mv = new MainView(conn);
        mv.setVisible(true);
    }
}
