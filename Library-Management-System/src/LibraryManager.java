import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * LibraryManager class - Main business logic controller for the Library Management System
 * Implements singleton pattern for centralized data management with SQLite database
 */
public class LibraryManager {
    private static LibraryManager instance;
    
    // Data collections (kept for caching)
    private Map<String, Book> books;
    private Map<String, User> users;
    private Map<String, Transaction> transactions;
    
    // ID generators
    private int nextBookId = 1;
    private int nextUserId = 1;
    private int nextTransactionId = 1;
    
    // Database manager
    private DatabaseManager databaseManager;
    
    // Private constructor for singleton pattern
    private LibraryManager() {
        books = new HashMap<>();
        users = new HashMap<>();
        transactions = new HashMap<>();
        
        // Initialize database connection
        databaseManager = new DatabaseManager();
        
        // Initialize database and load data
        initializeDatabase();
    }
    
    // Initialize database and load data
    private void initializeDatabase() {
        try {
            // Test database connection
            if (databaseManager.getDbConfig().testConnection()) {
                // Always ensure tables exist first
                databaseManager.getDbConfig().createTables();
                
                // Check if database already has data
                boolean hasExistingData = checkIfDatabaseHasData();
                
                if (hasExistingData) {
                    // Database exists and has data, just load it
                    System.out.println("Database already exists with data. Loading existing data...");
                    loadDataFromDatabase();
                } else {
                    // Database exists but is empty, initialize with sample data
                    System.out.println("Database exists but is empty. Initializing with sample data...");
                    databaseManager.initializeSampleData();
                    loadDataFromDatabase();
                }
                
                System.out.println("Database connection established successfully!");
            } else {
                System.err.println("Failed to connect to SQLite database. Please check file permissions.");
                // Fall back to sample data
                initializeSampleData();
            }
        } catch (Exception e) {
            System.err.println("Error initializing database: " + e.getMessage());
            // Fall back to sample data
            initializeSampleData();
        }
    }
    
    // Check if database already has data
    private boolean checkIfDatabaseHasData() {
        try {
            // Try to load data from database
            List<User> userList = databaseManager.getAllUsers();
            List<Book> bookList = databaseManager.getAllBooks();
            
            // If we can get data and it's not empty, database has existing data
            return !userList.isEmpty() || !bookList.isEmpty();
        } catch (Exception e) {
            // If there's an error, database is empty
            System.out.println("Database appears to be empty.");
            return false;
        }
    }
    
    // Load data from database into memory
    private void loadDataFromDatabase() {
        try {
            // Load users
            List<User> userList = databaseManager.getAllUsers();
            users.clear();
            for (User user : userList) {
                users.put(user.getUserId(), user);
                // Update ID counter
                int userIdNum = Integer.parseInt(user.getUserId().substring(1));
                if (userIdNum >= nextUserId) {
                    nextUserId = userIdNum + 1;
                }
            }
            
            // Load books
            List<Book> bookList = databaseManager.getAllBooks();
            books.clear();
            for (Book book : bookList) {
                books.put(book.getBookId(), book);
                // Update ID counter
                int bookIdNum = Integer.parseInt(book.getBookId().substring(1));
                if (bookIdNum >= nextBookId) {
                    nextBookId = bookIdNum + 1;
                }
            }
            
            // Load transactions
            List<Transaction> transactionList = databaseManager.getAllTransactions();
            transactions.clear();
            for (Transaction transaction : transactionList) {
                if (transaction != null) {
                    transactions.put(transaction.getTransactionId(), transaction);
                    // Update ID counter
                    int transactionIdNum = Integer.parseInt(transaction.getTransactionId().substring(1));
                    if (transactionIdNum >= nextTransactionId) {
                        nextTransactionId = transactionIdNum + 1;
                    }
                }
            }
            
            // Refresh book availability based on current transactions
            refreshBookAvailability();
            
            System.out.println("Data loaded from database successfully!");
        } catch (Exception e) {
            System.err.println("Error loading data from database: " + e.getMessage());
        }
    }
    
    // Method to refresh book availability based on current transactions
    private void refreshBookAvailability() {
        for (Book book : books.values()) {
            int borrowedCopies = 0;
            for (Transaction transaction : transactions.values()) {
                if (transaction.getBook().getBookId().equals(book.getBookId()) && 
                    transaction.getStatus() == Transaction.TransactionStatus.ACTIVE) {
                    borrowedCopies++;
                }
            }
            int availableCopies = book.getTotalCopies() - borrowedCopies;
            book.setAvailableCopies(availableCopies);
        }
    }
    
    // Public method to refresh book availability
    public void refreshAllBooksAvailability() {
        refreshBookAvailability();
    }
    
    // Singleton instance getter
    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }
    
    // Save all data to database
    public void saveData() {
        try {
            // Save all books with updated availability
            for (Book book : books.values()) {
                databaseManager.updateBook(book);
            }
            
            // Save all users
            for (User user : users.values()) {
                databaseManager.updateUser(user);
            }
            
            // Save all transactions (including completed ones for history)
            for (Transaction transaction : transactions.values()) {
                // Always save transactions to maintain complete history
                if (transaction.getStatus() == Transaction.TransactionStatus.ACTIVE) {
                    databaseManager.updateTransaction(transaction);
                } else if (transaction.getStatus() == Transaction.TransactionStatus.RETURNED) {
                    // Ensure returned transactions are also saved for history
                    databaseManager.updateTransaction(transaction);
                }
            }
            
            System.out.println("All data saved to database successfully!");
        } catch (Exception e) {
            System.err.println("Error saving data to database: " + e.getMessage());
        }
    }
    
    // Method to refresh all data from database
    public void refreshDataFromDatabase() {
        try {
            loadDataFromDatabase();
            System.out.println("Data refreshed from database successfully!");
        } catch (Exception e) {
            System.err.println("Error refreshing data from database: " + e.getMessage());
        }
    }
    
    // Method to automatically sync all data with database
    public void autoSyncWithDatabase() {
        try {
            System.out.println("Starting automatic database synchronization...");
            
            // Save all current in-memory data to database first
            saveData();
            
            // Then refresh from database to ensure consistency
            refreshDataFromDatabase();
            
            System.out.println("Automatic database synchronization completed successfully!");
        } catch (Exception e) {
            System.err.println("Error during automatic database synchronization: " + e.getMessage());
        }
    }
    
    // Initialize with sample data for testing
    private void initializeSampleData() {
        // Create sample users
        User admin = new User("U001", "admin", "admin123", "System Administrator", "admin@library.com", User.UserRole.ADMIN, 0);
        User librarian = new User("U002", "librarian", "lib123", "John Librarian", "librarian@library.com", User.UserRole.LIBRARIAN, 0);
        User member1 = new User("U003", "member1", "mem123", "Alice Member", "alice@email.com", User.UserRole.MEMBER, 5);
        User member2 = new User("U004", "member2", "mem123", "Bob Member", "bob@email.com", User.UserRole.MEMBER, 5);
        
        users.put(admin.getUserId(), admin);
        users.put(librarian.getUserId(), librarian);
        users.put(member1.getUserId(), member1);
        users.put(member2.getUserId(), member2);
        
        // Create sample books with correct constructor signature
        // Parameters: bookId, title, author, isbn, category, totalCopies, description, publicationDate, publisher
        Book book1 = new Book("B001", "Java Programming", "John Smith", "978-0-123456-78-9", "Programming", 3, "Comprehensive guide to Java programming", new java.util.Calendar.Builder().setDate(2023, 0, 1).build().getTime(), "Tech Books");
        Book book2 = new Book("B002", "Data Structures", "Jane Doe", "978-0-987654-32-1", "Computer Science", 2, "Fundamental data structures and algorithms", new java.util.Calendar.Builder().setDate(2022, 0, 1).build().getTime(), "Academic Press");
        Book book3 = new Book("B003", "Web Development", "Mike Johnson", "978-0-555555-55-5", "Programming", 4, "Modern web development techniques", new java.util.Calendar.Builder().setDate(2024, 0, 1).build().getTime(), "Web Publishers");
        Book book4 = new Book("B004", "Database Design", "Sarah Wilson", "978-0-111111-11-1", "Computer Science", 2, "Database design principles and practices", new java.util.Calendar.Builder().setDate(2021, 0, 1).build().getTime(), "Data Books");
        
        books.put(book1.getBookId(), book1);
        books.put(book2.getBookId(), book2);
        books.put(book3.getBookId(), book3);
        books.put(book4.getBookId(), book4);
        
        System.out.println("Sample data initialized successfully!");
        System.out.println("Note: No initial transactions created - database starts clean");
    }
    
    // Method to update user book capacity
    public boolean updateUserBookCapacity(String userId, int newCapacity) {
        User user = users.get(userId);
        if (user != null && user.getRole() == User.UserRole.MEMBER) {
            user.setBookCapacity(newCapacity);
            // Update in database
            if (databaseManager.updateUser(user)) {
                // Automatically refresh data from database to ensure consistency
                refreshDataFromDatabase();
                
                System.out.println("User " + user.getUsername() + " book capacity automatically updated in database and synchronized.");
                return true;
            }
        }
        return false;
    }
    
    // Method to get user capacity information for admin dashboard
    public String getUserCapacityInfo(String userId) {
        User user = users.get(userId);
        if (user != null && user.getRole() == User.UserRole.MEMBER) {
            int activeLoans = user.getActiveLoansCount();
            int capacity = user.getBookCapacity();
            int remaining = user.getRemainingBorrowingSlots();
            return String.format("Capacity: %d | Active Loans: %d | Remaining: %d", capacity, activeLoans, remaining);
        }
        return "N/A (Not a member)";
    }
    
    // Method to get user by ID
    public User getUser(String userId) {
        return users.get(userId);
    }
    
    // Book Management Methods
    public Book addBook(String title, String author, String isbn, String category, 
                       int totalCopies, String description, java.util.Date publicationDate, String publisher) {
        String bookId = "B" + String.format("%03d", nextBookId++);
        Book book = new Book(bookId, title, author, isbn, category, totalCopies, description, publicationDate, publisher);
        
        // Save to database
        if (databaseManager.addBook(book)) {
            books.put(bookId, book);
            
            // Automatically refresh data from database to ensure consistency
            refreshDataFromDatabase();
            
            System.out.println("Book " + title + " automatically saved to database and synchronized.");
            return book;
        }
        return null;
    }
    
    public boolean updateBook(String bookId, String title, String author, String isbn, String category, 
                             int totalCopies, String description, java.util.Date publicationDate, String publisher) {
        Book book = books.get(bookId);
        if (book != null) {
            // Since Book class doesn't have individual setters, we need to create a new Book instance
            // and replace the old one in our collections
            Book updatedBook = new Book(bookId, title, author, isbn, category, totalCopies, description, publicationDate, publisher);
            
            // Copy over the borrowed copies count to maintain transaction integrity
            updatedBook.setBorrowedCopies(book.getBorrowedCopies());
            
            // Update in database
            boolean success = databaseManager.updateBook(updatedBook);
            if (success) {
                // Replace the old book with the updated one
                books.put(bookId, updatedBook);
                
                // Automatically refresh data from database to ensure consistency
                refreshDataFromDatabase();
                
                System.out.println("Book " + title + " automatically updated in database and synchronized.");
            }
            return success;
        }
        return false;
    }
    
    public boolean deleteBook(String bookId) {
        Book book = books.get(bookId);
        if (book != null && book.getBorrowedCopies() == 0) {
            // Delete from database first
            if (databaseManager.deleteBook(bookId)) {
                // Remove from in-memory collection only after successful database deletion
                books.remove(bookId);
                
                // Automatically refresh data from database to ensure consistency
                refreshDataFromDatabase();
                
                System.out.println("Book " + book.getTitle() + " automatically deleted from database and synchronized.");
                return true;
            }
        }
        return false;
    }
    
    public Book getBook(String bookId) {
        return books.get(bookId);
    }
    
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }
    
    public List<Book> searchBooks(String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new ArrayList<>(books.values());
        }
        
        String term = searchTerm.toLowerCase().trim();
        return books.values().stream()
                   .filter(book -> book.getTitle().toLowerCase().contains(term) ||
                                  book.getAuthor().toLowerCase().contains(term) ||
                                  book.getCategory().toLowerCase().contains(term) ||
                                  book.getIsbn().toLowerCase().contains(term))
                   .collect(Collectors.toList());
    }
    
    public List<Book> getAvailableBooks() {
        return books.values().stream()
                   .filter(Book::isAvailable)
                   .collect(Collectors.toList());
    }
    
    // User Management Methods
    public User addUser(String username, String password, String fullName, String email, User.UserRole role) {
        String userId = "U" + String.format("%03d", nextUserId++);
        User user = new User(userId, username, password, fullName, email, role);
        
        // Automatically save to database first
        if (databaseManager.addUser(user)) {
            // Add to in-memory collection only after successful database save
            users.put(userId, user);
            
            // Automatically refresh data from database to ensure consistency
            refreshDataFromDatabase();
            
            System.out.println("User " + username + " automatically saved to database and synchronized.");
            return user;
        } else {
            System.err.println("Failed to save user " + username + " to database.");
            return null;
        }
    }
    
    public boolean updateUser(String userId, String username, String password, String fullName, String email, User.UserRole role) {
        User oldUser = users.get(userId);
        if (oldUser != null) {
            // Create new user instance with updated information
            User updatedUser = new User(userId, username, password, fullName, email, role, oldUser.getBookCapacity());
            
            // Copy over transaction history to maintain data integrity
            for (Transaction transaction : oldUser.getTransactionHistory()) {
                updatedUser.addTransaction(transaction);
            }
            
            // Update in database
            boolean success = databaseManager.updateUser(updatedUser);
            if (success) {
                // Replace old user with updated one
                users.put(userId, updatedUser);
                
                // Automatically refresh data from database to ensure consistency
                refreshDataFromDatabase();
                
                System.out.println("User " + username + " automatically updated in database and synchronized.");
            }
            return success;
        }
        return false;
    }
    
    public boolean deleteUser(String userId) {
        User user = users.get(userId);
        if (user != null && user.getActiveLoansCount() == 0) {
            // Delete from database first
            if (databaseManager.deleteUser(userId)) {
                // Remove from in-memory collection only after successful database deletion
                users.remove(userId);
                
                // Automatically refresh data from database to ensure consistency
                refreshDataFromDatabase();
                
                System.out.println("User " + user.getUsername() + " automatically deleted from database and synchronized.");
                return true;
            }
        }
        return false;
    }
    
    public User getUserByUsername(String username) {
        return users.values().stream()
                   .filter(user -> user.getUsername().equals(username))
                   .findFirst()
                   .orElse(null);
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    public List<User> getActiveUsers() {
        return users.values().stream()
                   .filter(User::isActive)
                   .collect(Collectors.toList());
    }
    
    // Transaction Management Methods
    public Transaction borrowBook(String bookId, String userId) {
        System.out.println("=== LIBRARY MANAGER: BORROWING BOOK ===");
        System.out.println("Book ID: " + bookId + ", User ID: " + userId);
        
        Book book = books.get(bookId);
        User user = users.get(userId);
        
        if (book == null || user == null) {
            System.out.println("✗ Book or user not found");
            return null;
        }
        
        System.out.println("Book found: " + book.getTitle());
        System.out.println("User found: " + user.getUsername());
        System.out.println("Book availability before borrow:");
        System.out.println("  Total Copies: " + book.getTotalCopies());
        System.out.println("  Available Copies: " + book.getAvailableCopies());
        System.out.println("  Borrowed Copies: " + book.getBorrowedCopies());
        
        if (!book.isAvailable()) {
            System.out.println("✗ Book is not available for borrowing");
            return null;
        }
        
        if (user.getRole() != User.UserRole.MEMBER) {
            System.out.println("✗ User is not a member");
            return null;
        }
        
        // Check if user has reached their book capacity using database-driven checking
        if (user.hasReachedBorrowingLimitFromDatabase(databaseManager)) {
            System.out.println("✗ User " + user.getUsername() + " has reached their book capacity limit of " + 
                             user.getBookCapacity() + " books. Cannot borrow more books.");
            return null;
        }
        
        String transactionId = "T" + String.format("%03d", nextTransactionId++);
        Transaction transaction = new Transaction(transactionId, book, user);
        
        System.out.println("Transaction created: " + transactionId);
        
        // Add transaction to database first
        System.out.println("Adding transaction to database...");
        if (databaseManager.addTransaction(transaction)) {
            System.out.println("✓ Transaction added to database successfully");
            
            // Update in-memory collections
            System.out.println("Updating book availability in memory...");
            book.borrowCopy();
            
            System.out.println("Book availability after borrow:");
            System.out.println("  Total Copies: " + book.getTotalCopies());
            System.out.println("  Available Copies: " + book.getAvailableCopies());
            System.out.println("  Borrowed Copies: " + book.getBorrowedCopies());
            
            user.addTransaction(transaction);
            transactions.put(transactionId, transaction);
            
            // IMPORTANT: Update book availability in database immediately
            System.out.println("Updating book availability in database...");
            if (databaseManager.updateBook(book)) {
                System.out.println("✓ Book availability successfully updated in database");
                
                // Verify the update by fetching the book from database
                Book updatedBook = databaseManager.getBook(bookId);
                if (updatedBook != null) {
                    System.out.println("Database verification after update:");
                    System.out.println("  Total Copies: " + updatedBook.getTotalCopies());
                    System.out.println("  Available Copies: " + updatedBook.getAvailableCopies());
                    System.out.println("  Borrowed Copies: " + updatedBook.getBorrowedCopies());
                }
            } else {
                System.err.println("✗ Failed to update book availability in database");
            }
            
            // DO NOT call refreshDataFromDatabase() here as it would overwrite our changes
            // Instead, let the UI refresh from the database directly
            
            System.out.println("✓ Book borrowing transaction successfully processed and saved to database");
            System.out.println("=== END BORROW OPERATION ===");
            return transaction;
        } else {
            System.err.println("✗ Failed to add transaction to database");
            System.out.println("=== END BORROW OPERATION (FAILED) ===");
            return null;
        }
    }
    
    public boolean returnBook(String transactionId) {
        System.out.println("=== LIBRARY MANAGER: RETURNING BOOK ===");
        System.out.println("Transaction ID: " + transactionId);
        
        // First try to get transaction from memory
        Transaction transaction = transactions.get(transactionId);
        
        // If not in memory, get from database
        if (transaction == null) {
            System.out.println("Transaction not in memory, getting from database...");
            transaction = databaseManager.getTransaction(transactionId);
        }
        
        if (transaction != null && transaction.canBeReturned()) {
            System.out.println("✓ Transaction found and can be returned");
            System.out.println("Book: " + transaction.getBook().getTitle());
            System.out.println("User: " + transaction.getUser().getUsername());
            
            System.out.println("Book availability before return:");
            System.out.println("  Total Copies: " + transaction.getBook().getTotalCopies());
            System.out.println("  Available Copies: " + transaction.getBook().getAvailableCopies());
            System.out.println("  Borrowed Copies: " + transaction.getBook().getBorrowedCopies());
            
            // Process the return
            transaction.returnBook();
            System.out.println("✓ Return processed. New status: " + transaction.getStatus() + 
                             ", Return date: " + transaction.getReturnDate());
            
            System.out.println("Book availability after return:");
            System.out.println("  Total Copies: " + transaction.getBook().getTotalCopies());
            System.out.println("  Available Copies: " + transaction.getBook().getAvailableCopies());
            System.out.println("  Borrowed Copies: " + transaction.getBook().getBorrowedCopies());
            
            // Update transaction in database FIRST
            System.out.println("Updating transaction in database...");
            if (databaseManager.updateTransaction(transaction)) {
                System.out.println("✓ Transaction updated successfully in database");
                
                // Update book availability in database
                System.out.println("Updating book availability in database...");
                if (databaseManager.updateBook(transaction.getBook())) {
                    System.out.println("✓ Book availability updated successfully in database");
                    
                    // Verify the update by fetching the book from database
                    Book updatedBook = databaseManager.getBook(transaction.getBook().getBookId());
                    if (updatedBook != null) {
                        System.out.println("Database verification after return:");
                        System.out.println("  Total Copies: " + updatedBook.getTotalCopies());
                        System.out.println("  Available Copies: " + updatedBook.getAvailableCopies());
                        System.out.println("  Borrowed Copies: " + updatedBook.getBorrowedCopies());
                    }
                } else {
                    System.err.println("✗ Failed to update book availability in database");
                }
                
                // Update in-memory collections
                transactions.put(transactionId, transaction);
                System.out.println("✓ Transaction updated in memory collections");
                
                // DO NOT call refreshDataFromDatabase() here as it might overwrite our changes
                // Instead, let the UI refresh from the database directly
                
                System.out.println("✓ Book return transaction successfully processed and updated in database");
                System.out.println("=== END RETURN OPERATION ===");
                return true;
            } else {
                System.err.println("✗ Failed to update transaction in database");
                System.out.println("=== END RETURN OPERATION (FAILED) ===");
            }
        } else {
            if (transaction == null) {
                System.err.println("✗ Transaction " + transactionId + " not found");
            } else {
                System.err.println("✗ Transaction " + transactionId + " cannot be returned. Status: " + transaction.getStatus());
            }
            System.out.println("=== END RETURN OPERATION (FAILED) ===");
        }
        return false;
    }
    
    public Transaction getTransaction(String transactionId) {
        return transactions.get(transactionId);
    }
    
    public Transaction getTransactionFromDatabase(String transactionId) {
        // Get fresh transaction data from database
        return databaseManager.getTransaction(transactionId);
    }
    
    public List<Transaction> getAllTransactions() {
        return new ArrayList<>(transactions.values());
    }
    
    public List<Transaction> getUserTransactions(String userId) {
        // Always get fresh transactions from database to ensure latest data
        List<Transaction> dbTransactions = databaseManager.getUserTransactionsFromDatabase(userId);
        
        // Also get from memory for current session
        User user = users.get(userId);
        if (user != null) {
            List<Transaction> memoryTransactions = user.getTransactionHistory();
            
            // Merge and deduplicate transactions
            Map<String, Transaction> allTransactions = new HashMap<>();
            
            // Add database transactions first (they're more complete and up-to-date)
            for (Transaction t : dbTransactions) {
                allTransactions.put(t.getTransactionId(), t);
            }
            
            // Add memory transactions (they might have latest status)
            for (Transaction t : memoryTransactions) {
                allTransactions.put(t.getTransactionId(), t);
            }
            
            // Return database-first transactions to ensure latest data
            return new ArrayList<>(allTransactions.values());
        }
        
        return dbTransactions;
    }
    
    public List<Transaction> getOverdueTransactions() {
        return transactions.values().stream()
                         .filter(Transaction::isOverdue)
                         .collect(Collectors.toList());
    }
    
    public double calculateTotalFines() {
        return transactions.values().stream()
                         .mapToDouble(Transaction::calculateFine)
                         .sum();
    }
    
    // Authentication Methods
    public User authenticateUser(String username, String password) {
        User user = getUserByUsername(username);
        if (user != null && user.getPassword().equals(password) && user.isActive()) {
            return user;
        }
        return null;
    }
    
    // Statistics Methods
    public int getTotalBooks() {
        return books.size();
    }
    
    public int getTotalUsers() {
        return users.size();
    }
    
    public int getTotalTransactions() {
        return transactions.size();
    }
    
    public int getAvailableBooksCount() {
        return (int) books.values().stream().filter(Book::isAvailable).count();
    }
    
    public int getOverdueBooksCount() {
        return getOverdueTransactions().size();
    }

    // Getter for database manager
    public DatabaseManager getDatabaseManager() {
        return databaseManager;
    }
}
