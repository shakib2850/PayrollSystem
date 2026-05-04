import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import EmployeeForm.EmployeeForm;

public class PayrollDashboard extends JFrame {
    private JTextField searchField;
    private boolean darkMode = false;
    private JPanel sidebar, topPanel, mainPanel, footerPanel;

    public PayrollDashboard(String role) {
        setTitle("Payroll Management System - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 700));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());

        // === Sidebar ===
        sidebar = new JPanel();
        sidebar.setBackground(new Color(56, 128, 177));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));

        // Theme Toggle
        JPanel togglePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        togglePanel.setOpaque(false);
        JButton themeToggle = new JButton("🌙");
        themeToggle.setPreferredSize(new Dimension(40, 30));
        themeToggle.addActionListener(e -> toggleTheme());
        togglePanel.add(themeToggle);
        sidebar.add(togglePanel);
        sidebar.add(Box.createVerticalStrut(10));

        // Profile Section
        JLabel userPic = new JLabel("👤", SwingConstants.CENTER);
        userPic.setFont(new Font("Segoe UI", Font.PLAIN, 50));
        userPic.setOpaque(true);
        userPic.setBackground(Color.WHITE);
        userPic.setPreferredSize(new Dimension(100,100));
        userPic.setMaximumSize(new Dimension(100,100));
        userPic.setAlignmentX(Component.CENTER_ALIGNMENT);
        userPic.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2, true),
            BorderFactory.createEmptyBorder(10,10,10,10)
        ));

        JLabel userName = new JLabel("Logged in: Admin", SwingConstants.CENTER);
        userName.setForeground(Color.WHITE);
        userName.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userName.setAlignmentX(Component.CENTER_ALIGNMENT);

        sidebar.add(userPic);
        sidebar.add(Box.createVerticalStrut(5));
        sidebar.add(userName);
        sidebar.add(Box.createVerticalStrut(20));

        // Sidebar Buttons
        String[] buttonNames = {
            "Dashboard","Employees","Attendance","Salary & Deduction","Payroll",
            "Report/Payslip","Departments","Leave","Loan","Allowances",
            "Users","Export/Print","Charts/Reports","Notifications"
        };
        for (String name : buttonNames) {
            JButton btn = createStyledButton(name);
            btn.setMaximumSize(new Dimension(250, 45));
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.addActionListener(e -> openForm(name));
            sidebar.add(btn);
            sidebar.add(Box.createVerticalStrut(10));
        }

        // Logout Button
        sidebar.add(Box.createVerticalGlue());
        JButton logoutBtn = new JButton("🚪 Logout");
        logoutBtn.setMaximumSize(new Dimension(250, 40));
        logoutBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutBtn.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "You have been logged out.");
                System.exit(0);
            }
        });
        sidebar.add(logoutBtn);
        sidebar.add(Box.createVerticalStrut(15));

        JScrollPane sidebarScroll = new JScrollPane(sidebar,
            JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sidebarScroll.setPreferredSize(new Dimension(340, getHeight()));

        // === Top Panel ===
        topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 60, 90));

        JLabel bigTitle = new JLabel("HR MANAGEMENT AND DIGITAL PAYROLL PROCESSING SYSTEM");
        bigTitle.setFont(new Font("Arial Black", Font.BOLD, 20));
        bigTitle.setForeground(Color.WHITE);
        bigTitle.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        topPanel.add(bigTitle, BorderLayout.WEST);

        // Right Panel (Search + Buttons)
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(true);
        rightPanel.setBackground(new Color(30, 60, 90));

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setForeground(Color.WHITE);
        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Go");
        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Sign Up");

        searchBtn.addActionListener(e -> {
            String query = searchField.getText().trim();
            if(query.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter something to search!");
            } else {
                JOptionPane.showMessageDialog(this, "Searching for: " + query);
            }
        });
        loginBtn.addActionListener(e -> new LoginForm().setVisible(true));
        signupBtn.addActionListener(e -> new SignUpForm().setVisible(true));

        rightPanel.add(searchLabel);
        rightPanel.add(searchField);
        rightPanel.add(searchBtn);
        rightPanel.add(loginBtn);
        rightPanel.add(signupBtn);

        topPanel.add(rightPanel, BorderLayout.EAST);

        // === Main Panel ===
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(220,235,250));

        // Overview Buttons
JPanel overviewPanel = new JPanel(new GridLayout(1, 9, 10, 10));
overviewPanel.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
String[] overviewButtons = {
    "Employees List","Leave Requests","Last Payroll","Total Loans","Departments",
    "Allowances","Users","Salary Reports","Notifications"
};
for (String name : overviewButtons) {
    JButton btn = createStyledButton(name);
    overviewPanel.add(btn);
}
mainPanel.add(overviewPanel, BorderLayout.NORTH);

        // === Middle Section (Cards + Scroll) ===
        // === Middle Section (Cards + Scroll) ===
JPanel chartPanel = new JPanel(new GridLayout(2, 3, 15, 15));
chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

// -------- Top Row: Custom Content --------
chartPanel.add(createLoanAllowanceTable());   // Table with 4 rows
chartPanel.add(createUserActivityPanel());    // Notifications / Logs
chartPanel.add(createReportsPieChart());      // Pie Chart (Reports)

// -------- Bottom Row: Charts --------
chartPanel.add(createPieChartPanel());        // Salary Distribution
chartPanel.add(createBarChartPanel());        // Attendance Overview
chartPanel.add(createDeptBarChartPanel());    // Department Distribution (Bar Chart)

JScrollPane middleScroll = new JScrollPane(chartPanel,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

mainPanel.add(middleScroll, BorderLayout.CENTER);

        // Bottom Section (Quick Actions)
        JPanel quickActionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        String[] actions = {"Add Employee","Approve Leave","Generate Payslip","Export Report"};
        for (String action : actions) {
            JButton btn = createStyledButton(action);
            quickActionPanel.add(btn);
        }
        mainPanel.add(quickActionPanel, BorderLayout.SOUTH);

       // === Footer Panel ===
        footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(30, 60, 90));
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Quick Stats
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setOpaque(false);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
        JLabel statsTitle = new JLabel("Quick Stats");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        statsTitle.setForeground(Color.WHITE);
        statsPanel.add(statsTitle);
        String[] quickStats = {
            "👥 Employees: 120","📝 Leave Requests: 5","💰 Payroll: 26 April",
            "🏢 Departments: 12","💳 Active Loans: 8"
        };
        for (String stat : quickStats) {
            JLabel lbl = new JLabel(stat);
            lbl.setForeground(Color.WHITE);
            statsPanel.add(lbl);
        }

        // Notifications (Auto Update)
        JPanel notifyPanel = new JPanel();
        notifyPanel.setLayout(new BoxLayout(notifyPanel, BoxLayout.Y_AXIS));
        notifyPanel.setOpaque(false);
        notifyPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        JLabel notifyTitle = new JLabel("Notifications");
        notifyTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        notifyTitle.setForeground(Color.WHITE);
        notifyPanel.add(notifyTitle);
        String[] notifications = {
            "🔔 New Payslip Generated","⚠ Pending Loan Approval","📌 New Employee Added",
            "📢 Upcoming Holiday Notice","🕒 Attendance Report Ready","✅ System Backup Completed"
        };
        JLabel[] notifyLabels = new JLabel[5];
        for (int i = 0; i < 5; i++) {
            notifyLabels[i] = new JLabel(notifications[i]);
            notifyLabels[i].setForeground(Color.WHITE);
            notifyPanel.add(notifyLabels[i]);
        }
        
                // === Notifications Auto Update Timer ===
        Timer timer = new Timer(5000, e -> {
            for (int i = 0; i < 4; i++) {
                notifyLabels[i].setText(notifyLabels[i+1].getText());
            }
            int randomIndex = (int)(Math.random() * notifications.length);
            notifyLabels[4].setText(notifications[randomIndex]);
        });
        timer.start();

        // Footer Buttons (Center)
        JPanel buttonRowTop = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        buttonRowTop.setOpaque(false);

        MouseAdapter hoverEffect = new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                JButton btn = (JButton) e.getSource();
                btn.setBackground(new Color(100, 149, 237));
                btn.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                JButton btn = (JButton) e.getSource();
                btn.setBackground(new Color(230, 230, 250));
                btn.setForeground(Color.BLACK);
            }
        };

        String[] footerButtonNames = {
            "Help", "Contact Support", "Settings",
            "Export Data", "View Reports", "Security Options", "Toggle Theme"
        };
        for (String name : footerButtonNames) {
            JButton btn = new JButton(name);
            btn.setBackground(new Color(230, 230, 250));
            btn.setForeground(Color.BLACK);
            btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btn.addMouseListener(hoverEffect);
            if (name.equals("Toggle Theme")) {
                btn.addActionListener(e -> toggleTheme());
            }
            buttonRowTop.add(btn);
        }

        // === Top Row Layout ===
        JPanel infoRow = new JPanel(new BorderLayout());
        infoRow.setOpaque(false);
        infoRow.add(statsPanel, BorderLayout.WEST);
        infoRow.add(buttonRowTop, BorderLayout.CENTER);
        infoRow.add(notifyPanel, BorderLayout.EAST);

        // === Bottom Row: Branding + Update + Status ===
        JPanel bottomRow = new JPanel(new BorderLayout());
        bottomRow.setOpaque(false);

        // Branding Center
        JPanel brandPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        brandPanel.setOpaque(false);
        JLabel brandLabel = new JLabel("© HR Management & Payroll System — All Operations Running Smoothly");
        brandLabel.setForeground(Color.WHITE);
        JLabel addressLabel = new JLabel("📍 Dhaka, Bangladesh");
        addressLabel.setForeground(Color.WHITE);
        brandPanel.add(brandLabel);
        brandPanel.add(addressLabel);

        // Last Update (Left)
        JPanel updatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        updatePanel.setOpaque(false);
        JLabel updateLabel = new JLabel("Last Update: 14 April 2026");
        updateLabel.setForeground(Color.WHITE);
        updateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        updatePanel.add(updateLabel);

        // Status Right
        JPanel statusPanel = new JPanel();
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        statusPanel.setOpaque(false);
        statusPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        JLabel statusLabel = new JLabel("System Status: Active");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        statusPanel.add(statusLabel);

        bottomRow.add(updatePanel, BorderLayout.WEST);
        bottomRow.add(brandPanel, BorderLayout.CENTER);
        bottomRow.add(statusPanel, BorderLayout.EAST);

        // === Add Rows to Footer ===
        footerPanel.add(infoRow, BorderLayout.NORTH);
        footerPanel.add(bottomRow, BorderLayout.SOUTH);
        
        // === Add All Panels to Frame ===
        add(sidebarScroll, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
    }

    // -------- Helper Methods --------

// Pie Chart (Salary Distribution)
private ChartPanel createPieChartPanel() {
    DefaultPieDataset dataset = new DefaultPieDataset();
    dataset.setValue("Basic", 40);
    dataset.setValue("Allowance", 25);
    dataset.setValue("Bonus", 20);
    dataset.setValue("Other", 15);

    JFreeChart chart = ChartFactory.createPieChart(
            "Salary Distribution", dataset, true, true, false);

    ChartPanel panel = new ChartPanel(chart);
    panel.setPreferredSize(new Dimension(300, 200));
    return panel;
}
//Loan & Allowance Trends → Table
private JPanel createLoanAllowanceTable() {
    String[] columns = {"ID", "Type", "Amount", "Status"};
    Object[][] data = {
        {"L001", "Loan", "৳50,000", "Active"},
        {"L002", "Loan", "৳30,000", "Pending"},
        {"L003", "Loan", "৳40,000", "Approved"},
        {"L004", "Loan", "৳25,000", "Active"},
        {"A001", "Allowance", "৳5,000", "Approved"},
        {"A002", "Allowance", "৳3,000", "Pending"},
        {"A003", "Allowance", "৳4,500", "Approved"},
        {"A004", "Allowance", "৳2,500", "Rejected"},
        {"L005", "Loan", "৳60,000", "Pending"},
        {"L006", "Loan", "৳35,000", "Active"},
        {"A005", "Allowance", "৳6,000", "Approved"},
        {"A006", "Allowance", "৳3,500", "Pending"}
    };

    JTable table = new JTable(data, columns);
    JScrollPane scrollPane = new JScrollPane(table);

    JPanel panel = new JPanel(new BorderLayout());
    panel.add(new JLabel("Loan & Allowance Trends", SwingConstants.CENTER), BorderLayout.NORTH);
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.setPreferredSize(new Dimension(300, 200));
    return panel;
}
//User Activity
private JPanel createUserActivityPanel() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.add(new JLabel("User Activity", SwingConstants.CENTER), BorderLayout.NORTH);

    JTextArea logArea = new JTextArea(10, 25); // 10 rows দেখানোর জন্য
    logArea.setEditable(false);
    logArea.setText(
        "👤 Admin logged in at 10:00 AM\n" +
        "👤 HR Manager added new employee\n" +
        "🔔 Password reset requested\n" +
        "📌 Finance Officer generated payroll report\n" +
        "👤 User 'Shakib' updated allowance data\n" +
        "🔔 Loan approval pending for Employee #102\n" +
        "📌 Attendance report exported by HR\n" +
        "👤 IT Officer changed system settings\n" +
        "🔔 New department created: Research\n" +
        "📌 Payslip generated for March payroll"
    );

    JScrollPane scrollPane = new JScrollPane(logArea);
    panel.add(scrollPane, BorderLayout.CENTER);
    panel.setPreferredSize(new Dimension(300, 200));
    return panel;
}


//Reports & Analytics → Pie Chart
private ChartPanel createReportsPieChart() {
    DefaultPieDataset dataset = new DefaultPieDataset();
    dataset.setValue("Monthly Reports", 50);
    dataset.setValue("Quarterly Reports", 30);
    dataset.setValue("Annual Reports", 20);

    JFreeChart chart = ChartFactory.createPieChart(
            "Reports & Analytics", dataset, true, true, false);

    ChartPanel panel = new ChartPanel(chart);
    panel.setPreferredSize(new Dimension(300, 200));
    return panel;
}

// Bar Chart (Attendance Overview)
private ChartPanel createBarChartPanel() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    dataset.addValue(80, "Attendance", "Jan");
    dataset.addValue(75, "Attendance", "Feb");
    dataset.addValue(90, "Attendance", "Mar");

    JFreeChart chart = ChartFactory.createBarChart(
            "Attendance Overview", "Month", "Percentage", dataset);

    ChartPanel panel = new ChartPanel(chart);
    panel.setPreferredSize(new Dimension(300, 200));
    return panel;
}

// Bar Chart (Department Distribution)
private ChartPanel createDeptBarChartPanel() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    dataset.addValue(50, "Employees", "HR");
    dataset.addValue(70, "Employees", "IT");
    dataset.addValue(40, "Employees", "Finance");

    JFreeChart chart = ChartFactory.createBarChart(
            "Department Distribution", "Department", "Employees", dataset);

    ChartPanel panel = new ChartPanel(chart);
    panel.setPreferredSize(new Dimension(300, 200));
    return panel;
}

    // === Utility Method: Card Section ===
    private JPanel createCardSection(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 5, 5, new Color(220,220,220)),
            BorderFactory.createLineBorder(new Color(180,180,180), 1, true)
        ));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(titleLabel, BorderLayout.NORTH);

        JLabel contentLabel = new JLabel("Content Placeholder", SwingConstants.CENTER);
        panel.add(contentLabel, BorderLayout.CENTER);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(245, 245, 255));
            }
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }
        });
        return panel;
    }

    // === Sidebar Button Styling ===
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(new Color(230, 230, 250));
        button.setForeground(Color.BLACK);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(100, 149, 237));
                button.setForeground(Color.WHITE);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(230, 230, 250));
                button.setForeground(Color.BLACK);
            }
        });
        return button;
    }

    // === Form Navigation ===
    private void openForm(String name) {
        switch (name) {
            case "Employees": new EmployeeForm().setVisible(true); break;
            case "Login": new LoginForm().setVisible(true); break;
            case "Sign Up": new SignUpForm().setVisible(true); break;
            case "Payroll": new PayrollForm().setVisible(true); break;
            case "Leave": new LeaveForm().setVisible(true); break;
            case "Attendance": new AttendanceForm().setVisible(true); break;
            case "Departments": new DepartmentForm().setVisible(true); break;
            case "Loan": new LoanForm().setVisible(true); break;
            case "Allowances": new AllowanceForm().setVisible(true); break;
            case "Users": new UserForm().setVisible(true); break;
            case "Export/Print": new ExportForm().setVisible(true); break;
            case "Charts/Reports": new ChartForm().setVisible(true); break;
            case "Notifications": new NotificationForm().setVisible(true); break;
            case "Report/Payslip": new ReportForm().setVisible(true); break;
            case "Salary & Deduction": new SalaryForm().setVisible(true); break;
            default: JOptionPane.showMessageDialog(this, name + " Form not implemented yet!");
        }
    }

    // === Theme Toggle ===
    private void toggleTheme() {
        darkMode = !darkMode;
        Color bg = darkMode ? new Color(30,30,30) : new Color(56,128,177);
        Color topBg = darkMode ? new Color(20,20,20) : new Color(30,60,90);
        Color footerBg = darkMode ? new Color(20,20,20) : new Color(30,60,90);

        sidebar.setBackground(bg);
        topPanel.setBackground(topBg);
        mainPanel.setBackground(darkMode ? new Color(45,45,45) : Color.WHITE);
        footerPanel.setBackground(footerBg);

        sidebar.repaint();
        topPanel.repaint();
        mainPanel.repaint();
        footerPanel.repaint();
    }

    // === Main Method ===
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PayrollDashboard("Admin").setVisible(true));
    }
}
