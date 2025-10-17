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
 * DAO de {@link Room}. Sincroniza caché y tabla habitaciones.
 */
public class RoomDao implements Dao<Room> {
    /** Lista en memoria de habitaciones. */
    private final List<Room> rooms = new ArrayList<>();
    /** Conexión JDBC. */
    private Connection conn;

    /**
     * Carga habitaciones desde BD.
     * @param conn conexión activa
     */
    public RoomDao(Connection conn) {
        try {
            this.conn = conn;
            String query = "SELECT * FROM habitaciones;";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idHabitacion");
                int roomNumber = rs.getInt("numeroHabitacion");
                String category = rs.getString("categoria");
                double price = rs.getDouble("precio");
                rooms.add(new Room(id, category, price, roomNumber));
            }
            ps.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la obtencion de habitaciones= " + ex.getMessage());
        }
    }

    /**
     * Busca por número de habitación.
     * @param id número de habitación
     * @return habitación o {@code null}
     */
    @Override
    public Room Get(long id) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == id) return r;
        }
        JOptionPane.showMessageDialog(null, "No se encontro una habitacion");
        return null;
    }

    /** @return todas las habitaciones en caché */
    @Override
    public List<Room> getAll() { return rooms; }

    /**
     * Inserta en BD y agrega al caché.
     * @param room habitación nueva
     */
    @Override
    public void save(Room room) {
        try {
            String query = "INSERT INTO habitaciones (numeroHabitacion,categoria,precio) VALUES (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, room.getRoomNumber());
            ps.setString(2, room.getCategory());
            ps.setDouble(3, room.getPrice());
            int affectedRow = ps.executeUpdate();
            if (affectedRow > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                room.setID(rs.getInt(1));
                rooms.add(room);
            } else {
                Utils.ShowErr("Error al guardar nueva habitacion", "Error");
            }
            ps.close();
        } catch (SQLException ex) {
            Utils.ShowErr("Excepcion: " + ex.getMessage(), "Excepcion");
        }
    }

    /**
     * Actualiza en BD y en caché.
     * @param oldOne habitación a actualizar
     * @param newOne nuevos datos
     * @return si se actualizó
     */
    @Override
    public boolean update(Room oldOne, Room newOne) {
        int index = rooms.indexOf(oldOne);
        if (index != -1) {
            try {
                String query = "UPDATE habitaciones SET numeroHabitacion=?,categoria=?,precio=? WHERE idHabitacion=?";
                PreparedStatement ps = conn.prepareStatement(query);
                ps.setInt(1, newOne.getRoomNumber());
                ps.setString(2, newOne.getCategory());
                ps.setDouble(3, newOne.getPrice());
                ps.setInt(4, oldOne.getID());
                ps.executeUpdate();
                rooms.set(index, newOne);
                return true;
            } catch (SQLException ex) {
                Utils.ShowErr("Excepcion: " + ex.getMessage(), "Excepcion");
            }
        }
        return false;
    }

    /**
     * Elimina en BD y del caché.
     * @param room habitación a eliminar
     */
    @Override
    public void delete(Room room) {
        try {
            rooms.remove(room);
            String query = "DELETE FROM habitaciones WHERE idHabitacion =?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, room.getID());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Utils.ShowErr("Excepcion: " + ex.getMessage(), "Excepcion");
        }
    }
}
