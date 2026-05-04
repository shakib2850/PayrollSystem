import javax.swing.*;
import java.awt.*;

public class UserForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JButton saveButton, updateButton, deleteButton;

    public UserForm() {
        setTitle("User Management Form");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel title = new JLabel("User Management Form");
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
        saveButton = new JButton("Save");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        buttonPanel.add(saveButton); buttonPanel.add(updateButton); buttonPanel.add(deleteButton);
        add(buttonPanel);

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
}
