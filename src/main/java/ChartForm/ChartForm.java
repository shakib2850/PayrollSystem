import javax.swing.*;
import java.awt.*;

public class ChartForm extends JFrame {
    public ChartForm() {
        setTitle("Charts / Reports");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("Charts / Reports (Demo)");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        // Placeholder panel for charts
        JPanel chartPanel = new JPanel();
        chartPanel.setBackground(Color.LIGHT_GRAY);
        chartPanel.add(new JLabel("Chart will be displayed here"));
        add(chartPanel, BorderLayout.CENTER);

        setVisible(true);
        
        setLocationRelativeTo(null);
    }
}
