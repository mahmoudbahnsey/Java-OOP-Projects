import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * AdminDashboard - Main interface for administrators in the Library Management System
 */
public class AdminDashboard extends JFrame {
    private User currentUser;
    private LibraryManager libraryManager;
    
    // Main components
    private JTabbedPane tabbedPane;
    private JLabel welcomeLabel;
    private JButton logoutButton;
    
    // Book management components
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JButton addBookButton;
    private JButton editBookButton;
    private JButton deleteBookButton;
    private JTextField bookSearchField;
    
    // User management components
    private JTable userTable;
    private DefaultTableModel userTableModel;
    private JButton addUserButton;
    private JButton editUserButton;
    private JButton deleteUserButton;
    
    // Statistics components
    private JLabel totalBooksLabel;
    private JLabel totalUsersLabel;
    private JLabel totalTransactionsLabel;
    private JLabel availableBooksLabel;
    private JLabel overdueBooksLabel;
    private JLabel totalFinesLabel;
    
    public AdminDashboard(User user) {
        this.currentUser = user;
        this.libraryManager = LibraryManager.getInstance();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
        
        setTitle("Library Management System - Admin Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Welcome and logout
        welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + " (Administrator)");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton = new JButton("Logout");
        
        // Tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Book management tab
        setupBookManagementTab();
        
        // User management tab
        setupUserManagementTab();
        
        // Statistics tab
        setupStatisticsTab();
    }
    
    private void setupBookManagementTab() {
        JPanel bookPanel = new JPanel(new BorderLayout());
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Books:"));
        bookSearchField = new JTextField(20);
        searchPanel.add(bookSearchField);
        JButton searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        JButton clearSearchButton = new JButton("Clear");
        searchPanel.add(clearSearchButton);
        
        // Table
        String[] bookColumns = {"ID", "Title", "Author", "Genre", "ISBN", "Year", "Publisher", "Available", "Total", "Status"};
        bookTableModel = new DefaultTableModel(bookColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        bookTable = new JTable(bookTableModel);
        JScrollPane bookScrollPane = new JScrollPane(bookTable);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addBookButton = new JButton("Add Book");
        editBookButton = new JButton("Edit Book");
        deleteBookButton = new JButton("Delete Book");
        buttonPanel.add(addBookButton);
        buttonPanel.add(editBookButton);
        buttonPanel.add(deleteBookButton);
        
        JButton refreshBooksButton = new JButton("Refresh Books");
        refreshBooksButton.setBackground(new Color(40, 167, 69));
        refreshBooksButton.setForeground(Color.WHITE);
        refreshBooksButton.addActionListener(e -> {
            System.out.println("Admin Dashboard: Refresh Books button clicked");
            loadBooks();
            updateStatistics();
            JOptionPane.showMessageDialog(this, 
                "Book availability refreshed from database!\n" +
                "All total copies and available copies are now current.",
                "Books Refreshed", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(refreshBooksButton);
        
        bookPanel.add(searchPanel, BorderLayout.NORTH);
        bookPanel.add(bookScrollPane, BorderLayout.CENTER);
        bookPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Book Management", bookPanel);
        
        // Search functionality
        searchButton.addActionListener(e -> searchBooks());
        clearSearchButton.addActionListener(e -> {
            bookSearchField.setText("");
            loadBooks();
        });
    }
    
    private void setupUserManagementTab() {
        JPanel userPanel = new JPanel(new BorderLayout());
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Users:"));
        JTextField userSearchField = new JTextField(20);
        searchPanel.add(userSearchField);
        JButton searchUsersButton = new JButton("Search");
        searchPanel.add(searchUsersButton);
        JButton clearUserSearchButton = new JButton("Clear");
        searchPanel.add(clearUserSearchButton);
        
        // Table
        String[] userColumns = {"ID", "Username", "Full Name", "Email", "Role", "Book Capacity", "Status"};
        userTableModel = new DefaultTableModel(userColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        userTable = new JTable(userTableModel);
        JScrollPane userScrollPane = new JScrollPane(userTable);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addUserButton = new JButton("Add User");
        editUserButton = new JButton("Edit User");
        deleteUserButton = new JButton("Delete User");
        
        JButton refreshUsersButton = new JButton("Refresh Users");
        refreshUsersButton.setBackground(new Color(40, 167, 69));
        refreshUsersButton.setForeground(Color.WHITE);
        refreshUsersButton.addActionListener(e -> {
            System.out.println("Admin Dashboard: Refresh Users button clicked");
            loadUsers();
            updateStatistics();
            JOptionPane.showMessageDialog(this, 
                "User data refreshed from database!\n" +
                "All user information is now current.",
                "Users Refreshed", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(refreshUsersButton);
        
        userPanel.add(searchPanel, BorderLayout.NORTH);
        userPanel.add(userScrollPane, BorderLayout.CENTER);
        userPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("User Management", userPanel);
        
        // Search functionality
        searchUsersButton.addActionListener(e -> searchUsers(userSearchField.getText().trim()));
        clearUserSearchButton.addActionListener(e -> {
            userSearchField.setText("");
            loadUsers();
        });
    }
    
    private void setupStatisticsTab() {
        JPanel statsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Statistics labels
        totalBooksLabel = new JLabel("Total Books: 0");
        totalUsersLabel = new JLabel("Total Users: 0");
        totalTransactionsLabel = new JLabel("Total Transactions: 0");
        availableBooksLabel = new JLabel("Available Books: 0");
        overdueBooksLabel = new JLabel("Overdue Books: 0");
        totalFinesLabel = new JLabel("Total Fines: $0.00");
        
        // Style the labels
        Font statsFont = new Font("Arial", Font.BOLD, 14);
        totalBooksLabel.setFont(statsFont);
        totalUsersLabel.setFont(statsFont);
        totalTransactionsLabel.setFont(statsFont);
        availableBooksLabel.setFont(statsFont);
        overdueBooksLabel.setFont(statsFont);
        totalFinesLabel.setFont(statsFont);
        
        // Add labels to panel
        gbc.gridx = 0; gbc.gridy = 0;
        statsPanel.add(totalBooksLabel, gbc);
        gbc.gridy = 1;
        statsPanel.add(totalUsersLabel, gbc);
        gbc.gridy = 2;
        statsPanel.add(totalTransactionsLabel, gbc);
        gbc.gridy = 3;
        statsPanel.add(availableBooksLabel, gbc);
        gbc.gridy = 4;
        statsPanel.add(overdueBooksLabel, gbc);
        gbc.gridy = 5;
        statsPanel.add(totalFinesLabel, gbc);
        
        tabbedPane.addTab("Statistics", statsPanel);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Left side - welcome and save button
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(welcomeLabel, BorderLayout.NORTH);
        
        JButton saveDataButton = new JButton("Save Data");
        saveDataButton.setBackground(new Color(0, 153, 0));
        saveDataButton.setForeground(Color.WHITE);
        saveDataButton.addActionListener(e -> {
            libraryManager.saveData();
            JOptionPane.showMessageDialog(this, "Data saved successfully!", "Save Complete", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton autoSyncButton = new JButton("Auto Sync");
        autoSyncButton.setBackground(new Color(0, 123, 255));
        autoSyncButton.setForeground(Color.WHITE);
        autoSyncButton.addActionListener(e -> {
            libraryManager.autoSyncWithDatabase();
            loadData(); // Refresh the UI after sync
            JOptionPane.showMessageDialog(this, "Database automatically synchronized!", "Auto Sync Complete", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton refreshAllButton = new JButton("Refresh All");
        refreshAllButton.setBackground(new Color(255, 193, 7));
        refreshAllButton.setForeground(Color.BLACK);
        refreshAllButton.addActionListener(e -> {
            System.out.println("Admin Dashboard: Refresh All button clicked");
            loadData();
            JOptionPane.showMessageDialog(this, 
                "All data refreshed from database!\n" +
                "Books, users, transactions, and statistics are now current.",
                "All Data Refreshed", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buttonPanel.add(saveDataButton);
        buttonPanel.add(autoSyncButton);
        buttonPanel.add(refreshAllButton);
        
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        logoutButton.addActionListener(e -> logout());
        
        addBookButton.addActionListener(e -> showAddBookDialog());
        editBookButton.addActionListener(e -> editSelectedBook());
        deleteBookButton.addActionListener(e -> deleteSelectedBook());
        
        addUserButton.addActionListener(e -> showAddUserDialog());
        editUserButton.addActionListener(e -> editSelectedUser());
        deleteUserButton.addActionListener(e -> deleteSelectedUser());
        
        // Double-click to edit
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedBook();
                }
            }
        });
        
        userTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    editSelectedUser();
                }
            }
        });
    }
    
    private void loadData() {
        loadBooks();
        loadUsers();
        updateStatistics();
    }
    
    private void loadBooks() {
        System.out.println("=== ADMIN DASHBOARD: LOADING BOOKS FROM DATABASE ===");
        bookTableModel.setRowCount(0);
        
        // Get books directly from database instead of memory
        List<Book> books = libraryManager.getDatabaseManager().getAllBooks();
        System.out.println("Retrieved " + books.size() + " books from database");
        
        for (Book book : books) {
            System.out.println("Processing book: " + book.getBookId() + 
                             " | Title: " + book.getTitle() + 
                             " | Available: " + book.getAvailableCopies() + 
                             " | Total: " + book.getTotalCopies() + 
                             " | Borrowed: " + book.getBorrowedCopies());
            
            // Verify the mathematical relationship: total = available + borrowed
            int calculatedAvailable = book.getTotalCopies() - book.getBorrowedCopies();
            if (book.getAvailableCopies() != calculatedAvailable) {
                System.out.println("⚠️  WARNING: Book " + book.getBookId() + " availability mismatch!");
                System.out.println("   Database shows: Available=" + book.getAvailableCopies() + 
                                 ", Total=" + book.getTotalCopies() + 
                                 ", Borrowed=" + book.getBorrowedCopies());
                System.out.println("   Calculated available should be: " + calculatedAvailable);
            } else {
                System.out.println("✓ Book " + book.getBookId() + " availability is consistent");
            }
            
            Object[] row = {
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.getPublisher(),
                book.getAvailableCopies(),
                book.getTotalCopies(),
                book.isActive() ? "Active" : "Inactive"
            };
            bookTableModel.addRow(row);
        }
        
        // Force table refresh
        bookTableModel.fireTableDataChanged();
        bookTable.revalidate();
        bookTable.repaint();
        
        System.out.println("Admin dashboard books loaded. Table now has " + bookTableModel.getRowCount() + " rows");
        System.out.println("=== END ADMIN DASHBOARD BOOK LOADING ===");
    }
    
    private void searchBooks() {
        String searchTerm = bookSearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadBooks();
            return;
        }
        
        System.out.println("=== ADMIN DASHBOARD: SEARCHING BOOKS IN DATABASE ===");
        System.out.println("Search term: '" + searchTerm + "'");
        
        // Get books directly from database instead of memory
        List<Book> books = libraryManager.getDatabaseManager().searchBooks(searchTerm);
        System.out.println("Found " + books.size() + " matching books");
        
        bookTableModel.setRowCount(0);
        for (Book book : books) {
            System.out.println("Processing search result: " + book.getBookId() + 
                             " | Title: " + book.getTitle() + 
                             " | Available: " + book.getAvailableCopies() + 
                             " | Total: " + book.getTotalCopies() + 
                             " | Borrowed: " + book.getBorrowedCopies());
            
            // Verify the mathematical relationship: total = available + borrowed
            int calculatedAvailable = book.getTotalCopies() - book.getBorrowedCopies();
            if (book.getAvailableCopies() != calculatedAvailable) {
                System.out.println("⚠️  WARNING: Book " + book.getBookId() + " availability mismatch!");
                System.out.println("   Database shows: Available=" + book.getAvailableCopies() + 
                                 ", Total=" + book.getTotalCopies() + 
                                 ", Borrowed=" + book.getBorrowedCopies());
                System.out.println("   Calculated available should be: " + calculatedAvailable);
            } else {
                System.out.println("✓ Book " + book.getBookId() + " availability is consistent");
            }
            
            Object[] row = {
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.getPublisher(),
                book.getAvailableCopies(),
                book.getTotalCopies(),
                book.isActive() ? "Active" : "Inactive"
            };
            bookTableModel.addRow(row);
        }
        
        // Force table refresh
        bookTableModel.fireTableDataChanged();
        bookTable.revalidate();
        bookTable.repaint();
        
        System.out.println("Admin dashboard search completed. Table now has " + bookTableModel.getRowCount() + " rows");
        System.out.println("=== END ADMIN DASHBOARD BOOK SEARCH ===");
    }
    
    private void loadUsers() {
        System.out.println("=== ADMIN DASHBOARD: LOADING USERS FROM DATABASE ===");
        userTableModel.setRowCount(0);
        
        // Get users directly from database instead of memory
        List<User> users = libraryManager.getDatabaseManager().getAllUsers();
        System.out.println("Retrieved " + users.size() + " users from database");
        
        for (User user : users) {
            System.out.println("Processing user: " + user.getUserId() + 
                             " | Username: " + user.getUsername() + 
                             " | Role: " + user.getRole() + 
                             " | Capacity: " + (user.getRole() == User.UserRole.MEMBER ? user.getBookCapacity() : "N/A"));
            
            Object[] row = {
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().toString(),
                user.getRole() == User.UserRole.MEMBER ? user.getBookCapacity() : "N/A",
                user.isActive() ? "Active" : "Inactive"
            };
            userTableModel.addRow(row);
        }
        
        // Force table refresh
        userTableModel.fireTableDataChanged();
        userTable.revalidate();
        userTable.repaint();
        
        System.out.println("Admin dashboard users loaded. Table now has " + userTableModel.getRowCount() + " rows");
        System.out.println("=== END ADMIN DASHBOARD USER LOADING ===");
    }
    
    private void searchUsers(String searchTerm) {
        if (searchTerm.isEmpty()) {
            loadUsers();
            return;
        }

        System.out.println("=== ADMIN DASHBOARD: SEARCHING USERS IN DATABASE ===");
        System.out.println("Search term: '" + searchTerm + "'");

        // Get all users from database and filter by search term
        List<User> allUsers = libraryManager.getDatabaseManager().getAllUsers();
        List<User> matchingUsers = new ArrayList<>();
        
        for (User user : allUsers) {
            if (user.getUsername().toLowerCase().contains(searchTerm.toLowerCase()) ||
                user.getFullName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                user.getEmail().toLowerCase().contains(searchTerm.toLowerCase()) ||
                user.getRole().toString().toLowerCase().contains(searchTerm.toLowerCase())) {
                matchingUsers.add(user);
            }
        }
        
        System.out.println("Found " + matchingUsers.size() + " matching users out of " + allUsers.size() + " total");

        userTableModel.setRowCount(0);
        for (User user : matchingUsers) {
            System.out.println("Processing search result: " + user.getUserId() + 
                             " | Username: " + user.getUsername() + 
                             " | Role: " + user.getRole() + 
                             " | Capacity: " + (user.getRole() == User.UserRole.MEMBER ? user.getBookCapacity() : "N/A"));
            
            Object[] row = {
                user.getUserId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().toString(),
                user.getRole() == User.UserRole.MEMBER ? user.getBookCapacity() : "N/A",
                user.isActive() ? "Active" : "Inactive"
            };
            userTableModel.addRow(row);
        }

        userTableModel.fireTableDataChanged();
        userTable.revalidate();
        userTable.repaint();

        System.out.println("Admin dashboard user search completed. Table now has " + userTableModel.getRowCount() + " rows");
        System.out.println("=== END ADMIN DASHBOARD USER SEARCH ===");
    }
    
    private void updateStatistics() {
        System.out.println("=== ADMIN DASHBOARD: UPDATING STATISTICS FROM DATABASE ===");
        
        // Get fresh data directly from database
        int totalBooks = libraryManager.getDatabaseManager().getAllBooks().size();
        int totalUsers = libraryManager.getDatabaseManager().getAllUsers().size();
        int totalTransactions = libraryManager.getDatabaseManager().getAllTransactions().size();
        
        // Calculate available books from database
        List<Book> allBooks = libraryManager.getDatabaseManager().getAllBooks();
        int availableBooks = 0;
        int overdueBooks = 0;
        double totalFines = 0.0;
        
        for (Book book : allBooks) {
            availableBooks += book.getAvailableCopies();
        }
        
        // Get overdue transactions and calculate fines
        List<Transaction> allTransactions = libraryManager.getDatabaseManager().getAllTransactions();
        for (Transaction transaction : allTransactions) {
            if (transaction.isOverdue()) {
                overdueBooks++;
                totalFines += transaction.calculateFine();
            }
        }
        
        System.out.println("Statistics calculated from database:");
        System.out.println("  - Total Books: " + totalBooks);
        System.out.println("  - Total Users: " + totalUsers);
        System.out.println("  - Total Transactions: " + totalTransactions);
        System.out.println("  - Available Books: " + availableBooks);
        System.out.println("  - Overdue Books: " + overdueBooks);
        System.out.println("  - Total Fines: $" + String.format("%.2f", totalFines));
        
        // Update the labels
        totalBooksLabel.setText("Total Books: " + totalBooks);
        totalUsersLabel.setText("Total Users: " + totalUsers);
        totalTransactionsLabel.setText("Total Transactions: " + totalTransactions);
        availableBooksLabel.setText("Available Books: " + availableBooks);
        overdueBooksLabel.setText("Overdue Books: " + overdueBooks);
        totalFinesLabel.setText(String.format("Total Fines: $%.2f", totalFines));
        
        System.out.println("=== END ADMIN DASHBOARD STATISTICS UPDATE ===");
    }
    
    private void showAddBookDialog() {
        JDialog dialog = new JDialog(this, "Add New Book", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form fields
        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField isbnField = new JTextField(20);
        JTextField genreField = new JTextField(20);
        JTextField yearField = new JTextField(20);
        JTextField publisherField = new JTextField(20);
        JTextField copiesField = new JTextField(20);
        
        // Add form components
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        formPanel.add(authorField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        formPanel.add(isbnField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Genre:"), gbc);
        gbc.gridx = 1;
        formPanel.add(genreField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Publication Year:"), gbc);
        gbc.gridx = 1;
        formPanel.add(yearField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Publisher:"), gbc);
        gbc.gridx = 1;
        formPanel.add(publisherField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Total Copies:"), gbc);
        gbc.gridx = 1;
        formPanel.add(copiesField, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String isbn = isbnField.getText().trim();
                String genre = genreField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                String publisher = publisherField.getText().trim();
                int copies = Integer.parseInt(copiesField.getText().trim());
                
                if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || genre.isEmpty() || publisher.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (year < 1800 || year > 2025) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a valid publication year", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (copies <= 0) {
                    JOptionPane.showMessageDialog(dialog, "Total copies must be greater than 0", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                Book newBook = libraryManager.addBook(title, author, isbn, genre, copies, "Description", new java.util.Calendar.Builder().setDate(year, 0, 1).build().getTime(), publisher);
                if (newBook != null) {
                    JOptionPane.showMessageDialog(dialog, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadBooks();
                    updateStatistics();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add book", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for year and copies", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void editSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to edit");
            return;
        }
        
        String bookId = (String) bookTable.getValueAt(selectedRow, 0);
        Book book = libraryManager.getBook(bookId);
        if (book == null) {
            JOptionPane.showMessageDialog(this, "Book not found");
            return;
        }
        
        JDialog dialog = new JDialog(this, "Edit Book", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 500);
        dialog.setLocationRelativeTo(this);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form fields (pre-populated with current values)
        JTextField titleField = new JTextField(book.getTitle(), 20);
        JTextField authorField = new JTextField(book.getAuthor(), 20);
        JTextField isbnField = new JTextField(book.getIsbn(), 20);
        JTextField genreField = new JTextField(book.getGenre(), 20);
        JTextField yearField = new JTextField(String.valueOf(book.getPublicationYear()), 20);
        JTextField publisherField = new JTextField(book.getPublisher(), 20);
        JTextField copiesField = new JTextField(String.valueOf(book.getTotalCopies()), 20);
        
        // Add form components
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        formPanel.add(titleField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        formPanel.add(authorField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        formPanel.add(isbnField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Genre:"), gbc);
        gbc.gridx = 1;
        formPanel.add(genreField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Publication Year:"), gbc);
        gbc.gridx = 1;
        formPanel.add(yearField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Publisher:"), gbc);
        gbc.gridx = 1;
        formPanel.add(publisherField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Total Copies:"), gbc);
        gbc.gridx = 1;
        formPanel.add(copiesField, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            try {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String isbn = isbnField.getText().trim();
                String genre = genreField.getText().trim();
                int year = Integer.parseInt(yearField.getText().trim());
                String publisher = publisherField.getText().trim();
                int copies = Integer.parseInt(copiesField.getText().trim());
                
                if (title.isEmpty() || author.isEmpty() || isbn.isEmpty() || genre.isEmpty() || publisher.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (year < 1800 || year > 2025) {
                    JOptionPane.showMessageDialog(dialog, "Please enter a valid publication year", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (copies < book.getBorrowedCopies()) {
                    JOptionPane.showMessageDialog(dialog, "Total copies cannot be less than borrowed copies (" + book.getBorrowedCopies() + ")", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                boolean success = libraryManager.updateBook(bookId, title, author, isbn, genre, copies, "Description", new java.util.Calendar.Builder().setDate(year, 0, 1).build().getTime(), publisher);
                if (success) {
                    JOptionPane.showMessageDialog(dialog, "Book updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadBooks();
                    updateStatistics();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to update book", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for year and copies", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void deleteSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to delete");
            return;
        }
        
        String bookId = (String) bookTable.getValueAt(selectedRow, 0);
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this book?", "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            if (libraryManager.deleteBook(bookId)) {
                JOptionPane.showMessageDialog(this, "Book deleted successfully");
                loadBooks();
                updateStatistics();
            } else {
                JOptionPane.showMessageDialog(this, "Cannot delete book. It may have active transactions.");
            }
        }
    }
    
    private void showAddUserDialog() {
        JDialog dialog = new JDialog(this, "Add New User", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 500); // Increased height for help text
        dialog.setLocationRelativeTo(this);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form fields
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField fullNameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JComboBox<User.UserRole> roleCombo = new JComboBox<>(User.UserRole.values());
        
        // Set default role to MEMBER for safety
        roleCombo.setSelectedItem(User.UserRole.MEMBER);
        
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
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(fullNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        formPanel.add(roleCombo, gbc);
        
        // Help text for role selection
        JLabel roleHelpLabel = new JLabel("Note: Admin and Librarian roles have elevated privileges");
        roleHelpLabel.setForeground(new Color(150, 75, 0)); // Brown color for warning
        roleHelpLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        gbc.gridx = 0; gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(roleHelpLabel, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            User.UserRole role = (User.UserRole) roleCombo.getSelectedItem();
            
            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "All fields are required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (password.length() < 3) {
                JOptionPane.showMessageDialog(dialog, "Password must be at least 3 characters long", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Additional validation for admin and librarian creation
            if (role == User.UserRole.ADMIN || role == User.UserRole.LIBRARIAN) {
                int result = JOptionPane.showConfirmDialog(dialog,
                    "You are about to create a new " + role.toString().toLowerCase() + " account.\n\n" +
                    "This user will have elevated privileges in the system.\n" +
                    "Are you sure you want to proceed?",
                    "Confirm Elevated Privileges",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                
                if (result != JOptionPane.YES_OPTION) {
                    return;
                }
            }
            
            // Check if username already exists
            if (libraryManager.getUserByUsername(username) != null) {
                JOptionPane.showMessageDialog(dialog, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            User newUser = libraryManager.addUser(username, password, fullName, email, role);
            if (newUser != null) {
                String roleText = role.toString().toLowerCase();
                String message = String.format(
                    "User added successfully!\n\n" +
                    "Username: %s\n" +
                    "Full Name: %s\n" +
                    "Role: %s\n" +
                    "Email: %s\n\n" +
                    "The new %s can now login to the system.",
                    username, fullName, role.toString(), email, roleText
                );
                
                JOptionPane.showMessageDialog(dialog, message, "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadUsers();
                updateStatistics();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to add user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void editSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to edit");
            return;
        }
        
        String userId = (String) userTable.getValueAt(selectedRow, 0);
        User user = libraryManager.getUser(userId);
        if (user == null) {
            JOptionPane.showMessageDialog(this, "User not found");
            return;
        }
        
        JDialog dialog = new JDialog(this, "Edit User", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(400, 450);
        dialog.setLocationRelativeTo(this);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        
        // Form fields (pre-populated with current values)
        JTextField usernameField = new JTextField(user.getUsername(), 20);
        JPasswordField passwordField = new JPasswordField(20);
        passwordField.setText(""); // Don't show current password
        JTextField fullNameField = new JTextField(user.getFullName(), 20);
        JTextField emailField = new JTextField(user.getEmail(), 20);
        JComboBox<User.UserRole> roleCombo = new JComboBox<>(User.UserRole.values());
        roleCombo.setSelectedItem(user.getRole());
        
        // Add form components
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Password (leave blank to keep current):"), gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(fullNameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        formPanel.add(roleCombo, gbc);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");
        
        saveButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            User.UserRole role = (User.UserRole) roleCombo.getSelectedItem();
            
            if (username.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Username, Full Name, and Email are required", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Check if username already exists (for different user)
            User existingUser = libraryManager.getUserByUsername(username);
            if (existingUser != null && !existingUser.getUserId().equals(userId)) {
                JOptionPane.showMessageDialog(dialog, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Use current password if new password is not provided
            String finalPassword = password.isEmpty() ? user.getPassword() : password;
            
            if (!password.isEmpty() && password.length() < 3) {
                JOptionPane.showMessageDialog(dialog, "Password must be at least 3 characters long", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean success = libraryManager.updateUser(userId, username, finalPassword, fullName, email, role);
            if (success) {
                JOptionPane.showMessageDialog(dialog, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                loadUsers();
                updateStatistics();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to update user", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void deleteSelectedUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user to delete");
            return;
        }
        
        String userId = (String) userTable.getValueAt(selectedRow, 0);
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this user?", "Confirm Delete", 
            JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            if (libraryManager.deleteUser(userId)) {
                JOptionPane.showMessageDialog(this, "User deleted successfully");
                loadUsers();
                updateStatistics();
            } else {
                JOptionPane.showMessageDialog(this, "Cannot delete user. They may have active transactions.");
            }
        }
    }

    private void showChangeCapacityDialog() {
        // Get selected user from table
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user first", "No User Selected", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String userId = (String) userTable.getValueAt(selectedRow, 0);
        User selectedUser = libraryManager.getUser(userId);
        
        if (selectedUser == null || selectedUser.getRole() != User.UserRole.MEMBER) {
            JOptionPane.showMessageDialog(this, "Please select a member user", "Invalid User Type", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JDialog dialog = new JDialog(this, "Change Book Capacity for " + selectedUser.getFullName(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(450, 250);
        dialog.setLocationRelativeTo(this);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Current capacity info
        JLabel currentCapacityLabel = new JLabel("Current Book Capacity: " + selectedUser.getBookCapacity());
        JLabel activeLoansLabel = new JLabel("Active Loans: " + selectedUser.getActiveLoansCount());
        JLabel remainingSlotsLabel = new JLabel("Remaining Slots: " + selectedUser.getRemainingBorrowingSlots());
        
        // Style the info labels
        Font infoFont = new Font("Arial", Font.BOLD, 12);
        currentCapacityLabel.setFont(infoFont);
        activeLoansLabel.setFont(infoFont);
        remainingSlotsLabel.setFont(infoFont);
        
        // New capacity input
        JTextField newCapacityField = new JTextField(10);
        newCapacityField.setText(String.valueOf(selectedUser.getBookCapacity()));

        // Add form components
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(currentCapacityLabel, gbc);
        
        gbc.gridy = 1;
        formPanel.add(activeLoansLabel, gbc);
        
        gbc.gridy = 2;
        formPanel.add(remainingSlotsLabel, gbc);
        
        gbc.gridy = 3; gbc.gridwidth = 1;
        formPanel.add(new JLabel("New Book Capacity:"), gbc);
        gbc.gridx = 1;
        formPanel.add(newCapacityField, gbc);

        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton saveButton = new JButton("Save Changes");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            String newCapacityStr = newCapacityField.getText().trim();

            try {
                int newCapacity = Integer.parseInt(newCapacityStr);
                if (newCapacity < 0) {
                    JOptionPane.showMessageDialog(dialog, "Book capacity cannot be negative", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                if (newCapacity < selectedUser.getActiveLoansCount()) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Cannot set capacity below current active loans (" + selectedUser.getActiveLoansCount() + ")", 
                        "Invalid Capacity", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (libraryManager.updateUserBookCapacity(userId, newCapacity)) {
                    JOptionPane.showMessageDialog(dialog, "Book capacity changed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    loadUsers();
                    updateStatistics();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to change book capacity", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter a valid number for book capacity", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
    
    private void logout() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to logout?", "Confirm Logout", 
            JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }
}
