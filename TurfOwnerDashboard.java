import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class TurfOwnerDashboard extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    private final static String LANDING_VIEW = "LANDING";
    private final static String DETAIL_VIEW = "DETAIL";
    private final static String ADD_TURF_VIEW = "ADD_TURF";
    
    private JPanel cardPanel;
    private JPanel detailViewPanel; 
    
    private final static Color APP_BACKGROUND = new Color(240, 240, 240);
    private final static Color ACCENT_COLOR = Color.BLACK; 
    private final static Color NEUTRAL_BUTTON_COLOR = new Color(220, 220, 220);
    private final static Color TEXT_COLOR_MAIN = new Color(50, 50, 50);
    private final static Color TURF_BOX_BACKGROUND = Color.WHITE;
    
    static class Turf {
        public final String id;
        public final String name;
        public final String location;
        public final double hourlyRate;

        public Turf(String id, String name, String location, double hourlyRate) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.hourlyRate = hourlyRate;
        }
    }

    static class Booking {
        public final String turfId;
        public final double totalCost;

        public Booking(String turfId, double totalCost) {
            this.turfId = turfId;
            this.totalCost = totalCost;
        }
    }

    private final List<Booking> allBookings = new ArrayList<>();
    private final List<Turf> managedTurfList = new ArrayList<>(); 
    private int nextTurfId = 1004; 

    public TurfOwnerDashboard() {
        super("Turf Management Platform");

        initializeMockData();
        setupFrame();

        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(APP_BACKGROUND); 
        
        cardPanel.add(createLandingPanel(), LANDING_VIEW);
        
        detailViewPanel = createEmptyDetailPanel();
        cardPanel.add(detailViewPanel, DETAIL_VIEW);
        
        cardPanel.add(createAddTurfPanel(), ADD_TURF_VIEW);
        
        add(cardPanel, BorderLayout.CENTER);
    }

    private void initializeMockData() {
        managedTurfList.add(new Turf("TA001", "Ground Zero Turf", "City Park", 1500.00));
        managedTurfList.add(new Turf("TB002", "Champions Arena", "Main Road", 2000.00));
        managedTurfList.add(new Turf("TC003", "Sunset Pitch", "Riverside", 1000.00));
        
        allBookings.add(new Booking("TA001", 1500.00));
        allBookings.add(new Booking("TA001", 750.00));
        allBookings.add(new Booking("TB002", 800.00));
        allBookings.add(new Booking("TA001", 2250.00));
        allBookings.add(new Booking("TC003", 1000.00));
        allBookings.add(new Booking("TB002", 600.00));
    }
    
    private void addTurf(String name, String location, double rate) {
        String newId = "T" + (nextTurfId++);
        Turf newTurf = new Turf(newId, name, location, rate);
        managedTurfList.add(newTurf);
        
        cardPanel.add(createLandingPanel(), LANDING_VIEW); 
        cardLayout.show(cardPanel, LANDING_VIEW);
    }

    private int calculateBookings(String turfId) {
        int bookings = 0;
        for (Booking booking : allBookings) {
            if (booking.turfId.equals(turfId)) {
                bookings++;
            }
        }
        return bookings;
    }
    
    private double calculateIncome(String turfId) {
        double income = 0.0;
        for (Booking booking : allBookings) {
            if (booking.turfId.equals(turfId)) {
                income += booking.totalCost;
            }
        }
        return income; 
    }

    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 550);
        setLocationRelativeTo(null); 
    }
    
    private JPanel createEmptyDetailPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(APP_BACKGROUND); 
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        return panel;
    }

    private JPanel createLandingPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(APP_BACKGROUND); 

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); 
        mainPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel("Turf Owner Console");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(25)); 

        JLabel subtitleLabel = new JLabel("Click a Turf to see its Performance:");
        subtitleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        subtitleLabel.setForeground(TEXT_COLOR_MAIN);
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(10));

        JPanel turfListPanel = new JPanel();
        turfListPanel.setLayout(new BoxLayout(turfListPanel, BoxLayout.Y_AXIS));
        turfListPanel.setBackground(APP_BACKGROUND); 
        turfListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        for (Turf turf : managedTurfList) {
            turfListPanel.add(createTurfButtonBox(turf));
            turfListPanel.add(Box.createVerticalStrut(10));
        }
        if (!managedTurfList.isEmpty()) {
             turfListPanel.remove(turfListPanel.getComponentCount() - 1);
        }
        
        mainPanel.add(turfListPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        JButton addButton = new JButton(" Add New Turf ");
        addButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addButton.addActionListener(e -> cardLayout.show(cardPanel, ADD_TURF_VIEW));
        
        addButton.setBackground(NEUTRAL_BUTTON_COLOR); 
        addButton.setForeground(TEXT_COLOR_MAIN); 
        addButton.setFocusPainted(false);
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        mainPanel.add(addButton);
        
        contentPanel.add(mainPanel, BorderLayout.NORTH);
        return contentPanel;
    }
    
    private JPanel createAddTurfPanel() {
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(APP_BACKGROUND); 
        
        JLabel titleLabel = new JLabel("Register New Turf");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(titleLabel);
        contentPanel.add(Box.createVerticalStrut(25)); 
        
        JTextField nameField = new JTextField(20);
        JTextField locationField = new JTextField(20);
        JTextField rateField = new JTextField(20);
        
        contentPanel.add(createFormField("Turf Name:", nameField));
        contentPanel.add(createFormField("Location:", locationField));
        contentPanel.add(createFormField("Hourly Rate (Rs.):", rateField));
        
        contentPanel.add(Box.createVerticalStrut(30));

        JButton saveButton = new JButton("Save Turf");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String location = locationField.getText().trim();
                double rate = Double.parseDouble(rateField.getText().trim());

                if (name.isEmpty() || location.isEmpty() || rate <= 0) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields correctly.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                addTurf(name, location, rate);
                
                nameField.setText("");
                locationField.setText("");
                rateField.setText("");
                
                JOptionPane.showMessageDialog(this, "Turf '" + name + "' added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Hourly Rate must be a valid number.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> cardLayout.show(cardPanel, LANDING_VIEW));

        saveButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        saveButton.setBackground(ACCENT_COLOR); 
        saveButton.setForeground(Color.WHITE); 
        
        cancelButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        cancelButton.setBackground(NEUTRAL_BUTTON_COLOR); 
        cancelButton.setForeground(TEXT_COLOR_MAIN); 

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        buttonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPanel.add(buttonPanel);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(contentPanel, BorderLayout.NORTH);
        return wrapper;
    }
    
    private JPanel createFormField(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
        panel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("SansSerif", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR_MAIN);
        
        JPanel innerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        innerPanel.setOpaque(false);
        
        innerPanel.add(label);
        innerPanel.add(field);
        
        panel.add(innerPanel, BorderLayout.NORTH);
        return panel;
    }

    private JPanel createTurfButtonBox(Turf turf) {
        JPanel box = new JPanel(new BorderLayout());
        box.setBackground(TURF_BOX_BACKGROUND); 
        
        box.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel turfLabel = new JLabel(turf.name);
        turfLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        turfLabel.setForeground(TEXT_COLOR_MAIN); 
        box.add(turfLabel, BorderLayout.WEST);

        JLabel arrow = new JLabel(" → ");
        arrow.setFont(new Font("SansSerif", Font.BOLD, 18));
        arrow.setForeground(ACCENT_COLOR); 
        box.add(arrow, BorderLayout.EAST);
        
        box.setCursor(new Cursor(Cursor.HAND_CURSOR));
        box.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        box.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showTurfDetails(turf);
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                box.setBackground(new Color(235, 235, 235)); 
            }
            @Override
            public void mouseExited(MouseEvent e) {
                box.setBackground(TURF_BOX_BACKGROUND);
            }
        });
        
        return box;
    }
    
    private void showTurfDetails(Turf turf) {
        updateDetailPanelContent(turf);
        cardLayout.show(cardPanel, DETAIL_VIEW);
    }
    
    private void updateDetailPanelContent(Turf turf) {
        int bookings = calculateBookings(turf.id);
        double income = calculateIncome(turf.id);
        
        detailViewPanel.removeAll();
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); 
        mainPanel.setOpaque(false);
        
        JLabel titleLabel = new JLabel(turf.name + " Performance");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(ACCENT_COLOR);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20)); 

        JPanel metricPanel = new JPanel(new GridLayout(2, 1, 0, 15)); 
        metricPanel.setOpaque(false);
        
        metricPanel.add(createMetricCard("Total Bookings", String.valueOf(bookings), ACCENT_COLOR)); 
        metricPanel.add(createMetricCard("Total Income", String.format("Rs. %.2f", income), ACCENT_COLOR)); 

        mainPanel.add(metricPanel);
        mainPanel.add(Box.createVerticalStrut(30));

        JButton backButton = new JButton("← Back to All Turfs");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.addActionListener(e -> cardLayout.show(cardPanel, LANDING_VIEW));
        
        backButton.setBackground(NEUTRAL_BUTTON_COLOR); 
        backButton.setForeground(TEXT_COLOR_MAIN);
        backButton.setFocusPainted(false);
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        mainPanel.add(backButton);
        
        detailViewPanel.add(mainPanel, BorderLayout.NORTH);
        
        detailViewPanel.revalidate();
        detailViewPanel.repaint();
    }


    private JPanel createMetricCard(String title, String value, Color accentColor) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE); 
        
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.PLAIN, 12));
        titleLabel.setForeground(TEXT_COLOR_MAIN);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        valueLabel.setForeground(accentColor.darker()); 
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        card.add(titleLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(valueLabel);
        
        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TurfOwnerDashboard dashboard = new TurfOwnerDashboard();
            dashboard.setVisible(true);
        });
    }
}
