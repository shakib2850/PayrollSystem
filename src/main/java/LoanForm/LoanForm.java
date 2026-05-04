import DBConnection.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class LoanForm extends JFrame {
    private JTextField empIdField, loanAmountField, loanDateField;
    private JButton saveButton, updateButton, deleteButton;
    private JTable loanTable;
    private JScrollPane scrollPane;

    public LoanForm() {
        setTitle("Loan Form");
        setSize(600, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Loan Form");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        addField("Employee ID:", empIdField = new JTextField());
        addField("Loan Amount:", loanAmountField = new JTextField());
        addField("Loan Date (YYYY-MM-DD):", loanDateField = new JTextField());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        saveButton = new JButton("Save");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        buttonPanel.add(saveButton); buttonPanel.add(updateButton); buttonPanel.add(deleteButton);
        add(buttonPanel);

        // JTable
        loanTable = new JTable();
        scrollPane = new JScrollPane(loanTable);
        scrollPane.setPreferredSize(new Dimension(550, 150));
        add(scrollPane);

        // Button Actions
        saveButton.addActionListener(e -> {
            saveLoan();
            loadLoans();
        });
        updateButton.addActionListener(e -> {
            updateLoan();
            loadLoans();
        });
        deleteButton.addActionListener(e -> {
            deleteLoan();
            loadLoans();
        });

        // Row select → auto-fill
        loanTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = loanTable.getSelectedRow();
                empIdField.setText(loanTable.getValueAt(row, 0).toString());
                loanAmountField.setText(loanTable.getValueAt(row, 1).toString());
                loanDateField.setText(loanTable.getValueAt(row, 2).toString());
            }
        });

        setVisible(true);
        setLocationRelativeTo(null);

        // Load data initially
        loadLoans();
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

    // ✅ Save Loan Record
    private void saveLoan() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Loans (EmployeeID, LoanAmount, LoanDate) VALUES (?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(empIdField.getText()));
            pst.setDouble(2, Double.parseDouble(loanAmountField.getText()));
            pst.setString(3, loanDateField.getText());
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Loan Saved Successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Update Loan Record
    private void updateLoan() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Loans SET LoanAmount=?, LoanDate=? WHERE EmployeeID=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, Double.parseDouble(loanAmountField.getText()));
            pst.setString(2, loanDateField.getText());
            pst.setInt(3, Integer.parseInt(empIdField.getText()));
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Loan Updated Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to update!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Delete Loan Record
    private void deleteLoan() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Loans WHERE EmployeeID=? AND LoanDate=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(empIdField.getText()));
            pst.setString(2, loanDateField.getText());
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Loan Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to delete!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Load Loan Data into JTable
    private void loadLoans() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT EmployeeID, LoanAmount, LoanDate FROM Loans";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Employee ID");
            model.addColumn("Loan Amount");
            model.addColumn("Loan Date");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("EmployeeID"),
                    rs.getDouble("LoanAmount"),
                    rs.getString("LoanDate")
                });
            }

            loanTable.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Main Method (Test Run)
    public static void main(String[] args) {
        new LoanForm();
    }
}
