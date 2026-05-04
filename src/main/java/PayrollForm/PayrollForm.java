import DBConnection.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class PayrollForm extends JFrame {
    private JTextField empIdField, fullNameField, monthField, totalSalaryField, totalDeductionField, netSalaryField;
    private JButton saveButton, updateButton, deleteButton, generateButton;
    private JTable payrollTable;
    private JScrollPane scrollPane;

    public PayrollForm() {
        setTitle("Payroll Form");
        setSize(650, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Title
        JLabel title = new JLabel("Payroll Form");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(15));

        // Fields
        addField("Employee ID:", empIdField = new JTextField());
        addField("Full Name:", fullNameField = new JTextField());
        addField("Month:", monthField = new JTextField());
        addField("Total Salary:", totalSalaryField = new JTextField());
        addField("Total Deduction:", totalDeductionField = new JTextField());
        addField("Net Salary:", netSalaryField = new JTextField());

        // Buttons row
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        saveButton = new JButton("Save");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        generateButton = new JButton("Generate Payroll");
        buttonPanel.add(saveButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(generateButton);

        add(Box.createVerticalStrut(15));
        add(buttonPanel);

        // JTable
        payrollTable = new JTable();
        scrollPane = new JScrollPane(payrollTable);
        scrollPane.setPreferredSize(new Dimension(600, 200));
        add(scrollPane);

        // Button Actions
        saveButton.addActionListener(e -> { savePayroll(); loadPayroll(); });
        updateButton.addActionListener(e -> { updatePayroll(); loadPayroll(); });
        deleteButton.addActionListener(e -> { deletePayroll(); loadPayroll(); });
        generateButton.addActionListener(e -> { generatePayroll(); loadPayroll(); });

        // Row select → auto-fill
        payrollTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                int row = payrollTable.getSelectedRow();
                empIdField.setText(payrollTable.getValueAt(row, 0).toString());
                fullNameField.setText(payrollTable.getValueAt(row, 1).toString());
                monthField.setText(payrollTable.getValueAt(row, 2).toString());
                totalSalaryField.setText(payrollTable.getValueAt(row, 3).toString());
                totalDeductionField.setText(payrollTable.getValueAt(row, 4).toString());
                netSalaryField.setText(payrollTable.getValueAt(row, 5).toString());
            }
        });

        setVisible(true);
        setLocationRelativeTo(null);

        // Load data initially
        loadPayroll();
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

    // ✅ Save Payroll Record
    private void savePayroll() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO Payroll (EmployeeID, FullName, Month, TotalSalary, TotalDeduction, NetSalary) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(empIdField.getText()));
            pst.setString(2, fullNameField.getText());
            pst.setString(3, monthField.getText());
            pst.setDouble(4, Double.parseDouble(totalSalaryField.getText()));
            pst.setDouble(5, Double.parseDouble(totalDeductionField.getText()));
            pst.setDouble(6, Double.parseDouble(netSalaryField.getText()));
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Payroll Saved Successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Update Payroll Record
    private void updatePayroll() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE Payroll SET FullName=?, TotalSalary=?, TotalDeduction=?, NetSalary=? WHERE EmployeeID=? AND Month=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, fullNameField.getText());
            pst.setDouble(2, Double.parseDouble(totalSalaryField.getText()));
            pst.setDouble(3, Double.parseDouble(totalDeductionField.getText()));
            pst.setDouble(4, Double.parseDouble(netSalaryField.getText()));
            pst.setInt(5, Integer.parseInt(empIdField.getText()));
            pst.setString(6, monthField.getText());
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Payroll Updated Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to update!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Delete Payroll Record
    private void deletePayroll() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "DELETE FROM Payroll WHERE EmployeeID=? AND Month=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, Integer.parseInt(empIdField.getText()));
            pst.setString(2, monthField.getText());
            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Payroll Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No record found to delete!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Generate Payroll (SalaryDeduction + Allowances → Net Salary)
// ✅ Generate Payroll (SalaryDeduction + Allowances → Net Salary)
private void generatePayroll() {
    try (Connection conn = DBConnection.getConnection()) {
        // Step 1: SalaryDeduction থেকে ডেটা নাও
        String sql = "SELECT BasicSalary, Bonus, Tax, Loan FROM SalaryDeduction WHERE EmployeeID=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(empIdField.getText()));
        ResultSet rs = pst.executeQuery();

        double totalSalary = 0;
        double totalDeduction = 0;
        double basicSalary = 0;
        double bonus = 0;

        while (rs.next()) {
            basicSalary = rs.getDouble("BasicSalary");
            bonus = rs.getDouble("Bonus");
            double tax = rs.getDouble("Tax");
            double loan = rs.getDouble("Loan");

            totalSalary += (basicSalary + bonus);
            totalDeduction += (tax + loan);
        }

        // Step 2: Allowances যোগ করো (fixed + percentage)
        String sql2 = "SELECT IsPercentage, Description FROM Allowances WHERE EmployeeID=?";
        PreparedStatement pst2 = conn.prepareStatement(sql2);
        pst2.setInt(1, Integer.parseInt(empIdField.getText()));
        ResultSet rs2 = pst2.executeQuery();

        double allowance = 0;
        while (rs2.next()) {
            boolean isPercent = rs2.getBoolean("IsPercentage");
            String desc = rs2.getString("Description");

            if (desc != null && !desc.isEmpty()) {
                try {
                    double value = Double.parseDouble(desc); // শুধু numeric হলে কাজ করবে
                    if (isPercent) {
                        allowance += (basicSalary * value / 100.0);
                    } else {
                        allowance += value;
                    }
                } catch (NumberFormatException nfe) {
                    // যদি Description টেক্সট হয় (যেমন "Fixed transport allowance"), তাহলে skip করো
                    System.out.println("Skipping non-numeric allowance: " + desc);
                }
            }
        }

        // Step 3: Net Salary হিসাব করো
        double netSalary = totalSalary + allowance - totalDeduction;

        // Step 4: Payroll টেবিলে Insert করো
        String insert = "INSERT INTO Payroll (EmployeeID, FullName, Month, TotalSalary, TotalDeduction, NetSalary) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pst3 = conn.prepareStatement(insert);
        pst3.setInt(1, Integer.parseInt(empIdField.getText()));
        pst3.setString(2, fullNameField.getText());
        pst3.setString(3, monthField.getText());
        pst3.setDouble(4, totalSalary + allowance);
        pst3.setDouble(5, totalDeduction);
        pst3.setDouble(6, netSalary);
        pst3.executeUpdate();

        // Step 5: Form এ দেখাও
        totalSalaryField.setText(String.valueOf(totalSalary + allowance));
        totalDeductionField.setText(String.valueOf(totalDeduction));
        netSalaryField.setText(String.valueOf(netSalary));

        JOptionPane.showMessageDialog(this, "Payroll Generated Successfully!\nNet Salary: " + netSalary);
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
    }
}


    // ✅ Load Payroll Data into JTable
    private void loadPayroll() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT EmployeeID, FullName, Month, TotalSalary, TotalDeduction, NetSalary FROM Payroll";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Employee ID");
            model.addColumn("Full Name");
            model.addColumn("Month");
            model.addColumn("Total Salary");
            model.addColumn("Total Deduction");
            model.addColumn("Net Salary");

            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("EmployeeID"),
                    rs.getString("FullName"),
                    rs.getString("Month"),
                    rs.getDouble("TotalSalary"),
                    rs.getDouble("TotalDeduction"),
                    rs.getDouble("NetSalary")
                });
            }

            payrollTable.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Main Method (Test Run)
    public static void main(String[] args) {
        new PayrollForm();
    }
}
