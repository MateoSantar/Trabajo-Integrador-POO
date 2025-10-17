package models;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 * Utilidades para Swing y tablas.
 */
public class Utils {

    /**
     * Muestra un diálogo de error.
     * @param err mensaje de error
     * @param title título del diálogo
     */
    public static void ShowErr(String err, String title) {
        JOptionPane.showMessageDialog(null, err, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Muestra un diálogo informativo.
     * @param info mensaje de información
     */
    public static void ShowInfo(String info) {
        JOptionPane.showMessageDialog(null, info, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Clona un {@link DefaultTableModel}.
     * @param orgModel modelo original
     * @return nuevo modelo con mismas columnas y filas
     */
    public static DefaultTableModel cloneModel(DefaultTableModel orgModel) {
        int columnCount = orgModel.getColumnCount();
        Object[] columnNames = new Object[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = orgModel.getColumnName(i);
        }

        DefaultTableModel newModel = new DefaultTableModel(columnNames, 0);
        int rowCount = orgModel.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            Object[] rowData = new Object[columnCount];
            for (int j = 0; j < columnCount; j++) {
                rowData[j] = orgModel.getValueAt(i, j);
            }
            newModel.addRow(rowData);
        }
        return newModel;
    }

    /**
     * Centra una ventana en la pantalla.
     * @param window ventana a centrar
     */
    public static void centerWindow(JFrame window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }
}
