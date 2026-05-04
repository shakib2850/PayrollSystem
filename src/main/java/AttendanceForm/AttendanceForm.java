import DBConnection.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class AttendanceForm extends JFrame {
    private JTextField empIdField, dateField;
    private JComboBox<String> statusBox;
    private JButton saveButton, updateButton, deleteButton;
    private JTable attendanceTable;
    private JScrollPane scrollPane;

    public AttendanceForm() {
        setTitle("Attendance Form");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Title
        JLabel title = new JLabel("Attendance Form");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(15));

        // Employee ID
        JLabel empLabel = new JLabel("Employee ID:");
        empLabel.setFont(new Font("Arial", Font.BOLD, 16));
        empLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(empLabel);

        empIdField = new JTextField();
        empIdField.setMaximumSize(new Dimension(200, 25));
        empIdField.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(empIdField);

        // Date
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD):");
        dateLabel.setFont(new Font("Arial", Font.BOLD, 16));
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(dateLabel);

        dateField = new JTextField();
        dateField.setMaximumSize(new Dimension(200, 25));
        dateField.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(dateField);

        // Status
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(statusLabel);

        statusBox = new JComboBox<>(new String[]{"Present", "Absent"});
        statusBox.setMaximumSize(new Dimension(200, 25));
        statusBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(statusBox);

        // Buttons row
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        saveButton = new JButton("Save");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        buttonPanel.add(saveButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        add(Box.createVerticalStrut(15));
        add(buttonPanel);

        // JTable
        attendanceTable = new JTable();
        scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setPreferredSize(new Dimension(450, 150));
        add(scrollPane);

        // Button Actions
        saveButton.addActionListener(e -> {
            saveAttendance();
            loadAttendance();
        });
        updateButton.addActionListener(e -> {
            updateAttendance();
            loadAttendance();
        });
        deleteButton.addActionListener(e -> {
            deleteAttendance();
            loadAttendance();
        });

        // Row select → auto-fill
        attendanceTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = attendanceTable.getSelectedRow();
                empIdField.setText(attendanceTable.getValueAt(row, 0).toString());
                dateField.setText(attendanceTable.getValueAt(row, 1).toString());
                statusBox.setSelectedItem(attendanceTable.getValueAt(row, 2).toString());
            }
        });

        setVisible(true);
        setLocationRelativeTo(null);

        // Load data initially
        loadAttendance();
    }

    // ✅ Save Attendance Record
    private void saveAttendance() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Attendance (EmployeeID, Date, Status) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(empIdField.getText()));
            pst.setString(2, dateField.getText());
            pst.setString(3, statusBox.getSelectedItem().toString());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Attendance Saved Successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Update Attendance Record
    private void updateAttendance() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Attendance SET Status=? WHERE EmployeeID=? AND Date=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, statusBox.getSelectedItem().toString());
            pst.setInt(2, Integer.parseInt(empIdField.getText()));
            pst.setString(3, dateField.getText());
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Attendance Updated Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to update!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Delete Attendance Record
    private void deleteAttendance() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Attendance WHERE EmployeeID=? AND Date=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(empIdField.getText()));
            pst.setString(2, dateField.getText());
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Attendance Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to delete!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Load Attendance Data into JTable
    private void loadAttendance() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT EmployeeID, Date, Status FROM Attendance";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Employee ID");
            model.addColumn("Date");
            model.addColumn("Status");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("EmployeeID"),
                    rs.getString("Date"),
                    rs.getString("Status")
                });
            }

            attendanceTable.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Main Method (Test Run)
    public static void main(String[] args) {
        new AttendanceForm();
    }
}
