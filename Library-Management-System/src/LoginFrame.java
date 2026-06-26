import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Properties;

/**
 * LoginFrame - User authentication interface for the Library Management System
 */
public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JCheckBox rememberMeCheckBox;
    private JButton clearSavedButton;
    private JButton loginButton;
    private JButton registerButton;
    private JButton exitButton;
    private JLabel statusLabel;
    
    private LibraryManager libraryManager;
    private static final String LOGIN_CONFIG_FILE = "login_config.properties";
    
    public LoginFrame() {
        libraryManager = LibraryManager.getInstance();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        restoreLoginState(); // Restore previous login state
        
        setTitle("Library Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450); // Increased frame size for remember me checkbox
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }
    
    private void initializeComponents() {
        usernameField = new JTextField(25); // Increased column count
        usernameField.setPreferredSize(new Dimension(300, 35)); // Larger size
        passwordField = new JPasswordField(25); // Increased column count
        passwordField.setPreferredSize(new Dimension(300, 35)); // Larger size
        
        rememberMeCheckBox = new JCheckBox("Remember Me");
        rememberMeCheckBox.setFont(new Font("Arial", Font.PLAIN, 12));
        
        loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(120, 40)); // Larger button
        
        registerButton = new JButton("Register");
        registerButton.setPreferredSize(new Dimension(120, 40)); // Larger button
        registerButton.setBackground(new Color(0, 153, 0));
        registerButton.setForeground(Color.BLACK); // Changed from WHITE to BLACK
        registerButton.setFocusPainted(false);
        
        exitButton = new JButton("Exit");
        exitButton.setPreferredSize(new Dimension(120, 40)); // Larger button
        statusLabel = new JLabel("Please enter your credentials");
        statusLabel.setForeground(Color.BLUE);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12)); // Larger font
        
        // Set focus to username field
        // Note: setNextFocusableComponent is deprecated, using focus traversal instead
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Main panel with GridBagLayout for better control
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // Add padding
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15); // Increased spacing
        
        // Title
        JLabel titleLabel = new JLabel("Library Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Larger title font
        titleLabel.setForeground(new Color(0, 102, 204));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(titleLabel, gbc);
        
        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Larger label font
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(usernameLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
        mainPanel.add(usernameField, gbc);
        
        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14)); // Larger label font
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE; // Reset fill
        mainPanel.add(passwordLabel, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Fill horizontally
        mainPanel.add(passwordField, gbc);

        // Remember Me checkbox and Clear Saved Login button
        JPanel rememberPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        rememberPanel.add(rememberMeCheckBox);
        
        clearSavedButton = new JButton("Clear Saved Login");
        clearSavedButton.setFont(new Font("Arial", Font.PLAIN, 10));
        clearSavedButton.setPreferredSize(new Dimension(120, 25));
        clearSavedButton.setEnabled(hasSavedCredentials()); // Only enable if there are saved credentials
        clearSavedButton.addActionListener(e -> {
            deleteLoginState();
            usernameField.setText("");
            passwordField.setText("");
            rememberMeCheckBox.setSelected(false);
            clearSavedButton.setEnabled(false);
            statusLabel.setText("Saved login credentials cleared");
            statusLabel.setForeground(Color.BLUE);
        });
        rememberPanel.add(clearSavedButton);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; // Reset fill
        mainPanel.add(rememberPanel, gbc);
        
        // Status label
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; // Reset fill
        mainPanel.add(statusLabel, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(loginButton);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(registerButton);
        buttonPanel.add(Box.createHorizontalStrut(15));
        buttonPanel.add(exitButton);
        buttonPanel.add(Box.createHorizontalGlue());
        
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        mainPanel.add(buttonPanel, gbc);
        
        // Help text
        JLabel helpLabel = new JLabel("New users? Click 'Register' to create an account");
        helpLabel.setForeground(new Color(100, 100, 100));
        helpLabel.setFont(new Font("Arial", Font.ITALIC, 12)); // Larger help text font
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(helpLabel, gbc);
        
        // Add main panel to frame
        add(mainPanel, BorderLayout.CENTER);
        
        // Add some padding at the bottom
        add(Box.createVerticalStrut(30), BorderLayout.SOUTH); // Increased bottom padding
    }
    
    private void setupEventHandlers() {
        // Login button action
        loginButton.addActionListener(e -> performLogin());
        
        // Register button action
        registerButton.addActionListener(e -> showRegistrationDialog());
        
        // Enter key in password field
        passwordField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performLogin();
                }
            }
        });
        
        // Keyboard shortcuts
        getRootPane().registerKeyboardAction(
            e -> showRegistrationDialog(),
            KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK),
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
        
        // Exit button
        exitButton.addActionListener(e -> System.exit(0));
        
        // Window focus listener to clear status
        addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                statusLabel.setText("Please enter your credentials");
                statusLabel.setForeground(Color.BLUE);
            }
        });
    }
    
    private void performLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Please enter both username and password");
            statusLabel.setForeground(Color.RED);
            return;
        }
        
        // Attempt authentication
        User user = libraryManager.authenticateUser(username, password);
        
        if (user != null) {
            statusLabel.setText("Login successful! Opening dashboard...");
            statusLabel.setForeground(Color.GREEN);
            
            // Clear sensitive fields
            usernameField.setText("");
            passwordField.setText("");
            
            // Save login state if "Remember Me" is checked
            if (rememberMeCheckBox.isSelected()) {
                saveLoginState(username, password);
            } else {
                deleteLoginState();
            }

            // Open appropriate dashboard based on user role
            SwingUtilities.invokeLater(() -> {
                openDashboard(user);
                dispose(); // Close login frame
            });
        } else {
            statusLabel.setText("Invalid username or password");
            statusLabel.setForeground(Color.RED);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    private void restoreLoginState() {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(LOGIN_CONFIG_FILE);
            props.load(fis);
            fis.close();

            String savedUsername = props.getProperty("username");
            String savedPassword = props.getProperty("password");
            boolean rememberMe = Boolean.parseBoolean(props.getProperty("rememberMe", "false"));

            if (savedUsername != null && savedPassword != null && rememberMe) {
                usernameField.setText(savedUsername);
                passwordField.setText(savedPassword);
                rememberMeCheckBox.setSelected(true);
                clearSavedButton.setEnabled(true);
                statusLabel.setText("Saved credentials restored. Click Login to continue.");
                statusLabel.setForeground(Color.GREEN);
            }
        } catch (IOException e) {
            // File doesn't exist yet, which is normal for first-time users
        }
    }

    private void saveLoginState(String username, String password) {
        try {
            Properties props = new Properties();
            
            // Try to load existing properties, but don't fail if file doesn't exist
            try {
                FileInputStream fis = new FileInputStream(LOGIN_CONFIG_FILE);
                props.load(fis);
                fis.close();
            } catch (IOException e) {
                // File doesn't exist yet, start with empty properties
            }

            props.setProperty("username", username);
            props.setProperty("password", password);
            props.setProperty("rememberMe", "true");

            FileOutputStream fos = new FileOutputStream(LOGIN_CONFIG_FILE);
            props.store(fos, "Library Management System Login State");
            fos.close();
            
            // Enable the clear saved login button
            clearSavedButton.setEnabled(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteLoginState() {
        File file = new File(LOGIN_CONFIG_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
    
    private boolean hasSavedCredentials() {
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream(LOGIN_CONFIG_FILE);
            props.load(fis);
            fis.close();
            
            String savedUsername = props.getProperty("username");
            String savedPassword = props.getProperty("password");
            boolean rememberMe = Boolean.parseBoolean(props.getProperty("rememberMe", "false"));
            
            return savedUsername != null && savedPassword != null && rememberMe;
        } catch (IOException e) {
            return false;
        }
    }
    
    private void openDashboard(User user) {
        switch (user.getRole()) {
            case ADMIN:
                new AdminDashboard(user);
                break;
            case LIBRARIAN:
                new LibrarianDashboard(user);
                break;
            case MEMBER:
                new MemberDashboard(user);
                break;
        }
    }
    
    private void showRegistrationDialog() {
        JDialog dialog = new JDialog(this, "New Member Registration", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 500);
        dialog.setLocationRelativeTo(this);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form fields
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);
        JTextField fullNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        
        // Add form components
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Confirm Password:"), gbc);
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(fullNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        // Terms and conditions checkbox
        JCheckBox termsCheckBox = new JCheckBox("I agree to the Library Terms and Conditions");
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(termsCheckBox, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton createAccountButton = new JButton("Create Account");
        JButton cancelButton = new JButton("Cancel");
        
        createAccountButton.addActionListener(e -> {
            // Validate input
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            
            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || 
                fullName.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, 
                    "All fields are required", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.length() < 3) {
                JOptionPane.showMessageDialog(dialog, 
                    "Password must be at least 3 characters long", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog, 
                    "Passwords do not match", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (!termsCheckBox.isSelected()) {
                JOptionPane.showMessageDialog(dialog, 
                    "You must agree to the terms and conditions", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if username already exists
            if (libraryManager.getUserByUsername(username) != null) {
                JOptionPane.showMessageDialog(dialog, 
                    "Username already exists. Please choose a different username.", 
                    "Registration Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Create new user
            User newUser = libraryManager.addUser(username, password, fullName, email, User.UserRole.MEMBER);
            
            if (newUser != null) {
                JOptionPane.showMessageDialog(dialog,
                    "Registration successful!\n\n" +
                    "Welcome to the Library Management System!\n" +
                    "You can now login with your new account.",
                    "Registration Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                
                dialog.dispose();
                
                // Clear login fields and show success message
                usernameField.setText("");
                passwordField.setText("");
                statusLabel.setText("Registration successful! You can now login.");
                statusLabel.setForeground(Color.GREEN);
            } else {
                JOptionPane.showMessageDialog(dialog,
                    "Registration failed. Please try again or contact support.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(createAccountButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
}
