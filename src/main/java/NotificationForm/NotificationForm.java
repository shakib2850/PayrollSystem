import javax.swing.*;
import java.awt.*;

public class NotificationForm extends JFrame {
    private JButton payrollNotifyBtn, attendanceNotifyBtn;

    public NotificationForm() {
        setTitle("Notifications");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Notifications");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        payrollNotifyBtn = new JButton("Payroll Generated");
        attendanceNotifyBtn = new JButton("Attendance Missing");

        payrollNotifyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        attendanceNotifyBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(10));
        add(payrollNotifyBtn);
        add(Box.createVerticalStrut(10));
        add(attendanceNotifyBtn);

        payrollNotifyBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,"Payroll Generated Successfully!"));
        attendanceNotifyBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,"Attendance Missing Alert!"));

        setVisible(true);
        
        setLocationRelativeTo(null);
    }
}
