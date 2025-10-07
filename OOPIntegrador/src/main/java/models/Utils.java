package models;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Mateo Santarsiero
 */
public class Utils {

    public static void ShowErr(String err, String title) {
        JOptionPane.showMessageDialog(null, err, title, JOptionPane.ERROR_MESSAGE);
    }

    public static void ShowInfo(String info) {
        JOptionPane.showMessageDialog(null, info, "Info", JOptionPane.INFORMATION_MESSAGE);
    }

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
    public static void centerWindow(JFrame window) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - window.getWidth()) / 2;
        int y = (screenSize.height - window.getHeight()) / 2;
        window.setLocation(x, y);
    }
}
