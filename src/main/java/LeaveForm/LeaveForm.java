import DBConnection.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class LeaveForm extends JFrame {
    private JTextField empIdField, leaveTypeField, startDateField, endDateField;
    private JButton saveButton, updateButton, deleteButton;
    private JTable leaveTable;
    private JScrollPane scrollPane;

    public LeaveForm() {
        setTitle("Leave Form");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Leave Form");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        addField("Employee ID:", empIdField = new JTextField());
        addField("Leave Type:", leaveTypeField = new JTextField());
        addField("Start Date (YYYY-MM-DD):", startDateField = new JTextField());
        addField("End Date (YYYY-MM-DD):", endDateField = new JTextField());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        saveButton = new JButton("Save");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        buttonPanel.add(saveButton); buttonPanel.add(updateButton); buttonPanel.add(deleteButton);
        add(buttonPanel);

        // JTable
        leaveTable = new JTable();
        scrollPane = new JScrollPane(leaveTable);
        scrollPane.setPreferredSize(new Dimension(550, 150));
        add(scrollPane);

        // Button Actions
        saveButton.addActionListener(e -> {
            saveLeave();
            loadLeaves();
        });
        updateButton.addActionListener(e -> {
            updateLeave();
            loadLeaves();
        });
        deleteButton.addActionListener(e -> {
            deleteLeave();
            loadLeaves();
        });

        // Row select → auto-fill
        leaveTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = leaveTable.getSelectedRow();
                empIdField.setText(leaveTable.getValueAt(row, 0).toString());
                leaveTypeField.setText(leaveTable.getValueAt(row, 1).toString());
                startDateField.setText(leaveTable.getValueAt(row, 2).toString());
                endDateField.setText(leaveTable.getValueAt(row, 3).toString());
            }
        });

        setVisible(true);
        setLocationRelativeTo(null);

        // Load data initially
        loadLeaves();
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

    // ✅ Save Leave Record
    private void saveLeave() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO LeaveRequests (EmployeeID, LeaveType, StartDate, EndDate) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(empIdField.getText()));
            pst.setString(2, leaveTypeField.getText());
            pst.setString(3, startDateField.getText());
            pst.setString(4, endDateField.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Leave Saved Successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Update Leave Record
    private void updateLeave() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE LeaveRequests SET LeaveType=?, StartDate=?, EndDate=? WHERE EmployeeID=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, leaveTypeField.getText());
            pst.setString(2, startDateField.getText());
            pst.setString(3, endDateField.getText());
            pst.setInt(4, Integer.parseInt(empIdField.getText()));
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Leave Updated Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to update!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Delete Leave Record
    private void deleteLeave() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM LeaveRequests WHERE EmployeeID=? AND LeaveType=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(empIdField.getText()));
            pst.setString(2, leaveTypeField.getText());
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Leave Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to delete!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Load Leave Data into JTable
    private void loadLeaves() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT EmployeeID, LeaveType, StartDate, EndDate FROM LeaveRequests";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Employee ID");
            model.addColumn("Leave Type");
            model.addColumn("Start Date");
            model.addColumn("End Date");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("EmployeeID"),
                    rs.getString("LeaveType"),
                    rs.getString("StartDate"),
                    rs.getString("EndDate")
                });
            }

            leaveTable.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Main Method (Test Run)
    public static void main(String[] args) {
        new LeaveForm();
    }
}
