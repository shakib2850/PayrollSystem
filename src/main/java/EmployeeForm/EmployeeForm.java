package EmployeeForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

// Import DBConnection class
import DBConnection.DBConnection;

public class EmployeeForm extends JFrame {
    private JTextField nameField, phoneField, emailField, deptField, joinDateField;
    private JButton saveButton, updateButton, deleteButton, clearButton;
    private JTable employeeTable; // JTable for showing employee list

    public EmployeeForm() {
        setTitle("Employee Registration Form");
        setSize(600, 500); // Increased size to fit JTable
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // === Title ===
        JLabel title = new JLabel("Employee Registration Form");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(15));

        // === Input Fields ===
        addField("Name:", nameField = new JTextField());
        addField("Phone:", phoneField = new JTextField());
        addField("Email:", emailField = new JTextField());
        addField("Department:", deptField = new JTextField());
        addField("Join Date:", joinDateField = new JTextField());

        // === Buttons ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        saveButton = new JButton("Save");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");
        buttonPanel.add(saveButton); 
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton); 
        buttonPanel.add(clearButton);
        add(buttonPanel);

        // === JTable for Employee List ===
        employeeTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        scrollPane.setPreferredSize(new Dimension(550, 200));
        add(scrollPane);

        // === Load Data from Database ===
        loadEmployeeData();

        // === Button Actions ===
        saveButton.addActionListener(e -> saveEmployee());
        updateButton.addActionListener(e -> updateEmployee());
        deleteButton.addActionListener(e -> deleteEmployee());
        clearButton.addActionListener(e -> clearForm());

        // === Connection Test Query ===
        testConnection();

        setVisible(true);
        setLocationRelativeTo(null);
    }

    // Helper method to add label + field
    private void addField(String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);
        field.setMaximumSize(new Dimension(200, 25));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(field);
    }

    // === Load Employee Data into JTable ===
    private void loadEmployeeData() {
        Connection conn = DBConnection.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT EmployeeID, Name, Department, Salary, Status FROM Employees");

            DefaultTableModel model = new DefaultTableModel(
                new String[]{"ID", "Name", "Department", "Salary", "Status"}, 0
            );

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("EmployeeID"),
                    rs.getString("Name"),
                    rs.getString("Department"),
                    rs.getDouble("Salary"),
                    rs.getString("Status")
                });
            }

            employeeTable.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // === Save Employee ===
    private void saveEmployee() {
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "INSERT INTO Employees (Name, Phone, Email, Department, JoinDate) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nameField.getText());
            pst.setString(2, phoneField.getText());
            pst.setString(3, emailField.getText());
            pst.setString(4, deptField.getText());
            pst.setString(5, joinDateField.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee Saved Successfully!");
            loadEmployeeData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // === Update Employee ===
    private void updateEmployee() {
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "UPDATE Employees SET Phone=?, Email=?, Department=?, JoinDate=? WHERE Name=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, phoneField.getText());
            pst.setString(2, emailField.getText());
            pst.setString(3, deptField.getText());
            pst.setString(4, joinDateField.getText());
            pst.setString(5, nameField.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee Updated Successfully!");
            loadEmployeeData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // === Delete Employee ===
    private void deleteEmployee() {
        Connection conn = DBConnection.getConnection();
        try {
            String sql = "DELETE FROM Employees WHERE Name=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, nameField.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Employee Deleted Successfully!");
            loadEmployeeData();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // === Clear Form ===
    private void clearForm() {
        nameField.setText("");
        phoneField.setText("");
        emailField.setText("");
        deptField.setText("");
        joinDateField.setText("");
    }

    // === Connection Test Query ===
    private void testConnection() {
        Connection conn = DBConnection.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT TOP 1 * FROM Employees");
            if (rs.next()) {
                System.out.println("✅ Database connected, first employee: " + rs.getString("Name"));
            } else {
                System.out.println("✅ Database connected, but no employees found.");
            }
        } catch (Exception e) {
            System.out.println("❌ Connection test failed!");
            e.printStackTrace();
        }
    }

    // === Main Method for Testing ===
    public static void main(String[] args) {
        new EmployeeForm();
    }
}
