package models;

/**
 *
 * @author Mateo Santarsiero <MateoSantar>
 */
public class Room {
    private int ID;
    private String category;
    private double price;
    private int roomNumber;

    public Room(int ID, String category, double price, int roomNumber) {
        this.ID = ID;
        this.category = category;
        this.price = price;
        this.roomNumber = roomNumber;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
    
    
    

    
    
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Room{");
        sb.append("ID=").append(ID);
        sb.append(", category=").append(category);
        sb.append(", price=").append(price);
        sb.append(", roomNumber=").append(roomNumber);
        sb.append('}');
        return sb.toString();
    }
    
    
    
}
