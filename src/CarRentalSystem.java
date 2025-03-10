import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.text.DecimalFormat;

public class CarRentalSystem {
    private static JFrame rentCarFrame;
    private static JFrame removeCarFrame;
    private static JComboBox<String> carComboBox;
    private static ModernTable carTable;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Database.createTable();
        showLoginWindow();
    }

    private static void setupFrame(JFrame frame) {
        frame.getContentPane().setBackground(new Color(245, 245, 245));
        ((JPanel)frame.getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    private static void refreshAllWindows() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if (rentCarFrame != null && rentCarFrame.isVisible()) {
                    updateCarComboBox(carComboBox);
                }
                if (removeCarFrame != null && removeCarFrame.isVisible()) {
                    updateCarTable(carTable);
                }
            }
        });
    }

    private static void updateCarComboBox(JComboBox<String> comboBox) {
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) comboBox.getModel();
        model.removeAllElements();
        List<Database.CarInfo> availableCars = Database.getAvailableCars();
        for (Database.CarInfo car : availableCars) {
            model.addElement(car.carInfo);
        }
    }

    private static void showLoginWindow() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setSize(300, 200);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupFrame(loginFrame);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2, 10, 10));
        loginPanel.setBackground(new Color(245, 245, 245));

        JLabel usernameLabel = new JLabel("Username:");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        ModernButton loginButton = new ModernButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = new String(passwordField.getPassword());

                if (username.equals("client") && password.equals("client")) {
                    showClientMenu();
                    loginFrame.dispose();
                } else if (username.equals("admin") && password.equals("admin")) {
                    showAdminMenu();
                    loginFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Username sau parolă incorecte.");
                }
            }
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(new JLabel());
        loginPanel.add(loginButton);

        loginFrame.add(loginPanel);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }

    private static void showAdminMenu() {
        JFrame adminFrame = new JFrame("Meniu Admin");
        adminFrame.setSize(400, 300);
        adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupFrame(adminFrame);

        JPanel adminPanel = new JPanel();
        adminPanel.setLayout(new GridLayout(4, 1, 10, 10));
        adminPanel.setBackground(new Color(245, 245, 245));

        ModernButton manageCarsButton = new ModernButton("Gestionare Mașini");
        manageCarsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showManageCarsMenu(adminFrame);
            }
        });

        ModernButton manageRentalsButton = new ModernButton("Gestionare Închirieri");
        manageRentalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRentalsTable(adminFrame);
            }
        });

        ModernButton logoutButton = new ModernButton("Deconectare");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setHoverColor(new Color(192, 57, 43));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adminFrame.dispose();
                showLoginWindow();
            }
        });

        adminPanel.add(manageCarsButton);
        adminPanel.add(manageRentalsButton);
        adminPanel.add(logoutButton);

        adminFrame.add(adminPanel);
        adminFrame.setLocationRelativeTo(null);
        adminFrame.setVisible(true);
    }

    private static void showManageCarsMenu(JFrame parentFrame) {
        JFrame manageCarsFrame = new JFrame("Gestionare Mașini");
        manageCarsFrame.setSize(500, 400);
        manageCarsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setupFrame(manageCarsFrame);

        JPanel manageCarsPanel = new JPanel();
        manageCarsPanel.setLayout(new GridLayout(4, 1, 10, 10));
        manageCarsPanel.setBackground(new Color(245, 245, 245));

        ModernButton addCarButton = new ModernButton("Adaugă Mașină");
        addCarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddCarForm();
            }
        });

        ModernButton removeCarButton = new ModernButton("Elimină Mașină");
        removeCarButton.setBackground(new Color(231, 76, 60));
        removeCarButton.setHoverColor(new Color(192, 57, 43));
        removeCarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRemoveCarTable();
            }
        });

        ModernButton backButton = new ModernButton("Înapoi");
        backButton.setBackground(new Color(149, 165, 166));
        backButton.setHoverColor(new Color(127, 140, 141));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manageCarsFrame.dispose();
                parentFrame.setVisible(true);
            }
        });

        manageCarsPanel.add(addCarButton);
        manageCarsPanel.add(removeCarButton);
        manageCarsPanel.add(backButton);

        manageCarsFrame.add(manageCarsPanel);
        manageCarsFrame.setLocationRelativeTo(null);
        manageCarsFrame.setVisible(true);
        parentFrame.setVisible(false);
    }

    private static void showRentalsTable(JFrame parentFrame) {
        JFrame rentalsFrame = new JFrame("Gestionare Închirieri");
        rentalsFrame.setSize(800, 400);
        rentalsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setupFrame(rentalsFrame);

        JPanel rentalsPanel = new JPanel();
        rentalsPanel.setLayout(new BorderLayout(10, 10));
        rentalsPanel.setBackground(new Color(245, 245, 245));

        String[] columnNames = {"ID", "Nume Client", "Telefon", "Mașină", "Perioada (zile)", "Cost Total (RON)"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        ModernTable rentalsTable = new ModernTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(rentalsTable);

        try (Connection conn = DriverManager.getConnection(Database.URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM rentals")) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("customer_name"),
                        rs.getString("phone"),
                        rs.getString("car_info"),
                        rs.getInt("rental_period"),
                        df.format(rs.getDouble("total_cost"))
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        ModernButton deleteButton = new ModernButton("Șterge Închirierea");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setHoverColor(new Color(192, 57, 43));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = rentalsTable.getSelectedRow();
                if (selectedRow != -1) {
                    int rentalId = (int) rentalsTable.getValueAt(selectedRow, 0);
                    Database.removeRental(rentalId);
                    tableModel.removeRow(selectedRow);
                    refreshAllWindows();
                } else {
                    JOptionPane.showMessageDialog(rentalsFrame,
                            "Selectați o închiriere pentru ștergere!");
                }
            }
        });

        ModernButton backButton = new ModernButton("Înapoi");
        backButton.setBackground(new Color(149, 165, 166));
        backButton.setHoverColor(new Color(127, 140, 141));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rentalsFrame.dispose();
                parentFrame.setVisible(true);
            }
        });

        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);

        rentalsPanel.add(scrollPane, BorderLayout.CENTER);
        rentalsPanel.add(buttonPanel, BorderLayout.SOUTH);

        rentalsFrame.add(rentalsPanel);
        rentalsFrame.setLocationRelativeTo(null);
        rentalsFrame.setVisible(true);
        parentFrame.setVisible(false);
    }

    private static void showRemoveCarTable() {
        removeCarFrame = new JFrame("Eliminare Mașină");
        removeCarFrame.setSize(600, 400);
        removeCarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setupFrame(removeCarFrame);

        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout(10, 10));
        tablePanel.setBackground(new Color(245, 245, 245));

        carTable = new ModernTable(new DefaultTableModel());
        JScrollPane scrollPane = new JScrollPane(carTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        updateCarTable(carTable);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(new Color(245, 245, 245));

        ModernButton deleteButton = new ModernButton("Șterge Mașina Selectată");
        deleteButton.setBackground(new Color(231, 76, 60));
        deleteButton.setHoverColor(new Color(192, 57, 43));
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = carTable.getSelectedRow();
                if (selectedRow != -1) {
                    int carId = (int) carTable.getValueAt(selectedRow, 0);
                    Database.removeCar(carId);
                    updateCarTable(carTable);
                    refreshAllWindows();
                    JOptionPane.showMessageDialog(removeCarFrame, "Mașina a fost ștearsă!");
                } else {
                    JOptionPane.showMessageDialog(removeCarFrame, "Selectează o mașină din tabel.");
                }
            }
        });

        ModernButton backButton = new ModernButton("Înapoi");
        backButton.setBackground(new Color(149, 165, 166));
        backButton.setHoverColor(new Color(127, 140, 141));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeCarFrame.dispose();
            }
        });

        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        removeCarFrame.add(tablePanel);
        removeCarFrame.setLocationRelativeTo(null);
        removeCarFrame.setVisible(true);
    }

    private static void updateCarTable(JTable table) {
        String[] columnNames = {"ID", "Marca", "Model", "An", "Status", "Preț/zi (RON)"};
        DefaultTableModel carTableModel = new DefaultTableModel(columnNames, 0);

        try (Connection conn = DriverManager.getConnection(Database.URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM cars")) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getInt("year"),
                        rs.getString("status"),
                        df.format(rs.getDouble("price_per_day"))
                };
                carTableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        table.setModel(carTableModel);
    }

    private static void showAddCarForm() {
        JFrame addCarFrame = new JFrame("Adaugă Mașină");
        addCarFrame.setSize(400, 300);
        addCarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setupFrame(addCarFrame);

        JPanel addCarPanel = new JPanel();
        addCarPanel.setLayout(new GridLayout(7, 2, 10, 10));
        addCarPanel.setBackground(new Color(245, 245, 245));

        JLabel brandLabel = new JLabel("Marca:");
        JTextField brandField = new JTextField();

        JLabel modelLabel = new JLabel("Model:");
        JTextField modelField = new JTextField();

        JLabel yearLabel = new JLabel("An:");
        JTextField yearField = new JTextField();

        JLabel priceLabel = new JLabel("Preț/zi (RON):");
        JTextField priceField = new JTextField();
        JLabel statusLabel = new JLabel("Status:");
        JComboBox<String> statusComboBox = new JComboBox<>(new String[]{"disponibil", "indisponibil"});

        ModernButton submitButton = new ModernButton("Adaugă");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String brand = brandField.getText();
                    String model = modelField.getText();
                    int year = Integer.parseInt(yearField.getText());
                    double pricePerDay = Double.parseDouble(priceField.getText());
                    String status = (String) statusComboBox.getSelectedItem();

                    if (brand.isEmpty() || model.isEmpty()) {
                        JOptionPane.showMessageDialog(addCarFrame, "Toate câmpurile sunt obligatorii!");
                        return;
                    }

                    Database.addCar(brand, model, year, status, pricePerDay);
                    refreshAllWindows();
                    JOptionPane.showMessageDialog(addCarFrame, "Mașina a fost adăugată!");
                    addCarFrame.dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(addCarFrame, "Anul și prețul trebuie să fie numere!");
                }
            }
        });

        ModernButton backButton = new ModernButton("Înapoi");
        backButton.setBackground(new Color(149, 165, 166));
        backButton.setHoverColor(new Color(127, 140, 141));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addCarFrame.dispose();
            }
        });

        addCarPanel.add(brandLabel);
        addCarPanel.add(brandField);
        addCarPanel.add(modelLabel);
        addCarPanel.add(modelField);
        addCarPanel.add(yearLabel);
        addCarPanel.add(yearField);
        addCarPanel.add(priceLabel);
        addCarPanel.add(priceField);
        addCarPanel.add(statusLabel);
        addCarPanel.add(statusComboBox);
        addCarPanel.add(backButton);
        addCarPanel.add(submitButton);

        addCarFrame.add(addCarPanel);
        addCarFrame.setLocationRelativeTo(null);
        addCarFrame.setVisible(true);
    }

    private static void showClientMenu() {
        JFrame clientFrame = new JFrame("Meniu Client");
        clientFrame.setSize(400, 300);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setupFrame(clientFrame);

        JPanel clientPanel = new JPanel();
        clientPanel.setLayout(new GridLayout(4, 1, 10, 10));
        clientPanel.setBackground(new Color(245, 245, 245));

        ModernButton rentCarButton = new ModernButton("Închiriază o Mașină");
        rentCarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRentCarForm(clientFrame);
            }
        });

        ModernButton logoutButton = new ModernButton("Deconectare");
        logoutButton.setBackground(new Color(231, 76, 60));
        logoutButton.setHoverColor(new Color(192, 57, 43));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientFrame.dispose();
                showLoginWindow();
            }
        });

        clientPanel.add(rentCarButton);
        clientPanel.add(logoutButton);

        clientFrame.add(clientPanel);
        clientFrame.setLocationRelativeTo(null);
        clientFrame.setVisible(true);
    }

    private static void showRentCarForm(JFrame parentFrame) {
        rentCarFrame = new JFrame("Închiriere Mașină");
        rentCarFrame.setSize(400, 350);
        rentCarFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setupFrame(rentCarFrame);

        JPanel rentCarPanel = new JPanel();
        rentCarPanel.setLayout(new GridLayout(7, 2, 10, 10));
        rentCarPanel.setBackground(new Color(245, 245, 245));

        JLabel nameLabel = new JLabel("Nume:");
        JTextField nameField = new JTextField();

        JLabel phoneLabel = new JLabel("Nr. Telefon:");
        JTextField phoneField = new JTextField();

        JLabel carLabel = new JLabel("Mașină:");
        DefaultComboBoxModel<String> carModel = new DefaultComboBoxModel<>();
        carComboBox = new JComboBox<>(carModel);

        JLabel priceLabel = new JLabel("Preț/zi: 0 RON");
        JLabel periodLabel = new JLabel("Perioada (zile):");
        JTextField periodField = new JTextField();
        JLabel totalLabel = new JLabel("Cost total: 0 RON");

        List<Database.CarInfo> availableCars = Database.getAvailableCars();
        for (Database.CarInfo car : availableCars) {
            carModel.addElement(car.carInfo);
        }

        carComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedCar = (String) carComboBox.getSelectedItem();
                if (selectedCar != null) {
                    double price = Database.getCarPrice(selectedCar);
                    priceLabel.setText("Preț/zi: " + df.format(price) + " RON");

                    try {
                        String periodText = periodField.getText();
                        if (!periodText.isEmpty()) {
                            int days = Integer.parseInt(periodText);
                            double totalCost = price * days;
                            totalLabel.setText("Cost total: " + df.format(totalCost) + " RON");
                        }
                    } catch (NumberFormatException ex) {
                        // Ignorăm eroarea dacă perioada nu este un număr valid
                    }
                }
            }
        });

        periodField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updateTotal();
            }
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updateTotal();
            }
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updateTotal();
            }

            private void updateTotal() {
                try {
                    String selectedCar = (String) carComboBox.getSelectedItem();
                    if (selectedCar != null && !periodField.getText().isEmpty()) {
                        int days = Integer.parseInt(periodField.getText());
                        double pricePerDay = Database.getCarPrice(selectedCar);
                        double totalCost = days * pricePerDay;
                        totalLabel.setText("Cost total: " + df.format(totalCost) + " RON");
                    }
                } catch (NumberFormatException ex) {
                    totalLabel.setText("Cost total: 0 RON");
                }
            }
        });

        ModernButton submitButton = new ModernButton("Închiriază");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String phone = phoneField.getText();
                String selectedCar = (String) carComboBox.getSelectedItem();
                String period = periodField.getText();

                if (name.isEmpty() || phone.isEmpty() || period.isEmpty() || selectedCar == null) {
                    JOptionPane.showMessageDialog(rentCarFrame,
                            "Toate câmpurile sunt obligatorii!");
                    return;
                }

                if (!phone.matches("\\d{10}")) {
                    JOptionPane.showMessageDialog(rentCarFrame,
                            "Numărul de telefon trebuie să conțină exact 10 cifre!");
                    return;
                }

                try {
                    int days = Integer.parseInt(period);
                    if (days <= 0) {
                        JOptionPane.showMessageDialog(rentCarFrame,
                                "Perioada trebuie să fie un număr pozitiv de zile!");
                        return;
                    }

                    double pricePerDay = Database.getCarPrice(selectedCar);
                    double totalCost = days * pricePerDay;

                    Database.addRental(name, phone, selectedCar, days, totalCost);

                    JOptionPane.showMessageDialog(rentCarFrame,
                            "Închiriere înregistrată cu succes!\n" +
                                    "Nume: " + name + "\n" +
                                    "Telefon: " + phone + "\n" +
                                    "Mașină: " + selectedCar + "\n" +
                                    "Perioada: " + period + " zile\n" +
                                    "Cost total: " + df.format(totalCost) + " RON");

                    rentCarFrame.dispose();
                    parentFrame.setVisible(true);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(rentCarFrame,
                            "Perioada trebuie să fie un număr!");
                }
            }
        });

        ModernButton backButton = new ModernButton("Înapoi");
        backButton.setBackground(new Color(149, 165, 166));
        backButton.setHoverColor(new Color(127, 140, 141));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rentCarFrame.dispose();
                parentFrame.setVisible(true);
            }
        });

        rentCarPanel.add(nameLabel);
        rentCarPanel.add(nameField);
        rentCarPanel.add(phoneLabel);
        rentCarPanel.add(phoneField);
        rentCarPanel.add(carLabel);
        rentCarPanel.add(carComboBox);
        rentCarPanel.add(priceLabel);
        rentCarPanel.add(new JLabel());
        rentCarPanel.add(periodLabel);
        rentCarPanel.add(periodField);
        rentCarPanel.add(totalLabel);
        rentCarPanel.add(new JLabel());
        rentCarPanel.add(backButton);
        rentCarPanel.add(submitButton);

        rentCarFrame.add(rentCarPanel);
        rentCarFrame.setLocationRelativeTo(null);
        rentCarFrame.setVisible(true);
        parentFrame.setVisible(false);
    }
}