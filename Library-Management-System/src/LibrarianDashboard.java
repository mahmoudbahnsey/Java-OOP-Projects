import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;

/**
 * LibrarianDashboard - Main interface for librarians in the Library Management System
 */
public class LibrarianDashboard extends JFrame {
    private User currentUser;
    private LibraryManager libraryManager;
    
    // Main components
    private JTabbedPane tabbedPane;
    private JLabel welcomeLabel;
    private JButton logoutButton;
    
    // Transaction management components
    private JTable transactionTable;
    private DefaultTableModel transactionTableModel;
    private JButton borrowBookButton;
    private JButton returnBookButton;
    private JButton viewOverdueButton;
    private JButton showAllTransactionsButton;
    private JLabel transactionStatusLabel;
    
    // Book search components
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTextField bookSearchField;
    private JButton searchButton;
    private JButton clearSearchButton;
    
    // User search components
    private JTextField userSearchField;
    private JButton searchUserButton;
    private JButton clearUserSearchButton;
    
    public LibrarianDashboard(User user) {
        this.currentUser = user;
        this.libraryManager = LibraryManager.getInstance();
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        loadData();
        
        setTitle("Library Management System - Librarian Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Welcome and logout
        welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + " (Librarian)");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton = new JButton("Logout");
        
        // Tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Transaction management tab
        setupTransactionManagementTab();
        
        // Book search tab
        setupBookSearchTab();
        
        // User search tab
        setupUserSearchTab();
    }
    
    private void setupTransactionManagementTab() {
        JPanel transactionPanel = new JPanel(new BorderLayout());
        
        // Buttons panel with better organization
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Primary transaction buttons
        borrowBookButton = new JButton("Borrow Book");
        returnBookButton = new JButton("Return Book");
        
        // View control buttons
        viewOverdueButton = new JButton("View Overdue");
        showAllTransactionsButton = new JButton("Show All Transactions");
        
        // Refresh button
        JButton refreshTransactionsButton = new JButton("Refresh Transactions");
        refreshTransactionsButton.setBackground(new Color(40, 167, 69));
        refreshTransactionsButton.setForeground(Color.WHITE);
        refreshTransactionsButton.addActionListener(e -> {
            System.out.println("Librarian Dashboard: Refresh Transactions button clicked");
            loadTransactions();
            JOptionPane.showMessageDialog(this, 
                "Transaction data refreshed from database!\n" +
                "All transaction information is now current.",
                "Transactions Refreshed", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Style the buttons
        styleButton(borrowBookButton, new Color(40, 167, 69));      // Green
        styleButton(returnBookButton, new Color(255, 193, 7));      // Yellow
        styleButton(viewOverdueButton, new Color(220, 53, 69));     // Red
        styleButton(showAllTransactionsButton, new Color(0, 123, 255)); // Blue
        styleButton(refreshTransactionsButton, new Color(40, 167, 69));  // Green
        
        // Add buttons with some grouping
        buttonPanel.add(borrowBookButton);
        buttonPanel.add(returnBookButton);
        buttonPanel.add(new JSeparator(JSeparator.VERTICAL));
        buttonPanel.add(viewOverdueButton);
        buttonPanel.add(showAllTransactionsButton);
        buttonPanel.add(new JSeparator(JSeparator.VERTICAL));
        buttonPanel.add(refreshTransactionsButton);
        
        // Status label
        transactionStatusLabel = new JLabel("Showing all transactions");
        transactionStatusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        transactionStatusLabel.setForeground(Color.BLUE);
        
        // Table
        String[] transactionColumns = {"ID", "Book Title", "User", "Borrow Date", "Due Date", "Status", "Fine"};
        transactionTableModel = new DefaultTableModel(transactionColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable = new JTable(transactionTableModel);
        
        // Set column widths for better readability
        transactionTable.getColumnModel().getColumn(0).setPreferredWidth(80);   // ID
        transactionTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Book Title
        transactionTable.getColumnModel().getColumn(2).setPreferredWidth(120);  // User
        transactionTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Borrow Date
        transactionTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // Due Date
        transactionTable.getColumnModel().getColumn(5).setPreferredWidth(80);   // Status
        transactionTable.getColumnModel().getColumn(6).setPreferredWidth(80);   // Fine
        
        JScrollPane transactionScrollPane = new JScrollPane(transactionTable);
        
        // Create a combined panel for status and table
        JPanel contentPanel = new JPanel(new BorderLayout());
        
        // Add status label above the table
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        statusPanel.add(transactionStatusLabel);
        
        contentPanel.add(statusPanel, BorderLayout.NORTH);
        contentPanel.add(transactionScrollPane, BorderLayout.CENTER);
        
        transactionPanel.add(buttonPanel, BorderLayout.NORTH);
        transactionPanel.add(contentPanel, BorderLayout.CENTER);
        
        tabbedPane.addTab("Transaction Management", transactionPanel);
    }
    
    private void setupBookSearchTab() {
        JPanel bookPanel = new JPanel(new BorderLayout());
        
        // Search panel with better layout
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        searchPanel.add(new JLabel("Search Books:"));
        bookSearchField = new JTextField(25);
        searchPanel.add(bookSearchField);
        searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        clearSearchButton = new JButton("Clear");
        searchPanel.add(clearSearchButton);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton viewDetailsButton = new JButton("View Details");
        buttonPanel.add(viewDetailsButton);
        
        JButton refreshBooksButton = new JButton("Refresh Books");
        refreshBooksButton.setBackground(new Color(40, 167, 69));
        refreshBooksButton.setForeground(Color.WHITE);
        refreshBooksButton.addActionListener(e -> {
            System.out.println("Librarian Dashboard: Refresh Books button clicked");
            loadAvailableBooks();
            JOptionPane.showMessageDialog(this, 
                "Book availability refreshed from database!\n" +
                "All total copies and available copies are now current.",
                "Books Refreshed", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        buttonPanel.add(refreshBooksButton);
        
        // Table
        String[] bookColumns = {"ID", "Title", "Author", "Genre", "Available", "Total", "Status"};
        bookTableModel = new DefaultTableModel(bookColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(bookTableModel);
        
        // Set column widths for better readability
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // ID
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Title
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Author
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Genre
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Available
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(60);  // Total
        bookTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // Status
        
        JScrollPane bookScrollPane = new JScrollPane(bookTable);
        
        bookPanel.add(searchPanel, BorderLayout.NORTH);
        bookPanel.add(bookScrollPane, BorderLayout.CENTER);
        bookPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Book Search", bookPanel);
    }
    
    private void setupUserSearchTab() {
        JPanel userPanel = new JPanel(new BorderLayout());
        
        // Search panel with better layout
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        searchPanel.add(new JLabel("Search Users:"));
        userSearchField = new JTextField(25);
        searchPanel.add(userSearchField);
        searchUserButton = new JButton("Search");
        searchPanel.add(searchUserButton);
        clearUserSearchButton = new JButton("Clear");
        searchPanel.add(clearUserSearchButton);
        
        // Table for user search results
        String[] userColumns = {"ID", "Username", "Full Name", "Email", "Role", "Status"};
        DefaultTableModel userTableModel = new DefaultTableModel(userColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable userTable = new JTable(userTableModel);
        
        // Set column widths for better readability
        userTable.getColumnModel().getColumn(0).setPreferredWidth(60);   // ID
        userTable.getColumnModel().getColumn(1).setPreferredWidth(120);  // Username
        userTable.getColumnModel().getColumn(2).setPreferredWidth(150);  // Full Name
        userTable.getColumnModel().getColumn(3).setPreferredWidth(200);  // Email
        userTable.getColumnModel().getColumn(4).setPreferredWidth(80);   // Role
        userTable.getColumnModel().getColumn(5).setPreferredWidth(80);   // Status
        
        JScrollPane userScrollPane = new JScrollPane(userTable);
        
        userPanel.add(searchPanel, BorderLayout.NORTH);
        userPanel.add(userScrollPane, BorderLayout.CENTER);
        
        tabbedPane.addTab("User Search", userPanel);
        
        // Search functionality
        searchUserButton.addActionListener(e -> searchUsers(userTableModel));
        clearUserSearchButton.addActionListener(e -> {
            userSearchField.setText("");
            userTableModel.setRowCount(0);
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header panel with better styling
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        headerPanel.setBackground(new Color(240, 240, 240));
        
        // Welcome label with better styling
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        
        // Auto-sync button with better styling
        JButton autoSyncButton = new JButton("Auto Sync");
        autoSyncButton.setPreferredSize(new Dimension(100, 35));
        autoSyncButton.setBackground(new Color(0, 123, 255));
        autoSyncButton.setForeground(Color.WHITE);
        autoSyncButton.setFocusPainted(false);
        autoSyncButton.addActionListener(e -> {
            libraryManager.autoSyncWithDatabase();
            loadData(); // Refresh the UI after sync
            JOptionPane.showMessageDialog(this, "Database automatically synchronized!", "Auto Sync Complete", JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Refresh all button
        JButton refreshAllButton = new JButton("Refresh All");
        refreshAllButton.setPreferredSize(new Dimension(100, 35));
        refreshAllButton.setBackground(new Color(255, 193, 7));
        refreshAllButton.setForeground(Color.BLACK);
        refreshAllButton.setFocusPainted(false);
        refreshAllButton.addActionListener(e -> {
            System.out.println("Librarian Dashboard: Refresh All button clicked");
            loadData();
            JOptionPane.showMessageDialog(this, 
                "All data refreshed from database!\n" +
                "Books and transactions are now current.",
                "All Data Refreshed", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Logout button with better styling
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setBackground(new Color(220, 53, 69));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        
        // Center panel for welcome and buttons
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(welcomeLabel);
        centerPanel.add(autoSyncButton);
        centerPanel.add(refreshAllButton);
        
        headerPanel.add(centerPanel, BorderLayout.CENTER);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        logoutButton.addActionListener(e -> logout());
        
        borrowBookButton.addActionListener(e -> showBorrowBookDialog());
        returnBookButton.addActionListener(e -> showReturnBookDialog());
        viewOverdueButton.addActionListener(e -> loadOverdueTransactions());
        showAllTransactionsButton.addActionListener(e -> loadTransactions());
        
        searchButton.addActionListener(e -> searchBooks());
        clearSearchButton.addActionListener(e -> {
            bookSearchField.setText("");
            loadAvailableBooks();
        });
        
        // Double-click to select
        transactionTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showTransactionDetails();
                }
            }
        });
        
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showBookDetails();
                }
            }
        });
    }
    
    private void loadData() {
        loadTransactions();
        loadAvailableBooks();
    }
    
    private void loadTransactions() {
        System.out.println("=== LIBRARIAN DASHBOARD: LOADING TRANSACTIONS FROM DATABASE ===");
        transactionTableModel.setRowCount(0);
        
        // Get transactions directly from database instead of memory
        List<Transaction> transactions = libraryManager.getDatabaseManager().getAllTransactions();
        System.out.println("Retrieved " + transactions.size() + " transactions from database");
        
        for (Transaction transaction : transactions) {
            System.out.println("Processing transaction: " + transaction.getTransactionId() + 
                             " | Book: " + transaction.getBook().getTitle() + 
                             " | User: " + transaction.getUser().getUsername() + 
                             " | Status: " + transaction.getStatus());
            
            Object[] row = {
                transaction.getTransactionId(),
                transaction.getBook().getTitle(),
                transaction.getUser().getUsername(),
                transaction.getBorrowDate().toLocalDate().toString(),
                transaction.getDueDate().toLocalDate().toString(),
                transaction.getStatusDescription(),
                String.format("$%.2f", transaction.calculateFine())
            };
            transactionTableModel.addRow(row);
        }
        
        // Force table refresh
        transactionTableModel.fireTableDataChanged();
        transactionTable.revalidate();
        transactionTable.repaint();
        
        // Update status label
        transactionStatusLabel.setText("Showing all transactions (" + transactions.size() + " total) - Database Verified");
        transactionStatusLabel.setForeground(Color.BLUE);
        
        System.out.println("Librarian dashboard transactions loaded. Table now has " + transactionTableModel.getRowCount() + " rows");
        System.out.println("=== END LIBRARIAN DASHBOARD TRANSACTION LOADING ===");
    }
    
    private void loadOverdueTransactions() {
        System.out.println("=== LIBRARIAN DASHBOARD: LOADING OVERDUE TRANSACTIONS FROM DATABASE ===");
        transactionTableModel.setRowCount(0);
        
        // Get all transactions from database and filter for overdue ones
        List<Transaction> allTransactions = libraryManager.getDatabaseManager().getAllTransactions();
        List<Transaction> overdueTransactions = new ArrayList<>();
        
        for (Transaction transaction : allTransactions) {
            if (transaction.isOverdue()) {
                overdueTransactions.add(transaction);
            }
        }
        
        System.out.println("Found " + overdueTransactions.size() + " overdue transactions out of " + allTransactions.size() + " total");
        
        if (overdueTransactions.isEmpty()) {
            // Show a message that there are no overdue transactions
            Object[] row = {"", "No overdue transactions found", "", "", "", "", ""};
            transactionTableModel.addRow(row);
        } else {
            for (Transaction transaction : overdueTransactions) {
                System.out.println("Processing overdue transaction: " + transaction.getTransactionId() + 
                                 " | Book: " + transaction.getBook().getTitle() + 
                                 " | User: " + transaction.getUser().getUsername() + 
                                 " | Days Overdue: " + transaction.getDaysOverdue());
                
                Object[] row = {
                    transaction.getTransactionId(),
                    transaction.getBook().getTitle(),
                    transaction.getUser().getUsername(),
                    transaction.getBorrowDate().toLocalDate().toString(),
                    transaction.getDueDate().toLocalDate().toString(),
                    "OVERDUE",
                    String.format("$%.2f", transaction.calculateFine())
                };
                transactionTableModel.addRow(row);
            }
        }
        
        // Force table refresh
        transactionTableModel.fireTableDataChanged();
        transactionTable.revalidate();
        transactionTable.repaint();
        
        // Update status label
        if (overdueTransactions.isEmpty()) {
            transactionStatusLabel.setText("No overdue transactions found - Database Verified");
            transactionStatusLabel.setForeground(Color.GREEN);
        } else {
            transactionStatusLabel.setText("Showing overdue transactions (" + overdueTransactions.size() + " overdue) - Database Verified");
            transactionStatusLabel.setForeground(Color.RED);
        }
        
        System.out.println("Librarian dashboard overdue transactions loaded. Table now has " + transactionTableModel.getRowCount() + " rows");
        System.out.println("=== END LIBRARIAN DASHBOARD OVERDUE TRANSACTION LOADING ===");
    }
    
    private void loadAvailableBooks() {
        System.out.println("=== LIBRARIAN DASHBOARD: LOADING AVAILABLE BOOKS FROM DATABASE ===");
        bookTableModel.setRowCount(0);
        
        // Get books directly from database instead of memory
        List<Book> availableBooks = libraryManager.getDatabaseManager().getAllBooks();
        System.out.println("Retrieved " + availableBooks.size() + " books from database");
        
        for (Book book : availableBooks) {
            System.out.println("Processing book: " + book.getBookId() + 
                             " | Title: " + book.getTitle() + 
                             " | Available: " + book.getAvailableCopies() + 
                             " | Total: " + book.getTotalCopies() + 
                             " | Borrowed: " + book.getBorrowedCopies() + 
                             " | Is Available: " + book.isAvailable());
            
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
        
        System.out.println("Librarian dashboard books loaded. Table now has " + bookTableModel.getRowCount() + " rows");
        System.out.println("=== END LIBRARIAN DASHBOARD BOOK LOADING ===");
    }
    
    private void searchBooks() {
        String searchTerm = bookSearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadAvailableBooks();
            return;
        }
        
        System.out.println("=== LIBRARIAN DASHBOARD: SEARCHING BOOKS IN DATABASE ===");
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
                             " | Borrowed: " + book.getBorrowedCopies() + 
                             " | Is Available: " + book.isAvailable());
            
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
        
        System.out.println("Librarian dashboard search completed. Table now has " + bookTableModel.getRowCount() + " rows");
        System.out.println("=== END LIBRARIAN DASHBOARD BOOK SEARCH ===");
    }
    
    private void searchUsers(DefaultTableModel userTableModel) {
        String searchTerm = userSearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            userTableModel.setRowCount(0);
            return;
        }
        
        List<User> allUsers = libraryManager.getAllUsers();
        userTableModel.setRowCount(0);
        
        for (User user : allUsers) {
            if (user.getUsername().toLowerCase().contains(searchTerm.toLowerCase()) ||
                user.getFullName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                user.getEmail().toLowerCase().contains(searchTerm.toLowerCase())) {
                
                Object[] row = {
                    user.getUserId(),
                    user.getUsername(),
                    user.getFullName(),
                    user.getEmail(),
                    user.getRole().toString(),
                    user.isActive() ? "Active" : "Inactive"
                };
                userTableModel.addRow(row);
            }
        }
    }
    
    private void showBorrowBookDialog() {
        // Simple dialog for borrowing books
        JDialog dialog = new JDialog(this, "Borrow Book", true);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JTextField bookIdField = new JTextField(10);
        JTextField userIdField = new JTextField(10);
        JButton borrowButton = new JButton("Borrow");
        JButton cancelButton = new JButton("Cancel");
        
        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(new JLabel("Book ID:"), gbc);
        gbc.gridx = 1;
        dialog.add(bookIdField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(new JLabel("User ID:"), gbc);
        gbc.gridx = 1;
        dialog.add(userIdField, gbc);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(borrowButton);
        buttonPanel.add(cancelButton);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        dialog.add(buttonPanel, gbc);
        
        borrowButton.addActionListener(e -> {
            String bookId = bookIdField.getText().trim();
            String userId = userIdField.getText().trim();
            
            if (bookId.isEmpty() || userId.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please enter both Book ID and User ID");
                return;
            }
            
            Transaction transaction = libraryManager.borrowBook(bookId, userId);
            if (transaction != null) {
                JOptionPane.showMessageDialog(dialog, "Book borrowed successfully!");
                dialog.dispose();
                loadData();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to borrow book. Check if book is available and user can borrow.");
            }
        });
        
        cancelButton.addActionListener(e -> dialog.dispose());
        
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private void showReturnBookDialog() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a transaction to return");
            return;
        }
        
        String transactionId = (String) transactionTable.getValueAt(selectedRow, 0);
        Transaction transaction = libraryManager.getTransaction(transactionId);
        
        if (transaction == null) {
            JOptionPane.showMessageDialog(this, "Transaction not found");
            return;
        }
        
        if (!transaction.canBeReturned()) {
            JOptionPane.showMessageDialog(this, "This transaction cannot be returned");
            return;
        }
        
        int result = JOptionPane.showConfirmDialog(this, 
            "Return book: " + transaction.getBook().getTitle() + "?", 
            "Confirm Return", JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            if (libraryManager.returnBook(transactionId)) {
                double fine = transaction.calculateFine();
                if (fine > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Book returned successfully. Fine: $" + String.format("%.2f", fine));
                } else {
                    JOptionPane.showMessageDialog(this, "Book returned successfully. No fine.");
                }
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to return book");
            }
        }
    }
    
    private void showTransactionDetails() {
        int selectedRow = transactionTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        String transactionId = (String) transactionTable.getValueAt(selectedRow, 0);
        Transaction transaction = libraryManager.getTransaction(transactionId);
        
        if (transaction != null) {
            String details = String.format(
                "Transaction Details:\n\n" +
                "ID: %s\n" +
                "Book: %s\n" +
                "User: %s\n" +
                "Borrow Date: %s\n" +
                "Due Date: %s\n" +
                "Status: %s\n" +
                "Fine: $%.2f",
                transaction.getTransactionId(),
                transaction.getBook().getTitle(),
                transaction.getUser().getFullName(),
                transaction.getBorrowDate().toString(),
                transaction.getDueDate().toString(),
                transaction.getStatusDescription(),
                transaction.calculateFine()
            );
            
            JOptionPane.showMessageDialog(this, details, "Transaction Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showBookDetails() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        String bookId = (String) bookTable.getValueAt(selectedRow, 0);
        Book book = libraryManager.getBook(bookId);
        
        if (book != null) {
            String details = String.format(
                "Book Details:\n\n" +
                "ID: %s\n" +
                "Title: %s\n" +
                "Author: %s\n" +
                "Genre: %s\n" +
                "ISBN: %s\n" +
                "Year: %d\n" +
                "Publisher: %s\n" +
                "Available: %d/%d\n" +
                "Status: %s",
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
            );
            
            JOptionPane.showMessageDialog(this, details, "Book Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Helper method to style buttons consistently
     */
    private void styleButton(JButton button, Color backgroundColor) {
        button.setPreferredSize(new Dimension(120, 35));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        button.setFont(new Font("Arial", Font.BOLD, 12));
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
