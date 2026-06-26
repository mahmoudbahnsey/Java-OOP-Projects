import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

/**
 * MemberDashboard - Main interface for library members in the Library Management System
 */
public class MemberDashboard extends JFrame {
    private User currentUser;
    private LibraryManager libraryManager;
    
    // Main components
    private JTabbedPane tabbedPane;
    private JLabel welcomeLabel;
    private JButton logoutButton;
    
    // Book search components
    private JTable bookTable;
    private DefaultTableModel bookTableModel;
    private JTextField bookSearchField;
    private JButton searchButton;
    private JButton clearSearchButton;
    private JButton viewDetailsButton;
    private JButton borrowButton;
    
    // My books components
    private JTable myBooksTable;
    private DefaultTableModel myBooksTableModel;
    private JButton refreshButton;
    private JButton returnBookButton;
    
    // Status components
    private JLabel borrowingStatusLabel;
    private JLabel capacityInfoLabel;
    
    public MemberDashboard(User user) {
        try {
            System.out.println("MemberDashboard constructor called with user: " + user.getUsername());
            
            this.currentUser = user;
            System.out.println("Current user set successfully");
            
            this.libraryManager = LibraryManager.getInstance();
            System.out.println("LibraryManager instance obtained successfully");
            
            // Refresh user data from database to ensure it's current
            System.out.println("Attempting to refresh user data from database...");
            refreshUserDataFromDatabase();
            System.out.println("User data refreshed successfully");
            
            System.out.println("Initializing components...");
            initializeComponents();
            System.out.println("Components initialized successfully");
            
            System.out.println("Setting up layout...");
            setupLayout();
            System.out.println("Layout setup successfully");
            
            System.out.println("Setting up event handlers...");
            setupEventHandlers();
            System.out.println("Event handlers setup successfully");
            
            System.out.println("Loading data...");
            loadData();
            System.out.println("Data loaded successfully");
            
            setTitle("Library Management System - Member Dashboard");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(900, 600);
            setLocationRelativeTo(null);
            
            System.out.println("About to make MemberDashboard visible...");
            setVisible(true);
            System.out.println("MemberDashboard is now visible!");
            
        } catch (Exception e) {
            System.err.println("ERROR in MemberDashboard constructor: " + e.getMessage());
            e.printStackTrace();
            
            // Show error dialog
            JOptionPane.showMessageDialog(null, 
                "Error opening Member Dashboard:\n" + e.getMessage() + "\n\nCheck console for details.",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void refreshUserDataFromDatabase() {
        try {
            System.out.println("refreshUserDataFromDatabase() called for user: " + currentUser.getUserId());
            
            // Get fresh user data from database
            User freshUser = libraryManager.getDatabaseManager().getUser(currentUser.getUserId());
            
            if (freshUser != null) {
                this.currentUser = freshUser;
                System.out.println("User data refreshed from database: " + currentUser.getFullName());
            } else {
                System.out.println("WARNING: Could not refresh user data from database, using existing data");
            }
        } catch (Exception e) {
            System.err.println("ERROR in refreshUserDataFromDatabase: " + e.getMessage());
            e.printStackTrace();
            System.out.println("Continuing with existing user data...");
        }
    }
    
    private void initializeComponents() {
        try {
            System.out.println("initializeComponents() called");
            
            // Welcome and logout
            welcomeLabel = new JLabel("Welcome, " + currentUser.getFullName() + " (Member)");
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
            logoutButton = new JButton("Logout");
            System.out.println("Welcome label and logout button created");
            
            // Status labels
            borrowingStatusLabel = new JLabel("Loading borrowing status...");
            capacityInfoLabel = new JLabel("Loading capacity information...");
            System.out.println("Status labels created");
            
            // Tabbed pane
            tabbedPane = new JTabbedPane();
            System.out.println("Tabbed pane created");
            
            // Book search tab
            System.out.println("Setting up book search tab...");
            setupBookSearchTab();
            System.out.println("Book search tab setup complete");
            
            // My books tab
            System.out.println("Setting up my books tab...");
            setupMyBooksTab();
            System.out.println("My books tab setup complete");
            
            System.out.println("All components initialized successfully");
        } catch (Exception e) {
            System.err.println("ERROR in initializeComponents: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize components", e);
        }
    }
    
    private void setupBookSearchTab() {
        JPanel bookPanel = new JPanel(new BorderLayout());
        
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search Books:"));
        bookSearchField = new JTextField(20);
        searchPanel.add(bookSearchField);
        searchButton = new JButton("Search");
        searchPanel.add(searchButton);
        clearSearchButton = new JButton("Clear");
        searchPanel.add(clearSearchButton);
        
        // Table
        String[] bookColumns = {"ID", "Title", "Author", "Genre", "Available", "Total", "Status", "Action"};
        bookTableModel = new DefaultTableModel(bookColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(bookTableModel);
        
        // Set column widths
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // ID
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Title
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Author
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Genre
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(80);  // Available
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(60);  // Total
        bookTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Status
        bookTable.getColumnModel().getColumn(7).setPreferredWidth(80);  // Action
        
        JScrollPane bookScrollPane = new JScrollPane(bookTable);
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        viewDetailsButton = new JButton("View Details");
        borrowButton = new JButton("Borrow Book");
        
        buttonPanel.add(viewDetailsButton);
        buttonPanel.add(borrowButton);
        
        bookPanel.add(searchPanel, BorderLayout.NORTH);
        bookPanel.add(bookScrollPane, BorderLayout.CENTER);
        bookPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        tabbedPane.addTab("Search Books", bookPanel);
    }
    
    private void setupMyBooksTab() {
        JPanel myBooksPanel = new JPanel(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(new JLabel("My Book Transactions:"));
        refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(40, 167, 69));
        refreshButton.setForeground(Color.WHITE);
        
        returnBookButton = new JButton("Return Book");
        headerPanel.add(refreshButton);
        headerPanel.add(returnBookButton);
        
        // Table
        String[] myBooksColumns = {"Transaction ID", "Book Title", "Borrow Date", "Due Date", "Status", "Fine", "Return Date"};
        myBooksTableModel = new DefaultTableModel(myBooksColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        myBooksTable = new JTable(myBooksTableModel);
        JScrollPane myBooksScrollPane = new JScrollPane(myBooksTable);
        
        myBooksPanel.add(headerPanel, BorderLayout.NORTH);
        myBooksPanel.add(myBooksScrollPane, BorderLayout.CENTER);
        
        tabbedPane.addTab("My Books", myBooksPanel);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Left side - welcome and status
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(welcomeLabel, BorderLayout.NORTH);
        
        // Status panel with both labels
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.add(borrowingStatusLabel, BorderLayout.NORTH);
        statusPanel.add(capacityInfoLabel, BorderLayout.SOUTH);
        
        leftPanel.add(statusPanel, BorderLayout.SOUTH);
        
        // Center panel for auto-sync button
        JButton autoSyncButton = new JButton("Auto Sync");
        autoSyncButton.setBackground(new Color(0, 123, 255));
        autoSyncButton.setForeground(Color.WHITE);
        autoSyncButton.addActionListener(e -> {
            forceRefreshAllDataFromDatabase();
            JOptionPane.showMessageDialog(this, "Database automatically synchronized!", "Auto Sync Complete", JOptionPane.INFORMATION_MESSAGE);
        });
        
        JButton refreshAllButton = new JButton("Refresh All");
        refreshAllButton.setBackground(new Color(255, 193, 7));
        refreshAllButton.setForeground(Color.BLACK);
        refreshAllButton.addActionListener(e -> {
            System.out.println("Member Dashboard: Refresh All button clicked");
            forceRefreshAllDataFromDatabase();
            JOptionPane.showMessageDialog(this, 
                "All data refreshed from database!\n" +
                "Books, transactions, and status are now current.",
                "All Data Refreshed", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.add(autoSyncButton);
        centerPanel.add(refreshAllButton);
        
        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(centerPanel, BorderLayout.CENTER);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        
        add(headerPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
        logoutButton.addActionListener(e -> logout());
        
        searchButton.addActionListener(e -> searchBooks());
        clearSearchButton.addActionListener(e -> {
            bookSearchField.setText("");
            loadAvailableBooks();
        });
        viewDetailsButton.addActionListener(e -> showBookDetails());
        borrowButton.addActionListener(e -> borrowSelectedBook());
        returnBookButton.addActionListener(e -> returnSelectedBook());
        refreshButton.addActionListener(e -> {
            System.out.println("Refresh button clicked - forcing table refresh from database");
            forceRefreshMyBooksTable();
            updateBorrowingStatus();
            JOptionPane.showMessageDialog(this, 
                "Table refreshed with latest database data!\n" +
                "All return dates, fines, and status updates are now current.",
                "Refresh Complete", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        // Double-click to view details
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showBookDetails();
                }
            }
        });
        
        myBooksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    showMyBookDetails();
                }
            }
        });
        
        // Enter key in search field
        bookSearchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchBooks();
                }
            }
        });
    }
    
    private void loadData() {
        try {
            System.out.println("loadData() called");
            
            // Load all data directly from database
            forceRefreshAllDataFromDatabase();
            
            System.out.println("Data loading completed successfully");
        } catch (Exception e) {
            System.err.println("ERROR in loadData: " + e.getMessage());
            e.printStackTrace();
            
            // Show error dialog but don't crash the dashboard
            JOptionPane.showMessageDialog(this, 
                "Error loading data:\n" + e.getMessage() + "\n\nSome features may not work properly.",
                "Data Loading Error", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void loadAvailableBooks() {
        System.out.println("=== LOADING AVAILABLE BOOKS ===");
        
        try {
            // Clear the current book table
            bookTableModel.setRowCount(0);
            System.out.println("Book table cleared");
            
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
                    book.isActive() ? "Available" : "Unavailable",
                    book.isAvailable() ? "Borrow" : "Unavailable"
                };
                bookTableModel.addRow(row);
                System.out.println("Added book row: " + book.getTitle() + 
                                 " (Available: " + book.getAvailableCopies() + 
                                 "/" + book.getTotalCopies() + ")");
            }
            
            // Force table refresh
            bookTableModel.fireTableDataChanged();
            bookTable.revalidate();
            bookTable.repaint();
            
            System.out.println("Available books loaded. Table now has " + bookTableModel.getRowCount() + " rows");
            
        } catch (Exception e) {
            System.err.println("ERROR in loadAvailableBooks: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== END LOADING AVAILABLE BOOKS ===");
    }
    
    private void searchBooks() {
        String searchTerm = bookSearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            loadAvailableBooks();
            return;
        }
        
        System.out.println("=== SEARCHING BOOKS ===");
        System.out.println("Search term: '" + searchTerm + "'");
        
        try {
            // Get books directly from database instead of memory
            List<Book> books = libraryManager.getDatabaseManager().searchBooks(searchTerm);
            System.out.println("Found " + books.size() + " matching books");
            
            // Clear the current book table
            bookTableModel.setRowCount(0);
            System.out.println("Book table cleared for search results");
            
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
                    book.isActive() ? "Available" : "Unavailable",
                    book.isAvailable() ? "Borrow" : "Unavailable"
                };
                bookTableModel.addRow(row);
                System.out.println("Added search result: " + book.getTitle() + 
                                 " (Available: " + book.getAvailableCopies() + 
                                 "/" + book.getTotalCopies() + ")");
            }
            
            // Force table refresh
            bookTableModel.fireTableDataChanged();
            bookTable.revalidate();
            bookTable.repaint();
            
            System.out.println("Search completed. Table now has " + bookTableModel.getRowCount() + " rows");
            
        } catch (Exception e) {
            System.err.println("ERROR in searchBooks: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== END SEARCHING BOOKS ===");
    }
    
    private void loadMyBooks() {
        System.out.println("loadMyBooks() method called");
        myBooksTableModel.setRowCount(0);
        
        // Always get fresh transaction data directly from database
        List<Transaction> myTransactions = libraryManager.getDatabaseManager().getUserTransactionsFromDatabase(currentUser.getUserId());
        System.out.println("Found " + myTransactions.size() + " transactions for user " + currentUser.getUserId());
        
        for (Transaction transaction : myTransactions) {
            System.out.println("Processing transaction: " + transaction.getTransactionId() + " - Status: " + transaction.getStatus());
            
            // Show all transactions (active, returned, overdue)
            String status = transaction.getStatus().toString();
            if (transaction.getStatus() == Transaction.TransactionStatus.ACTIVE && transaction.isOverdue()) {
                status = "OVERDUE";
            }
            
            String returnDate = "";
            if (transaction.getReturnDate() != null) {
                returnDate = transaction.getReturnDate().toLocalDate().toString();
                System.out.println("Transaction " + transaction.getTransactionId() + " has return date: " + returnDate);
            } else {
                System.out.println("Transaction " + transaction.getTransactionId() + " has no return date");
            }
            
            Object[] row = {
                transaction.getTransactionId(),
                transaction.getBook().getTitle(),
                transaction.getBorrowDate().toLocalDate().toString(),
                transaction.getDueDate().toLocalDate().toString(),
                status,
                String.format("$%.2f", transaction.calculateFine()),
                returnDate
            };
            myBooksTableModel.addRow(row);
            System.out.println("Added row for transaction: " + transaction.getTransactionId());
        }
        
        System.out.println("Table now has " + myBooksTableModel.getRowCount() + " rows");
        
        // Show message about persistence
        if (!myTransactions.isEmpty()) {
            borrowingStatusLabel.setText("Transaction history loaded from database - your history is preserved across sessions!");
            borrowingStatusLabel.setForeground(Color.GREEN);
        }
        
        // Force table refresh
        myBooksTableModel.fireTableDataChanged();
    }
    
    private void refreshMyBooksFromDatabase() {
        // Force database synchronization to get latest transaction data
        libraryManager.autoSyncWithDatabase();
        
        // Clear and reload the table with fresh data from database
        System.out.println("Refreshing My Books table from database...");
        loadMyBooks();
        
        // Update borrowing status with latest data
        updateBorrowingStatus();
        
        // Show confirmation
        JOptionPane.showMessageDialog(this, 
            "Transaction data refreshed from database!\n" +
            "All return dates, fines, and status updates are now current.",
            "Refresh Complete", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void borrowSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to borrow");
            return;
        }
        
        String bookId = (String) bookTable.getValueAt(selectedRow, 0);
        
        // Get book directly from database instead of memory
        Book book = libraryManager.getDatabaseManager().getBook(bookId);
        
        if (book == null) {
            JOptionPane.showMessageDialog(this, "Book not found");
            return;
        }
        
        if (!book.isAvailable()) {
            JOptionPane.showMessageDialog(this, "This book is not available for borrowing");
            return;
        }
        
        // Check if user can borrow more books using database-driven capacity checking
        int activeLoans = libraryManager.getDatabaseManager().getActiveLoansCountFromDatabase(currentUser.getUserId());
        int capacity = currentUser.getBookCapacity();
        
        if (activeLoans >= capacity) {
            JOptionPane.showMessageDialog(this, 
                String.format("You have reached your borrowing limit of %d books.\n" +
                "Please return some books before borrowing more.", capacity),
                "Borrowing Limit Reached",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirm borrowing
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to borrow '" + book.getTitle() + "'?\n\n" +
            "Due Date: " + java.time.LocalDate.now().plusDays(14) + "\n" +
            "Fine Rate: $1.00 per day if overdue",
            "Confirm Borrow",
            JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            Transaction transaction = libraryManager.borrowBook(bookId, currentUser.getUserId());
            
            if (transaction != null) {
                JOptionPane.showMessageDialog(this,
                    "Book borrowed successfully!\n\n" +
                    "Transaction ID: " + transaction.getTransactionId() + "\n" +
                    "Due Date: " + transaction.getDueDate().toLocalDate().toString() + "\n" +
                    "Please return the book on time to avoid fines.",
                    "Borrow Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Force database refresh to get latest transaction data
                libraryManager.autoSyncWithDatabase();
                
                // Refresh the tables and status with fresh database data
                System.out.println("Book borrowed successfully, refreshing tables from database...");
                
                // IMPORTANT: Force refresh book availability from database
                System.out.println("Refreshing book availability from database...");
                forceRefreshBookAvailability();
                
                // Refresh other components
                loadMyBooks(); // This will get fresh data from database
                updateBorrowingStatus();
                System.out.println("Table refresh completed");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to borrow book. Please try again or contact a librarian.",
                    "Borrow Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showBookDetails() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a book to view details");
            return;
        }
        
        String bookId = (String) bookTable.getValueAt(selectedRow, 0);
        
        // Get book directly from database instead of memory
        Book book = libraryManager.getDatabaseManager().getBook(bookId);
        
        if (book != null) {
            String details = String.format(
                "Book Details:\n\n" +
                "ID: %s\n" +
                "Title: %s\n" +
                "Author: %s\n" +
                "Genre: %s\n" +
                "ISBN: %s\n" +
                "Publication Year: %d\n" +
                "Publisher: %s\n" +
                "Available Copies: %d\n" +
                "Total Copies: %d\n" +
                "Status: %s\n" +
                "Date Added: %s",
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getIsbn(),
                book.getPublicationYear(),
                book.getPublisher(),
                book.getAvailableCopies(),
                book.getTotalCopies(),
                book.isActive() ? "Active" : "Inactive",
                book.getDateAdded().toString()
            );
            
            JOptionPane.showMessageDialog(this, details, "Book Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void updateBorrowingStatus() {
        // Get all capacity information directly from database
        int activeLoans = libraryManager.getDatabaseManager().getActiveLoansCountFromDatabase(currentUser.getUserId());
        int overdueBooks = libraryManager.getDatabaseManager().getOverdueBooksCountFromDatabase(currentUser.getUserId());
        int capacity = currentUser.getBookCapacity();
        int remaining = capacity - activeLoans;
        
        String statusText = String.format("Active Loans: %d/%d | Overdue: %d | Remaining Slots: %d", 
                                        activeLoans, capacity, overdueBooks, remaining);
        
        // Color coding based on capacity usage
        if (remaining == 0) {
            borrowingStatusLabel.setText(statusText + " - AT CAPACITY LIMIT");
            borrowingStatusLabel.setForeground(Color.RED);
        } else if (remaining <= 2) {
            borrowingStatusLabel.setText(statusText + " - NEARING CAPACITY");
            borrowingStatusLabel.setForeground(Color.ORANGE);
        } else {
            borrowingStatusLabel.setText(statusText + " - CAN BORROW MORE");
            borrowingStatusLabel.setForeground(Color.GREEN);
        }
        
        borrowingStatusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Update capacity info with database-driven data
        capacityInfoLabel.setText(String.format("Book Capacity: %d | Active Loans: %d | Overdue: %d | Remaining: %d | Database Verified", 
                                              capacity, activeLoans, overdueBooks, remaining));
        capacityInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
    }
    
    private void returnSelectedBook() {
        System.out.println("returnSelectedBook() method called");
        
        int selectedRow = myBooksTable.getSelectedRow();
        if (selectedRow == -1) {
            System.out.println("No row selected");
            JOptionPane.showMessageDialog(this, "Please select a book to return");
            return;
        }
        
        String transactionId = (String) myBooksTable.getValueAt(selectedRow, 0);
        System.out.println("Selected transaction ID: " + transactionId);
        
        // Get fresh transaction data from database to ensure latest information
        Transaction transaction = libraryManager.getTransactionFromDatabase(transactionId);
        System.out.println("Transaction from database: " + (transaction != null ? "Found" : "Not found"));
        
        if (transaction == null) {
            System.out.println("Transaction not found in database");
            JOptionPane.showMessageDialog(this, "Transaction not found");
            return;
        }
        
        System.out.println("Transaction status: " + transaction.getStatus());
        System.out.println("Can be returned: " + transaction.canBeReturned());
        
        if (transaction.getStatus() != Transaction.TransactionStatus.ACTIVE) {
            System.out.println("Transaction is not active, cannot be returned");
            JOptionPane.showMessageDialog(this, "This book has already been returned");
            return;
        }
        
        // Calculate fine if overdue
        double fine = transaction.calculateFine();
        String fineMessage = "";
        if (fine > 0) {
            fineMessage = "\n\nFine Amount: $" + String.format("%.2f", fine);
        }
        
        // Confirm return
        int result = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to return '" + transaction.getBook().getTitle() + "'?" +
            fineMessage + "\n\n" +
            "Due Date: " + transaction.getDueDate().toLocalDate().toString() + "\n" +
            "Current Date: " + java.time.LocalDate.now(),
            "Confirm Return",
            JOptionPane.YES_NO_OPTION);
            
        if (result == JOptionPane.YES_OPTION) {
            System.out.println("User confirmed return, calling libraryManager.returnBook()");
            System.out.println("Transaction ID to return: " + transactionId);
            System.out.println("Transaction status before return: " + transaction.getStatus());
            System.out.println("Transaction return date before return: " + transaction.getReturnDate());
            
            boolean success = libraryManager.returnBook(transactionId);
            System.out.println("Return book result: " + success);
            
            if (success) {
                System.out.println("Book returned successfully! Now refreshing table...");
                
                String successMessage = "Book returned successfully!";
                if (fine > 0) {
                    successMessage += "\n\nFine Amount: $" + String.format("%.2f", fine) + 
                                   "\nPlease pay the fine at the library counter.";
                }
                
                JOptionPane.showMessageDialog(this,
                    successMessage,
                    "Return Successful",
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Force immediate refresh from database
                System.out.println("Book returned successfully, refreshing table from database...");
                
                // Add a small delay to ensure database transaction is fully committed
                try {
                    Thread.sleep(100); // 100ms delay
                    System.out.println("Delay completed, now refreshing table...");
                } catch (InterruptedException e) {
                    System.err.println("Delay interrupted: " + e.getMessage());
                }
                
                // Force refresh the table with fresh database data
                forceRefreshMyBooksTable();
                
                // Verify the transaction was actually updated in the database
                verifyTransactionUpdate(transactionId);
                
                // Refresh other components
                loadAvailableBooks();
                updateBorrowingStatus();
                
                System.out.println("Table refresh completed");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to return book. Please contact a librarian.",
                    "Return Failed",
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            System.out.println("User cancelled return");
        }
    }
    
    private void showMyBookDetails() {
        int selectedRow = myBooksTable.getSelectedRow();
        if (selectedRow == -1) return;
        
        String transactionId = (String) myBooksTable.getValueAt(selectedRow, 0);
        
        // Get fresh transaction data from database to ensure latest information
        Transaction transaction = libraryManager.getTransactionFromDatabase(transactionId);
        
        if (transaction != null) {
            String details = String.format(
                "My Book Details:\n\n" +
                "Transaction ID: %s\n" +
                "Book Title: %s\n" +
                "Author: %s\n" +
                "Borrow Date: %s\n" +
                "Due Date: %s\n" +
                "Status: %s\n" +
                "Current Fine: $%.2f\n" +
                "Days %s: %d",
                transaction.getTransactionId(),
                transaction.getBook().getTitle(),
                transaction.getBook().getAuthor(),
                transaction.getBorrowDate().toLocalDate().toString(),
                transaction.getDueDate().toLocalDate().toString(),
                transaction.getStatusDescription(),
                transaction.calculateFine(),
                transaction.isOverdue() ? "Overdue" : "Remaining",
                transaction.isOverdue() ? 
                    transaction.getDaysOverdue() : 
                    java.time.temporal.ChronoUnit.DAYS.between(
                        java.time.LocalDate.now(), 
                        transaction.getDueDate().toLocalDate()
                    )
            );
            
            JOptionPane.showMessageDialog(this, details, "My Book Details", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void showAllTransactionHistory() {
        // Get all transactions directly from database
        List<Transaction> allTransactions = libraryManager.getDatabaseManager().getUserTransactionsFromDatabase(currentUser.getUserId());
        
        if (allTransactions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No transaction history found.", "Transaction History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        // Create a detailed history dialog
        JDialog dialog = new JDialog(this, "Complete Transaction History - " + currentUser.getFullName(), true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(this);
        
        // Create table model for detailed history
        String[] historyColumns = {"Transaction ID", "Book Title", "Author", "Borrow Date", "Due Date", "Return Date", "Status", "Fine", "Days Overdue"};
        DefaultTableModel historyModel = new DefaultTableModel(historyColumns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        JTable historyTable = new JTable(historyModel);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        
        // Populate with all transactions
        for (Transaction transaction : allTransactions) {
            String returnDate = "";
            if (transaction.getReturnDate() != null) {
                returnDate = transaction.getReturnDate().toLocalDate().toString();
            }
            
            String status = transaction.getStatus().toString();
            if (transaction.getStatus() == Transaction.TransactionStatus.ACTIVE && transaction.isOverdue()) {
                status = "OVERDUE";
            }
            
            String daysOverdue = "";
            if (transaction.isOverdue()) {
                daysOverdue = String.valueOf(transaction.getDaysOverdue());
            }
            
            Object[] row = {
                transaction.getTransactionId(),
                transaction.getBook().getTitle(),
                transaction.getBook().getAuthor(),
                transaction.getBorrowDate().toLocalDate().toString(),
                transaction.getDueDate().toLocalDate().toString(),
                returnDate,
                status,
                String.format("$%.2f", transaction.calculateFine()),
                daysOverdue
            };
            historyModel.addRow(row);
        }
        
        // Summary panel
        JPanel summaryPanel = new JPanel(new GridLayout(1, 4, 10, 5));
        summaryPanel.setBorder(BorderFactory.createTitledBorder("Summary"));
        
        int totalTransactions = allTransactions.size();
        int activeLoans = (int) allTransactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.ACTIVE)
            .count();
        int returnedBooks = (int) allTransactions.stream()
            .filter(t -> t.getStatus() == Transaction.TransactionStatus.RETURNED)
            .count();
        double totalFines = allTransactions.stream()
            .mapToDouble(Transaction::calculateFine)
            .sum();
        
        summaryPanel.add(new JLabel("Total Transactions: " + totalTransactions));
        summaryPanel.add(new JLabel("Active Loans: " + activeLoans));
        summaryPanel.add(new JLabel("Returned Books: " + returnedBooks));
        summaryPanel.add(new JLabel(String.format("Total Fines: $%.2f", totalFines)));
        
        // Buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton closeButton = new JButton("Close");
        JButton exportButton = new JButton("Export to Text");
        
        closeButton.addActionListener(e -> dialog.dispose());
        exportButton.addActionListener(e -> exportTransactionHistory(allTransactions));
        
        buttonPanel.add(closeButton);
        buttonPanel.add(exportButton);
        
        dialog.add(summaryPanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    private void exportTransactionHistory(List<Transaction> transactions) {
        try {
            String filename = "transaction_history_" + currentUser.getUsername() + ".txt";
            java.io.FileWriter writer = new java.io.FileWriter(filename);
            
            writer.write("TRANSACTION HISTORY FOR: " + currentUser.getFullName() + "\n");
            writer.write("Generated on: " + java.time.LocalDateTime.now() + "\n");
            writer.write("=".repeat(80) + "\n\n");
            
            for (Transaction transaction : transactions) {
                writer.write("Transaction ID: " + transaction.getTransactionId() + "\n");
                writer.write("Book: " + transaction.getBook().getTitle() + " by " + transaction.getBook().getAuthor() + "\n");
                writer.write("Borrow Date: " + transaction.getBorrowDate().toLocalDate() + "\n");
                writer.write("Due Date: " + transaction.getDueDate().toLocalDate() + "\n");
                
                if (transaction.getReturnDate() != null) {
                    writer.write("Return Date: " + transaction.getReturnDate().toLocalDate() + "\n");
                }
                
                writer.write("Status: " + transaction.getStatus() + "\n");
                writer.write("Fine: $" + String.format("%.2f", transaction.calculateFine()) + "\n");
                
                if (transaction.isOverdue()) {
                    writer.write("Days Overdue: " + transaction.getDaysOverdue() + "\n");
                }
                
                writer.write("-".repeat(40) + "\n");
            }
            
            writer.close();
            JOptionPane.showMessageDialog(this, 
                "Transaction history exported to: " + filename, 
                "Export Successful", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Failed to export: " + e.getMessage(), 
                "Export Failed", 
                JOptionPane.ERROR_MESSAGE);
        }
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

    private void forceRefreshMyBooksTable() {
        System.out.println("Force refreshing My Books table...");
        
        // Clear the table
        myBooksTableModel.setRowCount(0);
        System.out.println("Table cleared, row count: " + myBooksTableModel.getRowCount());
        
        // Get fresh data directly from database
        List<Transaction> freshTransactions = libraryManager.getDatabaseManager().getUserTransactionsFromDatabase(currentUser.getUserId());
        System.out.println("Retrieved " + freshTransactions.size() + " fresh transactions from database");
        
        // Check what's actually in the database for return dates
        System.out.println("Checking database return dates before processing...");
        checkDatabaseReturnDates();
        
        // Populate table with fresh data
        for (Transaction transaction : freshTransactions) {
            System.out.println("=== PROCESSING TRANSACTION ===");
            System.out.println("Transaction ID: " + transaction.getTransactionId());
            System.out.println("Status: " + transaction.getStatus());
            System.out.println("Return Date Object: " + transaction.getReturnDate());
            System.out.println("Return Date Type: " + (transaction.getReturnDate() != null ? transaction.getReturnDate().getClass().getName() : "NULL"));
            
            String status = transaction.getStatus().toString();
            if (transaction.getStatus() == Transaction.TransactionStatus.ACTIVE && transaction.isOverdue()) {
                status = "OVERDUE";
            }
            
            String returnDate = "";
            if (transaction.getReturnDate() != null) {
                returnDate = transaction.getReturnDate().toLocalDate().toString();
                System.out.println("✓ Transaction " + transaction.getTransactionId() + " has return date: " + returnDate);
            } else {
                System.out.println("✗ Transaction " + transaction.getTransactionId() + " has NO return date");
            }
            
            Object[] row = {
                transaction.getTransactionId(),
                transaction.getBook().getTitle(),
                transaction.getBorrowDate().toLocalDate().toString(),
                transaction.getDueDate().toLocalDate().toString(),
                status,
                String.format("$%.2f", transaction.calculateFine()),
                returnDate
            };
            
            myBooksTableModel.addRow(row);
            System.out.println("Added row for transaction " + transaction.getTransactionId() + 
                             " with return date: '" + returnDate + "'");
            System.out.println("=== END PROCESSING TRANSACTION ===");
        }
        
        // Force table refresh with multiple methods to ensure update
        myBooksTableModel.fireTableDataChanged();
        myBooksTableModel.fireTableStructureChanged();
        
        // Also refresh the table view directly
        myBooksTable.revalidate();
        myBooksTable.repaint();
        
        System.out.println("Table refreshed with " + freshTransactions.size() + " transactions");
        System.out.println("Final table row count: " + myBooksTableModel.getRowCount());
        
        // Debug: Print the actual table data to verify
        System.out.println("=== TABLE DATA VERIFICATION ===");
        for (int i = 0; i < myBooksTableModel.getRowCount(); i++) {
            String transId = (String) myBooksTableModel.getValueAt(i, 0);
            String returnDateCol = (String) myBooksTableModel.getValueAt(i, 6);
            System.out.println("Row " + i + ": Transaction " + transId + " Return Date: '" + returnDateCol + "'");
        }
        System.out.println("=== END TABLE DATA VERIFICATION ===");
    }

    private void forceRefreshAllDataFromDatabase() {
        System.out.println("Force refreshing all data from database...");
        
        // Refresh user data and welcome label
        refreshUserDataFromDatabase();
        refreshWelcomeLabel();
        
        // Refresh books table with availability data
        forceRefreshBookAvailability();
        
        // Refresh my books table
        forceRefreshMyBooksTable();
        
        // Refresh borrowing status
        updateBorrowingStatus();
        
        System.out.println("All data refreshed from database");
    }

    private void refreshWelcomeLabel() {
        // Get fresh user data from database
        User freshUser = libraryManager.getDatabaseManager().getUser(currentUser.getUserId());
        if (freshUser != null) {
            this.currentUser = freshUser;
            welcomeLabel.setText("Welcome, " + currentUser.getFullName() + " (Member)");
            System.out.println("Welcome label refreshed with user: " + currentUser.getFullName());
        }
    }

    private void verifyTransactionUpdate(String transactionId) {
        System.out.println("Verifying transaction update in database for: " + transactionId);
        
        // Get fresh transaction data from database
        Transaction updatedTransaction = libraryManager.getDatabaseManager().getTransaction(transactionId);
        
        if (updatedTransaction != null) {
            System.out.println("Transaction found in database after update:");
            System.out.println("  - Status: " + updatedTransaction.getStatus());
            System.out.println("  - Return Date: " + updatedTransaction.getReturnDate());
            System.out.println("  - Fine Amount: " + updatedTransaction.getFineAmount());
            
            if (updatedTransaction.getStatus() == Transaction.TransactionStatus.RETURNED) {
                System.out.println("✓ Transaction status successfully updated to RETURNED");
            } else {
                System.out.println("✗ Transaction status NOT updated - still " + updatedTransaction.getStatus());
            }
            
            if (updatedTransaction.getReturnDate() != null) {
                System.out.println("✓ Return date successfully set: " + updatedTransaction.getReturnDate());
            } else {
                System.out.println("✗ Return date NOT set in database");
            }
        } else {
            System.out.println("✗ Transaction not found in database after update!");
        }
        
        // Also check the raw database data directly
        System.out.println("=== RAW DATABASE VERIFICATION ===");
        try {
            String sql = "SELECT transaction_id, status, return_date, fine_amount FROM transactions WHERE transaction_id = ?";
            java.sql.Connection conn = libraryManager.getDatabaseManager().getDbConfig().getConnection();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, transactionId);
            java.sql.ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String dbStatus = rs.getString("status");
                String dbReturnDate = rs.getString("return_date");
                double dbFineAmount = rs.getDouble("fine_amount");
                
                System.out.println("Raw database data:");
                System.out.println("  - Status: " + dbStatus);
                System.out.println("  - Return Date: " + (dbReturnDate != null ? dbReturnDate : "NULL"));
                System.out.println("  - Fine Amount: " + dbFineAmount);
            } else {
                System.out.println("No raw database data found for transaction " + transactionId);
            }
            
            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            System.err.println("Error checking raw database data: " + e.getMessage());
        }
        System.out.println("=== END RAW DATABASE VERIFICATION ===");
    }

    private void checkDatabaseReturnDates() {
        System.out.println("=== CHECKING DATABASE RETURN DATES ===");
        
        try {
            String sql = "SELECT transaction_id, status, return_date FROM transactions WHERE user_id = ?";
            java.sql.Connection conn = libraryManager.getDatabaseManager().getDbConfig().getConnection();
            java.sql.PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, currentUser.getUserId());
            java.sql.ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                String transId = rs.getString("transaction_id");
                String status = rs.getString("status");
                String returnDate = rs.getString("return_date");
                
                System.out.println("DB Transaction: " + transId + 
                                 " | Status: " + status + 
                                 " | Return Date: " + (returnDate != null ? returnDate : "NULL"));
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (Exception e) {
            System.err.println("Error checking database return dates: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== END DATABASE RETURN DATE CHECK ===");
    }

    private void forceRefreshBookAvailability() {
        System.out.println("=== FORCE REFRESHING BOOK AVAILABILITY ===");
        
        try {
            // Clear the current book table
            bookTableModel.setRowCount(0);
            System.out.println("Book table cleared");
            
            // Get fresh book data directly from database
            List<Book> freshBooks = libraryManager.getDatabaseManager().getAllBooks();
            System.out.println("Retrieved " + freshBooks.size() + " fresh books from database");
            
            // Populate table with fresh data
            for (Book book : freshBooks) {
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
                    book.getAvailableCopies(),
                    book.getTotalCopies(),
                    book.isActive() ? "Available" : "Unavailable",
                    book.isAvailable() ? "Borrow" : "Unavailable"
                };
                
                bookTableModel.addRow(row);
                System.out.println("Added book row: " + book.getTitle() + 
                                 " (Available: " + book.getAvailableCopies() + 
                                 "/" + book.getTotalCopies() + ")");
            }
            
            // Force table refresh
            bookTableModel.fireTableDataChanged();
            bookTable.revalidate();
            bookTable.repaint();
            
            System.out.println("Book availability refresh completed. Table now has " + bookTableModel.getRowCount() + " rows");
            
        } catch (Exception e) {
            System.err.println("ERROR in forceRefreshBookAvailability: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== END BOOK AVAILABILITY REFRESH ===");
    }
}
