package models;

import java.sql.Date;

/**
 * Reserva de una habitación (cliente, habitación y fechas).
 */
public class Reservation {
    /** ID único de la reserva. */
    private int ID;
    /** Número de habitación. */
    private int roomNumber;
    /** ID del cliente. */
    private int idClient;
    /** Fecha de inicio (check-in). */
    private Date startDate;
    /** Fecha de fin (check-out). */
    private Date endDate;

    /**
     * Crea una reserva.
     * @param ID id de reserva
     * @param roomNumber número de habitación
     * @param idClient id de cliente
     * @param startDate fecha de inicio
     * @param endDate fecha de fin
     */
    public Reservation(int ID, int roomNumber, int idClient, Date startDate, Date endDate) {
        this.ID = ID;
        this.roomNumber = roomNumber;
        this.idClient = idClient;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /** @return id de reserva */
    public int getID() { return ID; }
    /** @param ID nuevo id */
    public void setID(int ID) { this.ID = ID; }

    /** @return número de habitación */
    public int getRoomNumber() { return roomNumber; }
    /** @param roomNumber nuevo número */
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    /** @return id de cliente */
    public int getIdClient() { return idClient; }
    /** @param idClient nuevo id de cliente */
    public void setIdClient(int idClient) { this.idClient = idClient; }

    /** @return fecha inicio */
    public Date getStartDate() { return startDate; }
    /** @param startDate nueva fecha inicio */
    public void setStartDate(Date startDate) { this.startDate = startDate; }

    /** @return fecha fin */
    public Date getEndDate() { return endDate; }
    /** @param endDate nueva fecha fin */
    public void setEndDate(Date endDate) { this.endDate = endDate; }
}
