import javax.swing.*;
import java.awt.*;

public class ExportForm extends JFrame {
    private JButton printBtn, pdfBtn, excelBtn;

    public ExportForm() {
        setTitle("Export / Print");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Export / Print Options");
        title.setFont(new Font("Arial", Font.BOLD, 20));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(title);

        printBtn = new JButton("Print Report");
        pdfBtn = new JButton("Export as PDF");
        excelBtn = new JButton("Export as Excel");

        printBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        pdfBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        excelBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(10));
        add(printBtn);
        add(Box.createVerticalStrut(10));
        add(pdfBtn);
        add(Box.createVerticalStrut(10));
        add(excelBtn);

        // Dummy actions
        printBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,"Printing Report..."));
        pdfBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,"Exporting to PDF..."));
        excelBtn.addActionListener(e -> JOptionPane.showMessageDialog(this,"Exporting to Excel..."));

        setVisible(true);
        
        setLocationRelativeTo(null);
    }
}
