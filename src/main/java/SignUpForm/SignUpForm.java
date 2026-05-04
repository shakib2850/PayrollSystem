import DBConnection.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class SignUpForm extends JFrame {
    private JTextField firstNameField, lastNameField, empIdField, contactField;
    private JTextField usernameField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> roleBox;
    private JButton signupButton, clearButton;

    public SignUpForm() {
        setTitle("Sign Up Form");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Create New Account");
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        // New fields
        addField("First Name:", firstNameField = new JTextField());
        addField("Last Name:", lastNameField = new JTextField());
        addField("Employee ID:", empIdField = new JTextField());
        addField("Contact Number:", contactField = new JTextField());

        // Existing fields
        addField("Username:", usernameField = new JTextField());
        addField("Email:", emailField = new JTextField());

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Arial", Font.BOLD, 16));
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(passLabel);
        passwordField = new JPasswordField();
        passwordField.setMaximumSize(new Dimension(200, 25));
        passwordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(passwordField);

        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setFont(new Font("Arial", Font.BOLD, 16));
        confirmLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(confirmLabel);
        confirmPasswordField = new JPasswordField();
        confirmPasswordField.setMaximumSize(new Dimension(200, 25));
        confirmPasswordField.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(confirmPasswordField);

        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        roleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(roleLabel);
        roleBox = new JComboBox<>(new String[]{"Admin", "HR", "Employee"});
        roleBox.setMaximumSize(new Dimension(200, 25));
        roleBox.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(roleBox);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        signupButton = new JButton("Sign Up");
        clearButton = new JButton("Clear");
        buttonPanel.add(signupButton); 
        buttonPanel.add(clearButton);
        add(buttonPanel);

        // ✅ Button Actions
        signupButton.addActionListener(e -> signUpUser());
        clearButton.addActionListener(e -> clearForm());

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

    // ✅ Sign Up Method
    private void signUpUser() {
        try (Connection conn = DBConnection.getConnection()) {
            String pass = new String(passwordField.getPassword());
            String confirm = new String(confirmPasswordField.getPassword());

            if (!pass.equals(confirm)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!");
                return;
            }

            String hashedPass = hashPassword(pass);

            String sql = "INSERT INTO Users (FirstName, LastName, EmployeeID, Contact, Username, Email, PasswordHash, Role) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, firstNameField.getText());
            pst.setString(2, lastNameField.getText());
            pst.setString(3, empIdField.getText());
            pst.setString(4, contactField.getText());
            pst.setString(5, usernameField.getText());
            pst.setString(6, emailField.getText());
            pst.setString(7, hashedPass);
            pst.setString(8, roleBox.getSelectedItem().toString());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "User Registered Successfully!");
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    // ✅ Clear Method
    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        empIdField.setText("");
        contactField.setText("");
        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        roleBox.setSelectedIndex(0);
    }

    // ✅ Password Hash Method
    private String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    // ✅ Main Method (Test Run)
    public static void main(String[] args) {
        new SignUpForm();
    }
}
