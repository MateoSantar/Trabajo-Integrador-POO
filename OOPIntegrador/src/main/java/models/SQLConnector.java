package models;
import interfaces.DBConnector;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;
/**
 *
 * @author Mateo Santarsiero 
 */
public class SQLConnector implements DBConnector {
    private final String URL = "jdbc:mysql://localhost:3306/hoteldb";
    private final String USER = "root";
    private final String PASSWORD = "root";
    
    @Override
    public Connection Connect() {
        Connection conn;
        try{
            conn = DriverManager.getConnection(URL,USER,PASSWORD);
            return conn;
        }catch(SQLException ex){
            Utils.ShowErr("Error en la conexion: "+ex.getMessage(), "Error");
            return null;
        }
        
    }
    
}
