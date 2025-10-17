package models;

/**
 * Cliente del sistema (ID, nombre completo y teléfono).
 */
public class Client {
    /** ID único del cliente. */
    private int ID;
    /** Nombre completo del cliente. */
    private String name;
    /** Teléfono del cliente. */
    private String phone;

    /**
     * Crea un cliente.
     * @param ID id único
     * @param name nombre completo
     * @param phone teléfono
     */
    public Client(int ID, String name, String phone) {
        this.ID = ID;
        this.name = name;
        this.phone = phone;
    }

    /** @return id del cliente */
    public int getID() { return ID; }
    /** @param ID nuevo id */
    public void setID(int ID) { this.ID = ID; }

    /** @return nombre completo */
    public String getName() { return name; }
    /** @param name nuevo nombre */
    public void setName(String name) { this.name = name; }

    /** @return teléfono */
    public String getPhone() { return phone; }
    /** @param phone nuevo teléfono */
    public void setPhone(String phone) { this.phone = phone; }
}
