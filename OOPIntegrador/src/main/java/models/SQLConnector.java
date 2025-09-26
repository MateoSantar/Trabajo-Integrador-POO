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
            JOptionPane.showMessageDialog(null, "Conexion establecida como "+USER, "Conexion exitosa", JOptionPane.INFORMATION_MESSAGE);
            return conn;
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"Ocurrio un error en la conexion","Error",JOptionPane.ERROR_MESSAGE);
            return null;
        }
        
    }
    
}
