package models;

import java.sql.Date;

/**
 *
 * @author Mateo Santarsiero 
 */
public class Reservation {
    private int ID,roomNumber,idClient;
    private Date startDate,endDate;
    
    

    public Reservation(int ID, int roomNumber, int idClient, Date startDate, Date endDate) {
        this.ID = ID;
        this.roomNumber = roomNumber;
        this.idClient = idClient;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    
    
    
    
    

    
    
    
    
}
