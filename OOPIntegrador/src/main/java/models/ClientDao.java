
package models;

import interfaces.Dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Mateo Santarsiero
 */
public class ClientDao implements Dao<Client> {
    private final List<Client> clients = new ArrayList<>();
    private Connection conn;
    public ClientDao(Connection conn) {
        try {
            this.conn = conn;
            String query = "SELECT * FROM clientes;";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("idCliente");
                String name = rs.getString("nombre");
                String surName = rs.getString("apellido");
                String phone = rs.getString("telefono");
                clients.add(new Client(id,(name+" "+surName),phone));
            }
            ps.close();
            
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la obtencion de clientes = "+ex.getMessage());
        }
        
    }

    @Override
    public List<Client> getAll() {
        return clients;
    }
    
    @Override
    public Client Get(long id) {
        for(Client c : clients){
            if (c.getID() == id) {
                return c;
            }
        }
         // <-- En la ventana de busqueda, debe mostrarse el error
        return null;
    }

    

    @Override
    public void save(Client c) {
        try{
            String query = "INSERT INTO clientes (nombre,apellido,telefono) VALUES (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS);
            String[] name = c.getName().split(" ");
            ps.setString(1, name[0]);
            ps.setString(2, name[1]);
            ps.setString(3, c.getPhone());
            int affectedRow = ps.executeUpdate();
            if (affectedRow > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                c.setID(rs.getInt(1));
                clients.add(c);
            } else {
                Utils.ShowErr("Error al guardar nuevo cliente", "Error en guardado");
            }
            
            ps.close();
        } catch (SQLException ex){
            Utils.ShowErr("Excepcion: "+ex.getMessage(), "Excepcion");
        }
    }

    @Override
    public boolean update(Client oldClient, Client newClient) {
        int index = clients.indexOf(oldClient);
        if (index != -1) {
            clients.set(index, newClient);
            return true;
        }
        Utils.ShowErr("Error en el update del cliente", "Error"); // <== En ningun momento se deberia utilizar el update de Client, pero ahi esta
        return false;
    }

    @Override
    public void delete(Client c) {
        clients.remove(c);
    }
    
    
    
}
