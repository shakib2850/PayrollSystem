import DBConnection.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class DepartmentForm extends JFrame {
    private JTextField deptNameField;
    private JButton saveButton, updateButton, deleteButton;
    private JTable deptTable;
    private JScrollPane scrollPane;

    public DepartmentForm() {
        setTitle("Department Form");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Department Form");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        addField("Department Name:", deptNameField = new JTextField());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        saveButton = new JButton("Save");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        buttonPanel.add(saveButton); buttonPanel.add(updateButton); buttonPanel.add(deleteButton);
        add(buttonPanel);

        // JTable
        deptTable = new JTable();
        scrollPane = new JScrollPane(deptTable);
        scrollPane.setPreferredSize(new Dimension(450, 150));
        add(scrollPane);

        // Button Actions
        saveButton.addActionListener(e -> {
            saveDepartment();
            loadDepartments();
        });
        updateButton.addActionListener(e -> {
            updateDepartment();
            loadDepartments();
        });
        deleteButton.addActionListener(e -> {
            deleteDepartment();
            loadDepartments();
        });

        // Row select → auto-fill
        deptTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = deptTable.getSelectedRow();
                deptNameField.setText(deptTable.getValueAt(row, 0).toString());
            }
        });

        setVisible(true);
        setLocationRelativeTo(null);

        // Load data initially
        loadDepartments();
    }

    private void addField(String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);
        field.setMaximumSize(new Dimension(200, 25));
        field.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(field);
    }

    // ✅ Save Department Record
    private void saveDepartment() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Departments (DepartmentName) VALUES (?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, deptNameField.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Department Saved Successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Update Department Record
    private void updateDepartment() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Departments SET DepartmentName=? WHERE DepartmentName=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, deptNameField.getText());
            pst.setString(2, deptTable.getValueAt(deptTable.getSelectedRow(), 0).toString());
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Department Updated Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to update!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Delete Department Record
    private void deleteDepartment() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Departments WHERE DepartmentName=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, deptNameField.getText());
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Department Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to delete!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Load Departments Data into JTable
    private void loadDepartments() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT DepartmentName FROM Departments";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Department Name");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getString("DepartmentName")
                });
            }

            deptTable.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Main Method (Test Run)
    public static void main(String[] args) {
        new DepartmentForm();
    }
}
