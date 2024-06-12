import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MedicineReminderApp {
    private JFrame frame;
    private JPanel loginPanel, medicinePanel;
    private JTextField usernameField, medNameField, medDosageField, medFrequencyField;
    private Map<String, ArrayList<Map<String, String>>> users;
    private String currentUser;

    public MedicineReminderApp() {
        frame = new JFrame("Medicine Reminder App");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        users = new HashMap<>();
        currentUser = null;

        createLoginPanel();
        frame.setVisible(true);
    }

    private void createLoginPanel() {
        frame.getContentPane().removeAll();

        loginPanel = new JPanel(new GridLayout(3, 2));
        frame.add(loginPanel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("Medicine Reminder App", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Helvetica", Font.BOLD, 20));
        loginPanel.add(titleLabel);

        JLabel usernameLabel = new JLabel("Username:");
        loginPanel.add(usernameLabel);

        usernameField = new JTextField();
        loginPanel.add(usernameField);

        JButton loginButton = new JButton("Login / Register");
        loginButton.setBackground(new Color(76, 175, 80));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginOrRegister();
            }
        });
        loginPanel.add(loginButton);

        frame.revalidate();
        frame.repaint();
    }

    private void loginOrRegister() {
        String username = usernameField.getText().trim();
        if (users.containsKey(username)) {
            currentUser = username;
            switchToMedicinePanel();
        } else {
            int confirmRegister = JOptionPane.showConfirmDialog(null, "User not found. Register as a new user?", "Registration", JOptionPane.YES_NO_OPTION);
            if (confirmRegister == JOptionPane.YES_OPTION) {
                registerUser(username);
                switchToMedicinePanel();
            }
        }
    }

    private void registerUser(String username) {
        users.put(username, new ArrayList<>());
        currentUser = username;
    }

    private void switchToMedicinePanel() {
        frame.getContentPane().removeAll();
        createMedicinePanel();
        frame.revalidate();
        frame.repaint();
    }

    private void createMedicinePanel() {
        medicinePanel = new JPanel(new BorderLayout());
        frame.add(medicinePanel, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        medicinePanel.add(inputPanel, BorderLayout.NORTH);

        JLabel medNameLabel = new JLabel("Medicine Name:");
        inputPanel.add(medNameLabel);

        medNameField = new JTextField();
        inputPanel.add(medNameField);

        JLabel medDosageLabel = new JLabel("Dosage:");
        inputPanel.add(medDosageLabel);

        medDosageField = new JTextField();
        inputPanel.add(medDosageField);

        JLabel medFrequencyLabel = new JLabel("Frequency (minutes):");
        inputPanel.add(medFrequencyLabel);

        medFrequencyField = new JTextField();
        inputPanel.add(medFrequencyField);

        JButton addMedButton = new JButton("Add Medicine");
        addMedButton.setBackground(new Color(76, 175, 80));
        addMedButton.setForeground(Color.WHITE);
        addMedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMedicine();
            }
        });
        inputPanel.add(addMedButton);

        JButton displayButton = new JButton("Display Medicines");
        displayButton.setBackground(new Color(33, 150, 243));
        displayButton.setForeground(Color.WHITE);
        displayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayMedicines();
            }
        });
        inputPanel.add(displayButton);

        DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("Medicines");
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        JTree tree = new JTree(treeModel);
        medicinePanel.add(new JScrollPane(tree), BorderLayout.CENTER);
    }

    private void addMedicine() {
        String medName = medNameField.getText().trim();
        String medDosage = medDosageField.getText().trim();
        String medFrequency = medFrequencyField.getText().trim();

        if (!medName.isEmpty() && !medDosage.isEmpty() && !medFrequency.isEmpty()) {
            Map<String, String> medicine = new HashMap<>();
            medicine.put("name", medName);
            medicine.put("dosage", medDosage);
            medicine.put("frequency", medFrequency);
            users.get(currentUser).add(medicine);
            JOptionPane.showMessageDialog(null, "Medicine added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            saveToCSV();
            clearMedicineFields();
            // Schedule medicine reminder and update UI here
        } else {
            JOptionPane.showMessageDialog(null, "Please enter medicine information.", "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void saveToCSV() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("medicine_data.csv"));
            for (Map.Entry<String, ArrayList<Map<String, String>>> entry : users.entrySet()) {
                for (Map<String, String> medicine : entry.getValue()) {
                    writer.write(entry.getKey() + "," + medicine.get("name") + "," + medicine.get("dosage") + "," + medicine.get("frequency") + "\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayMedicines() {
        // Update tree view with medicines
    }

    private void clearMedicineFields() {
        medNameField.setText("");
        medDosageField.setText("");
        medFrequencyField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MedicineReminderApp();
            }
        });
    }
}