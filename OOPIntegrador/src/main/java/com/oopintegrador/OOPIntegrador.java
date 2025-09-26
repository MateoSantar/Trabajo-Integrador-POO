/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.oopintegrador;

import views.MainView;
import java.sql.*;
import models.SQLConnector;

/**
 *
 * @author Mateo Santarsiero
 */
public class OOPIntegrador {

    public static void main(String[] args) {

        SQLConnector connector = new SQLConnector();
        Connection conn = connector.Connect();
        if (conn == null) {
            return;
        }
        MainView mv = new MainView(conn);
        mv.setVisible(true);
    }

}
