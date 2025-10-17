package views;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import models.Client;
import models.ClientDao;
import models.Reservation;
import models.ReservationDao;
import models.Room;
import models.RoomDao;
import models.Utils;

/**
 * Ventana para añadir o modificar reservas en el sistema. Permite seleccionar
 * cliente, habitación, fechas de inicio y fin, así como gestionar las
 * habitaciones disponibles.
 *
 * <p>
 * Esta clase puede funcionar tanto en modo de inserción (nueva reserva) como en
 * modo de edición (modificar una reserva existente).
 * </p>
 *
 *
 *
 *
 * @author Mateo Santarsiero
 */
public class AddReservationView extends javax.swing.JFrame {

    /**
     * Indica si la vista se utiliza para insertar una nueva reserva o editar
     * una existente.
     */
    private boolean isInsert = false;
    /**
     * Tabla principal de reservas que se actualizará tras una inserción o
     * edición.
     */
    private final JTable table;
    /**
     * Vista principal desde la cual se abre esta ventana.
     */
    private final MainView mainView;
    /**
     * Mapa que asocia categorías de habitaciones con sus números disponibles.
     */
    private final HashMap<String, ArrayList<Integer>> roomNumbers = new HashMap<>();
    /**
     * DAO para acceso a los datos de habitaciones.
     */
    private final RoomDao roomDao;
    /**
     * DAO para acceso a los datos de reservas.
     */
    private final ReservationDao reservDao;
    /**
     * Habitación actualmente seleccionada (en modo edición).
     */
    private Room actualRoom;
    /**
     * Categoría de la habitación actualmente seleccionada (en modo edición).
     */
    private String actualCategory;
    /**
     * DAO para acceso a los datos de clientes.
     */
    private final ClientDao clientDao;

    /**
     * Crea una nueva instancia de AddReservationView para insertar una reserva.
     *
     * @param isInsert indica si la vista es para inserción
     * @param table tabla de reservas que se actualizará
     * @param mainView referencia a la vista principal
     * @param dao DAO de habitaciones
     * @param reservDao DAO de reservas
     * @param clientDao DAO de clientes
     */
    public AddReservationView(boolean isInsert, JTable table, MainView mainView, RoomDao dao, ReservationDao reservDao, ClientDao clientDao) {
        initComponents();
        this.isInsert = isInsert;
        this.mainView = mainView;
        this.roomDao = dao;
        this.reservDao = reservDao;
        this.clientDao = clientDao;
        this.table = table;
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowClosingEvent();
        assignRoomNumbersMap();
        setLocation(700, 450);

        if (roomNumbers.get("Standard") == null) {
            roomNumberSelBox.setEnabled(false);
        }
    }

    /**
     * Crea una nueva instancia de AddReservationView para editar una reserva
     * existente.
     *
     * @param isInsert indica si la vista es para inserción o edición
     * @param model tabla asociada a las reservas
     * @param data datos de la reserva seleccionada
     * @param mainView vista principal
     * @param roomDao DAO de habitaciones
     * @param reservDao DAO de reservas
     * @param clientDao DAO de clientes
     */
    public AddReservationView(boolean isInsert, JTable model, Object[] data, MainView mainView, RoomDao roomDao, ReservationDao reservDao, ClientDao clientDao) {
        initComponents();
        this.isInsert = isInsert;
        this.table = model;
        this.mainView = mainView;
        this.roomDao = roomDao;
        this.reservDao = reservDao;
        this.clientDao = clientDao;
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowClosingEvent();
        assignRoomNumbersMap();
        completeSelectors(data);
        Utils.centerWindow(this);
        if (roomNumbers.get("Standard") == null) {
            roomNumberSelBox.setEnabled(false);
        }
    }

    /**
     * Completa los campos y selectores de la interfaz con los datos de la
     * reserva seleccionada.
     *
     * @param data arreglo de objetos con los valores de la reserva
     */
    private void completeSelectors(Object[] data) {
        Map<String, String> map = new HashMap<>();
        map.put("id", (String) data[0]);
        map.put("client", (String) data[1]);
        map.put("category", (String) data[2]);
        map.put("roomNumber", (String) data[3]);
        map.put("price", (String) data[4]);
        map.put("date", (String) data[5]);
        map.put("endDate", (String) data[6]);
        NewClientTxtField.setText(map.get("client"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            reservDateChooser.setDate(sdf.parse(map.get("date")));
            endDateChooser.setDate(sdf.parse(map.get("endDate")));
        } catch (ParseException ex) {
            Utils.ShowErr("Excepcion: " + ex.getMessage(), "Excepcion");
            return;
        }
        roomCategorySelBox.setSelectedItem(map.get("category"));

    }

    /**
     * Asigna el mapa de números de habitación disponibles por categoría,
     * excluyendo las habitaciones actualmente reservadas.
     */
    private void assignRoomNumbersMap() {
        roomNumberSelBox.removeAllItems();
        ArrayList<Room> rooms = (ArrayList<Room>) roomDao.getAll();
        ArrayList<Reservation> reservations = (ArrayList<Reservation>) reservDao.getAll();
        ArrayList<Integer> roomsOcupied = new ArrayList<>();
        ArrayList<Room> roomsDesocupied = new ArrayList<>();
        roomNumbers.clear();
        for (Reservation r : reservations) {
            roomsOcupied.add(r.getRoomNumber());
        }
        for (Room r : rooms) {
            if (!roomsOcupied.contains(r.getRoomNumber())) {
                roomsDesocupied.add(r);
            }
        }
        for (Room r : roomsDesocupied) {
            if (roomNumbers.containsKey(r.getCategory())) {
                roomNumbers.get(r.getCategory()).add(r.getRoomNumber());
            } else {
                roomNumbers.put(r.getCategory(), new ArrayList<>());
                roomNumbers.get(r.getCategory()).add(r.getRoomNumber());
            }
        }
        ArrayList<Integer> availableRooms = roomNumbers.get(roomCategorySelBox.getSelectedItem().toString());
        if (isInsert && availableRooms != null) {
            for (Integer room : availableRooms) {
                roomNumberSelBox.addItem(String.valueOf(room));
            }
            roomNumberSelBox.setSelectedIndex(0);
        }

    }

    /**
     * Establece el nombre del cliente en el campo de texto correspondiente.
     *
     * @param nombre nombre del cliente
     */
    public void setClientName(String nombre) {
        this.NewClientTxtField.setText(nombre);
    }

    /**
     * Agrega un listener para manejar el evento de cierre de ventana. Restaura
     * la visibilidad y habilitación de la vista principal.
     */
    private void addWindowClosingEvent() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                mainView.setEnabled(true);
                dispose();

            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        roomCategorySelBox = new javax.swing.JComboBox<>();
        NewClientTxtField = new java.awt.TextField();
        AddUserBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        roomNumberSelBox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        reservDateChooser = new com.toedter.calendar.JDateChooser();
        ReserveBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        endDateChooser = new com.toedter.calendar.JDateChooser();
        adminRoomsBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Añadido de Reserva");
        setResizable(false);

        roomCategorySelBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Standard", "Superior", "Suite" }));
        roomCategorySelBox.setActionCommand("selectRoom");
        roomCategorySelBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomCategorySelBoxActionPerformed(evt);
            }
        });

        AddUserBtn.setText("Añadir Nuevo Cliente");
        AddUserBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddUserBtnActionPerformed(evt);
            }
        });

        jLabel1.setText("Nombre:");

        jLabel2.setText("Categoria:");

        roomNumberSelBox.setActionCommand("selectNumber");

        jLabel3.setText("Numero:");

        ReserveBtn.setText("Reservar");
        ReserveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ReserveBtnActionPerformed(evt);
            }
        });

        jLabel4.setText("Inicio:");

        jLabel5.setText("Final:");

        adminRoomsBtn.setText("Administrar Habitaciones");
        adminRoomsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminRoomsBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(roomCategorySelBox, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(roomNumberSelBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ReserveBtn)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGap(18, 18, 18)))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(NewClientTxtField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(reservDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(adminRoomsBtn)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(endDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 10, Short.MAX_VALUE)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(AddUserBtn))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1)
                            .addComponent(NewClientTxtField, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(AddUserBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(reservDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(endDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(roomCategorySelBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(adminRoomsBtn))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(roomNumberSelBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addComponent(ReserveBtn)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Evento al cambiar la categoría de habitación seleccionada. Actualiza las
     * habitaciones disponibles para la nueva categoría.
     *
     * @param evt evento de selección
     */
    private void roomCategorySelBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomCategorySelBoxActionPerformed
        roomNumberSelBox.removeAllItems();
        if (!isInsert) {
            if (actualRoom == null) {
                actualRoom = roomDao.Get(Long.parseLong(table.getModel().getValueAt(table.getSelectedRow(), 3).toString()));
                actualCategory = actualRoom.getCategory();
            }
            if (roomCategorySelBox.getSelectedItem().equals(actualCategory)) {
                roomNumberSelBox.addItem(String.valueOf(actualRoom.getRoomNumber()));
            }

        }
        ArrayList<Integer> roomsAvailable = roomNumbers.get(roomCategorySelBox.getSelectedItem().toString());
        if (roomsAvailable != null) {
            roomNumberSelBox.setEnabled(true);
            for (Integer roomAvailable : roomsAvailable) {
                roomNumberSelBox.addItem(String.valueOf(roomAvailable));
            }
        } else {
            roomNumberSelBox.setEnabled(false);
        }

    }//GEN-LAST:event_roomCategorySelBoxActionPerformed

    /**
     * Evento del botón "Añadir Nuevo Cliente". Abre una nueva ventana para
     * registrar un cliente y desactiva la actual.
     *
     * @param evt evento de clic
     */
    private void AddUserBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddUserBtnActionPerformed
        AddClientView adv = new AddClientView(clientDao, this, (DefaultTableModel) table.getModel());
        adv.setVisible(true);
        this.setEnabled(false);
    }//GEN-LAST:event_AddUserBtnActionPerformed

    /**
     * Obtiene el ID del cliente a partir de su nombre.
     *
     * @return el ID del cliente, o -1 si no se encuentra
     */
    private int getIdByName() {
        for (Client c : clientDao.getAll()) {
            if (c.getName().equalsIgnoreCase(NewClientTxtField.getText())) {
                return c.getID();
            }
        }
        return -1;
    }

        /**
     * Evento del botón "Reservar".
     * <p>
     * Este método valida los datos ingresados antes de registrar o actualizar
     * una reserva en el sistema. Se asegura de que:
     * </p>
     * <ul>
     *   <li>Todos los campos obligatorios estén completos.</li>
     *   <li>La fecha de finalización no sea anterior ni igual a la fecha de inicio.</li>
     *   <li>El cliente exista en la base de datos.</li>
     *   <li>La habitación seleccionada esté disponible durante el rango de fechas indicado (sin solapamientos con otras reservas).</li>
     * </ul>
     * <p>
     * Si todas las validaciones son correctas, inserta una nueva reserva o actualiza
     * una existente según el modo actual de la vista (<code>isInsert</code>).
     * Luego, recarga la tabla principal de reservas y cierra la ventana.
     * </p>
     *
     * @param evt evento de clic del botón "Reservar"
     */

    private void ReserveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ReserveBtnActionPerformed
        if (NewClientTxtField == null || reservDateChooser.getDate() == null || endDateChooser.getDate() == null || roomNumberSelBox.getSelectedItem() == null || roomNumberSelBox.getSelectedItem() == null) {
            Utils.ShowInfo("Complete los datos");
            return;
        }

        Date startDate = reservDateChooser.getDate();
        Date endDate = endDateChooser.getDate();
        
        if (endDate.before(startDate)) {
            Utils.ShowInfo("La fecha final no puede ser anterior a la fecha inicial.");
            return;
        }
        if (endDate.equals(startDate)) {
            Utils.ShowInfo("La reserva debe tener al menos un día de duración");
            return;
        }
        int clientId = getIdByName();
        if (clientId == -1) {
            Utils.ShowInfo("No existe el cliente");
            return;
        }

        if (!isInsert) {
            int idSelected = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
            for (Reservation r : reservDao.getAll()) {
                if (r.getID() == idSelected) {
                    reservDao.update(r, new Reservation(r.getID(), Integer.parseInt(roomNumberSelBox.getSelectedItem().toString()), clientId, new java.sql.Date(reservDateChooser.getDate().getTime()), new java.sql.Date(endDateChooser.getDate().getTime())));
                    break;
                }
            }
            mainView.reloadReservations();
            mainView.setEnabled(true);
            this.dispose();
            return;
        }

        reservDao.save(new Reservation(-1, Integer.parseInt(roomNumberSelBox.getSelectedItem().toString()), clientId, new java.sql.Date(reservDateChooser.getDate().getTime()), new java.sql.Date(endDateChooser.getDate().getTime())));
        mainView.reloadReservations();
        mainView.setEnabled(true);
        this.dispose();

    }//GEN-LAST:event_ReserveBtnActionPerformed

    /**
     * Evento del botón "Administrar Habitaciones". Abre la ventana de
     * administración de habitaciones.
     *
     * @param evt evento de clic
     */
    private void adminRoomsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminRoomsBtnActionPerformed
        AdminRoomsView adminRoomView = new AdminRoomsView(reservDao, roomDao, (DefaultTableModel) table.getModel(), this, clientDao, roomNumbers);
        this.setEnabled(false);
        adminRoomView.setVisible(true);
    }//GEN-LAST:event_adminRoomsBtnActionPerformed

    /**
     * Reinicializa el mapa de números de habitación disponibles.
     */
    public void resetRoomNumberHashMap() {
        this.assignRoomNumbersMap();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddUserBtn;
    private java.awt.TextField NewClientTxtField;
    private javax.swing.JButton ReserveBtn;
    private javax.swing.JButton adminRoomsBtn;
    private com.toedter.calendar.JDateChooser endDateChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private com.toedter.calendar.JDateChooser reservDateChooser;
    private javax.swing.JComboBox<String> roomCategorySelBox;
    private javax.swing.JComboBox<String> roomNumberSelBox;
    // End of variables declaration//GEN-END:variables
}
