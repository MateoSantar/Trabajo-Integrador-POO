package models;

/**
 * Habitación del hotel (categoría, precio y número).
 */
public class Room {
    /** ID interno en BD. */
    private int ID;
    /** Categoría (Single, Double, Suite, etc.). */
    private String category;
    /** Precio por noche. */
    private double price;
    /** Número visible de la habitación. */
    private int roomNumber;

    /**
     * Crea una habitación.
     * @param ID id interno
     * @param category categoría
     * @param price precio
     * @param roomNumber número de habitación
     */
    public Room(int ID, String category, double price, int roomNumber) {
        this.ID = ID;
        this.category = category;
        this.price = price;
        this.roomNumber = roomNumber;
    }

    /** @return número de habitación */
    public int getRoomNumber() { return roomNumber; }
    /** @param roomNumber nuevo número */
    public void setRoomNumber(int roomNumber) { this.roomNumber = roomNumber; }

    /** @return id interno */
    public int getID() { return ID; }
    /** @param ID nuevo id */
    public void setID(int ID) { this.ID = ID; }

    /** @return categoría */
    public String getCategory() { return category; }
    /** @param category nueva categoría */
    public void setCategory(String category) { this.category = category; }

    /** @return precio */
    public double getPrice() { return price; }
    /** @param price nuevo precio */
    public void setPrice(double price) { this.price = price; }

    /** @return representación legible */
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
