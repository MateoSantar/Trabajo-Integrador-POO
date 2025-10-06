package models;

import interfaces.Dao;
import java.sql.Connection;
import java.sql.Date;
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
public class ReservationDao implements Dao<Reservation> {

    private final List<Reservation> reservations = new ArrayList<>();

    private Connection conn;

    public ReservationDao(Connection conn) {
        try {
            this.conn = conn;
            String query = "SELECT * FROM reservas;";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("idReserva");
                int idClient = rs.getInt("idCliente");
                int roomNumber = rs.getInt("numeroHabitacion");
                Date fecha_inicio = rs.getDate("fecha_inicio");
                Date fecha_final = rs.getDate("fecha_fin");
                reservations.add(new Reservation(id, roomNumber, idClient, fecha_inicio, fecha_final));
            }
            ps.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error en la obtencion de reservas = " + ex.getMessage());
        }
    }

    @Override
    public Reservation Get(long id) {
        for (Reservation r : reservations) {
            if (r.getID() == id) {
                return r;
            }
        }
        System.out.println("No se encontro una reserva"); // <-- En la ventana de busqueda, debe mostrarse el error
        return null;
    }

    @Override
    public List<Reservation> getAll() {
        return reservations;
    }

    @Override
    public void save(Reservation r) {
        try {
            String query = "INSERT INTO reservas (idCliente,numeroHabitacion,fecha_inicio,fecha_fin) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, r.getIdClient());
            ps.setInt(2, r.getRoomNumber());
            ps.setDate(3, r.getStartDate());
            ps.setDate(4, r.getEndDate());

            int affectedRow = ps.executeUpdate();
            if (affectedRow > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                rs.next();
                r.setID(rs.getInt(1));
                reservations.add(r);
            } else {
                Utils.ShowErr("Error al guardar nueva reserva", "Error");
            }

            ps.close();
        } catch (SQLException ex) {
            Utils.ShowErr("Excepcion en guardado de reserva: " + ex.getMessage(), "Excepcion");
        }

    }

    @Override
    public boolean update(Reservation oldOne, Reservation newOne) {
        try {
            int index = reservations.indexOf(oldOne);
            if (index == -1) {
                return false;
            }

            String query = "UPDATE reservas SET idCliente = ?, numeroHabitacion = ?, fecha_inicio = ?, fecha_fin = ? WHERE idReserva = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, newOne.getIdClient());
            ps.setInt(2, newOne.getRoomNumber());
            ps.setDate(3, newOne.getStartDate());
            ps.setDate(4, newOne.getEndDate());
            ps.setInt(5, oldOne.getID());
            int affectedRows = ps.executeUpdate();
            if (affectedRows != 0) {
                reservations.set(index, newOne);
                return true;
            }
        } catch (SQLException ex) {
            Utils.ShowErr("Excepcion: " + ex.getMessage(), "Excepcion");
        }
        return false;
    }

    @Override
    public void delete(Reservation r) {
        try {
            reservations.remove(r);
            String query = "DELETE FROM reservas WHERE idReserva =?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, r.getID());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Utils.ShowErr("Excepcion: " + ex.getMessage(), "Excepcion");
        }
    }

}
