
package models;

import interfaces.Dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        System.out.println("No se encontro un cliente"); // <-- En la ventana de busqueda, debe mostrarse el error
        return null;
    }

    

    @Override
    public void save(Client c) {
        try{
            String query = "INSERT INTO clientes (nombre,apellido,telefono) VALUES (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(query);
            String[] name = c.getName().split(" ");
            ps.setString(1, name[0]);
            ps.setString(2, name[1]);
            ps.setString(3, c.getPhone());
            ResultSet rs = ps.getGeneratedKeys();
            c.setID(rs.getInt(1));
            clients.add(c);
            ps.close();
        } catch (SQLException ex){
            Logger.getLogger(ReservationDao.class.getName()).log(Level.SEVERE, null, ex);

        }
    }

    @Override
    public boolean update(Client oldClient, Client newClient) {
        int index = clients.indexOf(oldClient);
        if (index != -1) {
            clients.set(index, newClient);
            return true;
        }
        return false;
    }

    @Override
    public void delete(Client c) {
        clients.remove(c);
    }
    
}
