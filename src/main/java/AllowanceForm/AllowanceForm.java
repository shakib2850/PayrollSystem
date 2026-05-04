import DBConnection.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class AllowanceForm extends JFrame {
    private JTextField allowanceNameField, descriptionField;
    private JComboBox<String> percentageBox;
    private JButton saveButton, updateButton, deleteButton;
    private JTable allowanceTable;
    private JScrollPane scrollPane;

    public AllowanceForm() {
        setTitle("Allowance Form");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Allowance Form");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        addField("Allowance Name:", allowanceNameField = new JTextField());

        JLabel percentLabel = new JLabel("Is Percentage:");
        percentLabel.setFont(new Font("Arial", Font.BOLD, 16));
        percentLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(percentLabel);
        percentageBox = new JComboBox<>(new String[]{"Yes", "No"});
        percentageBox.setMaximumSize(new Dimension(200, 25));
        percentageBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(percentageBox);

        addField("Description:", descriptionField = new JTextField());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        saveButton = new JButton("Save");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        buttonPanel.add(saveButton); buttonPanel.add(updateButton); buttonPanel.add(deleteButton);
        add(buttonPanel);

        // JTable
        allowanceTable = new JTable();
        scrollPane = new JScrollPane(allowanceTable);
        scrollPane.setPreferredSize(new Dimension(450, 150));
        add(scrollPane);

        // Button Actions
        saveButton.addActionListener(e -> {
            saveAllowance();
            loadAllowances();
        });
        updateButton.addActionListener(e -> {
            updateAllowance();
            loadAllowances();
        });
        deleteButton.addActionListener(e -> {
            deleteAllowance();
            loadAllowances();
        });

        // Row select → auto-fill
        allowanceTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = allowanceTable.getSelectedRow();
                allowanceNameField.setText(allowanceTable.getValueAt(row, 0).toString());
                percentageBox.setSelectedItem(allowanceTable.getValueAt(row, 1).toString());
                descriptionField.setText(allowanceTable.getValueAt(row, 2).toString());
            }
        });

        setVisible(true);
        setLocationRelativeTo(null);

        // Load data initially
        loadAllowances();
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

    // ✅ Save Allowance Record
    private void saveAllowance() {
        try (Connection conn = DBConnection.getConnection()) {
            int isPercentage = percentageBox.getSelectedItem().toString().equals("Yes") ? 1 : 0;
            String sql = "INSERT INTO Allowances (AllowanceName, IsPercentage, Description) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, allowanceNameField.getText());
            pst.setInt(2, isPercentage);   // bit value
            pst.setString(3, descriptionField.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Allowance Saved Successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Update Allowance Record
    private void updateAllowance() {
        try (Connection conn = DBConnection.getConnection()) {
            int isPercentage = percentageBox.getSelectedItem().toString().equals("Yes") ? 1 : 0;
            String sql = "UPDATE Allowances SET IsPercentage=?, Description=? WHERE AllowanceName=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, isPercentage);   // bit value
            pst.setString(2, descriptionField.getText());
            pst.setString(3, allowanceNameField.getText());
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Allowance Updated Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to update!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Delete Allowance Record
    private void deleteAllowance() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Allowances WHERE AllowanceName=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, allowanceNameField.getText());
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Allowance Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to delete!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Load Allowances Data into JTable
    private void loadAllowances() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT AllowanceName, IsPercentage, Description FROM Allowances";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Allowance Name");
            model.addColumn("Is Percentage");
            model.addColumn("Description");

            while (rs.next()) {
                String isPerc = rs.getInt("IsPercentage") == 1 ? "Yes" : "No";
                model.addRow(new Object[]{
                    rs.getString("AllowanceName"),
                    isPerc,
                    rs.getString("Description")
                });
            }

            allowanceTable.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Main Method (Test Run)
    public static void main(String[] args) {
        new AllowanceForm();
    }
}
