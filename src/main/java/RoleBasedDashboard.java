import javax.swing.*;
import java.awt.*;

public class RoleBasedDashboard extends JFrame {
    public RoleBasedDashboard(String role) {
        setTitle("Dashboard - " + role);
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Welcome Label
        JLabel welcome = new JLabel("Welcome " + role + "!", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 22));
        add(welcome, BorderLayout.NORTH);

        // Panel for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,1,10,10));

        JButton payrollBtn = new JButton("Payroll Management");
        JButton attendanceBtn = new JButton("Attendance");
        JButton reportBtn = new JButton("Payslip Report");

        // ✅ Role অনুযায়ী ফিচার control
        if(role.equals("Admin")) {
            panel.add(payrollBtn);
            panel.add(attendanceBtn);
            panel.add(reportBtn);
        } else if(role.equals("HR")) {
            panel.add(payrollBtn);
            panel.add(attendanceBtn);
        } else if(role.equals("Employee")) {
            panel.add(reportBtn);
        }

        add(panel, BorderLayout.CENTER);

        setVisible(true);
        setLocationRelativeTo(null);
    }

    // ✅ Main Method (Test Run)
    public static void main(String[] args) {
        new RoleBasedDashboard("Admin"); // টেস্ট করার জন্য Admin role দিয়ে চালাও
    }
}
