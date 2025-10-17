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
 * DAO de {@link Client}. Maneja caché y persistencia.
 */
public class ClientDao implements Dao<Client> {
    /** Lista en memoria de clientes. */
    private final List<Client> clients = new ArrayList<>();
    /** Conexión JDBC. */
    private Connection conn;

    /**
     * Carga clientes desde la BD.
     * @param conn conexión activa
     */
    public ClientDao(Connection conn) {
        try {
            this.conn = conn;
            String query = "SELECT * FROM clientes;";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idCliente");
                String name = rs.getString("nombre");
                String surName = rs.getString("apellido");
                String phone = rs.getString("telefono");
                clients.add(new Client(id, (name + " " + surName), phone));
            }
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la obtencion de clientes = " + ex.getMessage());
        }
    }

    /** @return todos los clientes en caché */
    @Override
    public List<Client> getAll() { return clients; }

    /**
     * Busca por id.
     * @param id id del cliente
     * @return cliente o {@code null}
     */
    @Override
    public Client Get(long id) {
        for (Client c : clients) {
            if (c.getID() == id) return c;
        }
        return null;
    }

    /**
     * Guarda en BD y agrega al caché.
     * @param c cliente nuevo
     */
    @Override
    public void save(Client c) {
        try {
            String query = "INSERT INTO clientes (nombre,apellido,telefono) VALUES (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            String[] name = c.getName().split(" ");
            ps.setString(1, name[0]);
            ps.setString(2, name.length > 1 ? name[1] : "");
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
        } catch (SQLException ex) {
            Utils.ShowErr("Excepcion: " + ex.getMessage(), "Excepcion");
        }
    }

    /**
     * Reemplaza en caché (no actualiza BD).
     * @param oldClient a reemplazar
     * @param newClient nuevo cliente
     * @return si se reemplazó
     */
    @Override
    public boolean update(Client oldClient, Client newClient) {
        int index = clients.indexOf(oldClient);
        if (index != -1) {
            clients.set(index, newClient);
            return true;
        }
        Utils.ShowErr("Error en el update del cliente", "Error");
        return false;
    }

    /**
     * Elimina del caché (no borra en BD).
     * @param c cliente a eliminar
     */
    @Override
    public void delete(Client c) {
        clients.remove(c);
    }
}
