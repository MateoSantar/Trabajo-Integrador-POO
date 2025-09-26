
package models;

/**
 *
 * @author Mateo Santarsiero 
 */
public class Client {
    private int ID;
    private String name, phone;

    public Client(int ID, String name, String phone) {
        this.ID = ID;
        this.name = name;
        this.phone = phone;
    }

    
    
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    
}
