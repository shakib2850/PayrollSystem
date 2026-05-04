import DBConnection.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class SalaryForm extends JFrame {
    private JTextField basicSalaryField, overtimeRateField, bonusField, taxField, loanField;
    private JButton saveButton, updateButton, deleteButton;
    private JTable salaryTable;
    private JScrollPane scrollPane;

    public SalaryForm() {
        setTitle("Salary & Deduction Form");
        setSize(650, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Salary & Deduction Form");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(15));

        addField("Basic Salary:", basicSalaryField = new JTextField());
        addField("Overtime Rate:", overtimeRateField = new JTextField());
        addField("Bonus:", bonusField = new JTextField());
        addField("Tax:", taxField = new JTextField());
        addField("Loan:", loanField = new JTextField());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        saveButton = new JButton("Save");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        buttonPanel.add(saveButton); buttonPanel.add(updateButton); buttonPanel.add(deleteButton);
        add(buttonPanel);

        // JTable
        salaryTable = new JTable();
        scrollPane = new JScrollPane(salaryTable);
        scrollPane.setPreferredSize(new Dimension(600, 150));
        add(scrollPane);

        // Button Actions
        saveButton.addActionListener(e -> {
            saveSalary();
            loadSalaries();
        });
        updateButton.addActionListener(e -> {
            updateSalary();
            loadSalaries();
        });
        deleteButton.addActionListener(e -> {
            deleteSalary();
            loadSalaries();
        });

        // Row select → auto-fill
        salaryTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = salaryTable.getSelectedRow();
                basicSalaryField.setText(salaryTable.getValueAt(row, 0).toString());
                overtimeRateField.setText(salaryTable.getValueAt(row, 1).toString());
                bonusField.setText(salaryTable.getValueAt(row, 2).toString());
                taxField.setText(salaryTable.getValueAt(row, 3).toString());
                loanField.setText(salaryTable.getValueAt(row, 4).toString());
            }
        });

        setVisible(true);
        setLocationRelativeTo(null);

        // Load data initially
        loadSalaries();
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

    // ✅ Save Salary Record
    private void saveSalary() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO SalaryDeduction (BasicSalary, OvertimeRate, Bonus, Tax, Loan) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, Double.parseDouble(basicSalaryField.getText()));
            pst.setDouble(2, Double.parseDouble(overtimeRateField.getText()));
            pst.setDouble(3, Double.parseDouble(bonusField.getText()));
            pst.setDouble(4, Double.parseDouble(taxField.getText()));
            pst.setDouble(5, Double.parseDouble(loanField.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Salary Saved Successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Update Salary Record
    private void updateSalary() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE SalaryDeduction SET OvertimeRate=?, Bonus=?, Tax=?, Loan=? WHERE BasicSalary=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, Double.parseDouble(overtimeRateField.getText()));
            pst.setDouble(2, Double.parseDouble(bonusField.getText()));
            pst.setDouble(3, Double.parseDouble(taxField.getText()));
            pst.setDouble(4, Double.parseDouble(loanField.getText()));
            pst.setDouble(5, Double.parseDouble(basicSalaryField.getText()));
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Salary Updated Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to update!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Delete Salary Record
    private void deleteSalary() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM SalaryDeduction WHERE BasicSalary=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, Double.parseDouble(basicSalaryField.getText()));
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Salary Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to delete!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Load Salary Data into JTable
    private void loadSalaries() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT BasicSalary, OvertimeRate, Bonus, Tax, Loan FROM SalaryDeduction";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Basic Salary");
            model.addColumn("Overtime Rate");
            model.addColumn("Bonus");
            model.addColumn("Tax");
            model.addColumn("Loan");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getDouble("BasicSalary"),
                    rs.getDouble("OvertimeRate"),
                    rs.getDouble("Bonus"),
                    rs.getDouble("Tax"),
                    rs.getDouble("Loan")
                });
            }

            salaryTable.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Main Method (Test Run)
    public static void main(String[] args) {
        new SalaryForm();
    }
}
