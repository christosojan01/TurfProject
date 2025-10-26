import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*; 

public class TurfHub extends JFrame {

    private final Color BLUE = new Color(0x123499); 
    private final Color DARK_TEXT = new Color(50, 50, 50);
    private final Color WHITE = Color.WHITE;
    private final Color GRAY_BG = new Color(245, 245, 245);

    private JPanel turfDisplayPanel; 

    public TurfHub() { this("Football"); }

    public TurfHub(String category) {
        super("Turf Booking App - " + category);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 700); 
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout());

        JScrollPane scrollPane = createHubContent(); 
        add(scrollPane, BorderLayout.CENTER); 
        setVisible(true);
        displayTurfsForCategory(category); 
    }
    
    private JScrollPane createHubContent() {
        JPanel container = new JPanel(new FlowLayout(FlowLayout.CENTER));
        container.setBackground(GRAY_BG);
        
        turfDisplayPanel = new JPanel(); 
        turfDisplayPanel.setLayout(new BoxLayout(turfDisplayPanel, BoxLayout.Y_AXIS));
        turfDisplayPanel.setBackground(GRAY_BG);
        turfDisplayPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        container.add(turfDisplayPanel); 
        
        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        return scrollPane;
    }

    private void displayTurfsForCategory(String category) {
        turfDisplayPanel.removeAll(); 

        List<TurfModel> turfs = getSimulatedTurfsByCategory(category + "Turfs");
        turfs = turfs.subList(0, Math.min(turfs.size(), 3)); 

        if (turfs.isEmpty()) {
            turfDisplayPanel.add(new JLabel("No " + category + " turfs found."));
        } else {
            JLabel title = new JLabel(category + " Turfs");
            title.setFont(new Font("Segoe UI", Font.BOLD, 20));
            title.setForeground(DARK_TEXT);
            title.setAlignmentX(Component.LEFT_ALIGNMENT); 
            turfDisplayPanel.add(title);
            turfDisplayPanel.add(Box.createVerticalStrut(10)); 

            for (TurfModel turf : turfs) {
                turfDisplayPanel.add(createTurfCard(
                    turf.getName(), turf.getAddress(), turf.getOperatingHours(), 
                    turf.getPricePerHour(), turf.getImagePath() 
                ));
                turfDisplayPanel.add(Box.createVerticalStrut(15));
            }
        }
        turfDisplayPanel.revalidate();
        turfDisplayPanel.repaint();
    }
    
    private JPanel createTurfCard(String name, String address, String hours, int price, String imagePath) { 
        JPanel card = new JPanel(new BorderLayout(0, 0)); 
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1)); 
        card.setPreferredSize(new Dimension(750, 120)); 

        JLabel details = new JLabel("<html><b>" + name + "</b><br>" + address + "<br>" + hours + "</html>");
        details.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        details.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 0)); 
        card.add(details, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new BorderLayout()); 
        actionPanel.setBackground(WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 15)); 

        JLabel priceLabel = new JLabel("Price: ₹" + price + ".00/hour", SwingConstants.RIGHT); 
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(DARK_TEXT); 
        actionPanel.add(priceLabel, BorderLayout.NORTH); 
        
        JButton bookButton = new JButton("Book Now");
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookButton.setBackground(DARK_TEXT); 
        bookButton.setForeground(WHITE);
        bookButton.setFocusPainted(false);
        bookButton.addActionListener(e -> handleBookNow()); 
        actionPanel.add(bookButton, BorderLayout.SOUTH); 
        
        card.add(actionPanel, BorderLayout.EAST);
        return card;
    }

    private void handleBookNow() {
        if (!SessionManager.isLoggedIn()) {
             JOptionPane.showMessageDialog(this, "Please log in to book a slot.", "Session Expired", JOptionPane.ERROR_MESSAGE);
             new AuthPage();
             this.dispose();
             return;
        }
        new BookingPage(); 
    }
    
    private List<TurfModel> getSimulatedTurfsByCategory(String categoryIdentifier) {
        List<TurfModel> turfs = new ArrayList<>();
        if ("FootballTurfs".equals(categoryIdentifier)) {
            turfs.add(new TurfModel(1, "Football Field A", "123 Street, City", 800, "08:00-22:00", "Football", "images/football1.jpg"));
            turfs.add(new TurfModel(2, "Soccer Dome", "456 Avenue, Town", 1000, "09:00-23:00", "Football", "images/football2.jpg"));
            turfs.add(new TurfModel(3, "Kick Off Grounds", "789 Road, Village", 750, "07:00-21:00", "Football", "images/football3.jpg"));
        } 
        return turfs;
    }

    private void handleLogout() {
        SessionManager.logout(); 
        this.dispose(); 
        new AuthPage(); 
        JOptionPane.showMessageDialog(null, "Logged out successfully.", "Logout", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new TurfHub("Football"));
    }
}
