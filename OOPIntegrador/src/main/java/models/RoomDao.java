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

public class RoomDao implements Dao<Room> {

    private final List<Room> rooms = new ArrayList<>();
    private Connection conn;

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

    @Override
    public Room Get(long id) {
        for (Room r : rooms) {
            if (r.getRoomNumber() == id) {
                return r;
            }
        }
        JOptionPane.showMessageDialog(null, "No se encontro una habitacion");
        return null;
    }

    @Override
    public List<Room> getAll() {
        return rooms;
    }

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
                JOptionPane.showMessageDialog(null, "Error al guardar nueva habitacion", "Error", JOptionPane.ERROR_MESSAGE);
            }

            ps.close();
        } catch (SQLException ex) {
            Logger.getLogger(RoomDao.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public boolean update(Room oldOne, Room newOne) {
        StringBuilder bs = new StringBuilder();
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
                Logger.getLogger(RoomDao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @Override
    public void delete(Room room) {
        rooms.remove(room);
    }

    public void closeConnection() {
        try {
            this.conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(RoomDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
