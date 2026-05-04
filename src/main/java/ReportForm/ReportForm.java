
import javax.swing.*;
import java.awt.*;
import java.awt.print.*;

public class ReportForm extends JFrame {
    private JTextField monthField, empIdField, nameField, netSalaryField;
    private JButton printButton;

    public ReportForm() {
        setTitle("Report / Payslip Form");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Report / Payslip Form");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(Box.createVerticalStrut(10));
        add(title);
        add(Box.createVerticalStrut(15));

        addField("Month:", monthField = new JTextField());
        addField("Employee ID:", empIdField = new JTextField());
        addField("Name:", nameField = new JTextField());
        addField("Net Salary:", netSalaryField = new JTextField());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        printButton = new JButton("Print Payslip");
        buttonPanel.add(printButton);
        add(buttonPanel);

        // ✅ Print button action
        printButton.addActionListener(e -> printPayslip());

        setVisible(true);
        setLocationRelativeTo(null);
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

    // ✅ Print Payslip Method
    private void printPayslip() {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pf, int pageIndex) {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) g;
                g2d.translate(pf.getImageableX(), pf.getImageableY());

                int y = 100;
                g.setFont(new Font("Arial", Font.BOLD, 18));
                g.drawString("Payslip Report", 200, y);
                y += 40;

                g.setFont(new Font("Arial", Font.PLAIN, 14));
                g.drawString("Month: " + monthField.getText(), 100, y); y += 25;
                g.drawString("Employee ID: " + empIdField.getText(), 100, y); y += 25;
                g.drawString("Name: " + nameField.getText(), 100, y); y += 25;
                g.drawString("Net Salary: " + netSalaryField.getText(), 100, y);

                return PAGE_EXISTS;
            }
        });

        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this, "Payslip Printed Successfully!");
            } catch (PrinterException ex) {
                JOptionPane.showMessageDialog(this, "Error Printing: " + ex.getMessage());
            }
        }
    }

    // ✅ Main Method (Test Run)
    public static void main(String[] args) {
        new ReportForm();
    }
}
