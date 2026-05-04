import DBConnection.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JButton loginButton, clearButton;

    public LoginForm() {
        setTitle("Login Form");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel title = new JLabel("User Login");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        // Username
        addField("Username:", usernameField = new JTextField());

        // Password
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(passLabel);
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(200, 25));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(passwordField);

        // Role Dropdown
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(roleLabel);
        roleBox = new JComboBox<>(new String[]{"Admin", "HR", "Employee"});
        roleBox.setMaximumSize(new Dimension(200, 25));
        roleBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(roleBox);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        loginButton = new JButton("Login");
        clearButton = new JButton("Clear");
        buttonPanel.add(loginButton); 
        buttonPanel.add(clearButton);
        add(buttonPanel);

        // ✅ Login action
        loginButton.addActionListener(e -> loginUser());

        // ✅ Clear action
        clearButton.addActionListener(e -> {
            usernameField.setText("");
            passwordField.setText("");
            roleBox.setSelectedIndex(0);
        });

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

    // ✅ Login Method
    private void loginUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if(username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,"Please enter Username and Password!");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String hashedPass = hashPassword(password);

            String sql = "SELECT Role FROM Users WHERE Username=? AND PasswordHash=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, username);
            pst.setString(2, hashedPass);
            ResultSet rs = pst.executeQuery();

            if(rs.next()) {
                String role = rs.getString("Role");
                JOptionPane.showMessageDialog(this,"Login Successful as " + role);

                // ✅ Role-based Common Dashboard
                new RoleBasedDashboard(role).setVisible(true);

                dispose();
            } else {
                JOptionPane.showMessageDialog(this,"Invalid Username or Password!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,"Error: " + ex.getMessage());
        }
    }

    // ✅ Password Hash Method
    private String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // ✅ Main Method (Test Run)
    public static void main(String[] args) {
        new LoginForm();
    }
}
