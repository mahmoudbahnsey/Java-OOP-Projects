import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * User class represents a user entity in the Library Management System.
 * 
 * This class manages user information including authentication details, personal information,
 * role-based access control, and borrowing capacity management. Users can have different
 * roles (ADMIN, LIBRARIAN, MEMBER) with varying permissions and borrowing limits.
 * 
 * Key Features:
 * - Role-based access control (Admin, Librarian, Member)
 * - Configurable borrowing capacity for members
 * - Transaction history tracking
 * - Active/inactive status management
 * - Borrowing limit enforcement
 * 
 * @author Library Management System
 * @version 2.0
 * @since 1.0
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    // Unique identifier for the user
    private String userId;
    
    // Authentication credentials
    private String username;    // Login username (must be unique)
    private String password;    // Login password (stored as plain text for demo purposes)
    
    // Personal information
    private String fullName;    // User's full name
    private String email;       // User's email address
    
    // Role and status
    private UserRole role;      // User's role in the system (ADMIN, LIBRARIAN, MEMBER)
    private boolean active;     // Whether the user account is active
    
    // Borrowing capacity management (only applicable to MEMBER users)
    private int bookCapacity;   // Maximum number of books the user can borrow simultaneously
    
    // Transaction tracking
    private List<Transaction> transactionHistory; // List of all transactions (borrowed/returned books)

    /**
     * Constructor to create a new User instance with default book capacity.
     * Default book capacity is 5 for MEMBER users, 0 for ADMIN/LIBRARIAN users.
     * 
     * @param userId Unique identifier for the user
     * @param username Login username
     * @param password Login password
     * @param fullName User's full name
     * @param email User's email address
     * @param role User's role in the system
     */
    public User(String userId, String username, String password, String fullName, String email, UserRole role) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.active = true;  // New users start as active
        
        // Set default book capacity based on role
        this.bookCapacity = (role == UserRole.MEMBER) ? 5 : 0;
        
        // Initialize empty transaction history
        this.transactionHistory = new ArrayList<>();
    }

    /**
     * Constructor to create a new User instance with custom book capacity.
     * 
     * @param userId Unique identifier for the user
     * @param username Login username
     * @param password Login password
     * @param fullName User's full name
     * @param email User's email address
     * @param role User's role in the system
     * @param bookCapacity Custom borrowing capacity (only meaningful for MEMBER users)
     */
    public User(String userId, String username, String password, String fullName, String email, UserRole role, int bookCapacity) {
        this(userId, username, password, fullName, email, role);
        this.bookCapacity = bookCapacity;
    }

    // ==================== GETTER METHODS ====================
    
    /**
     * Gets the unique user identifier.
     * @return User ID string
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets the login username.
     * @return Username string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the login password.
     * @return Password string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the user's full name.
     * @return Full name string
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Gets the user's email address.
     * @return Email string
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the user's role in the system.
     * @return UserRole enum value
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Checks if the user account is active.
     * @return true if account is active, false otherwise
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Gets the user's borrowing capacity.
     * For MEMBER users, this represents the maximum number of books they can borrow simultaneously.
     * For ADMIN/LIBRARIAN users, this value is 0 (no borrowing limit).
     * 
     * @return Maximum number of books that can be borrowed
     */
    public int getBookCapacity() {
        return bookCapacity;
    }

    /**
     * Gets the user's complete transaction history.
     * This includes all books borrowed and returned, both active and completed transactions.
     * 
     * @return List of all transactions associated with this user
     */
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactionHistory); // Return a copy to prevent external modification
    }

    // ==================== SETTER METHODS ====================
    
    /**
     * Sets the user's borrowing capacity.
     * This method is primarily used by administrators to adjust member borrowing limits.
     * 
     * @param bookCapacity New borrowing capacity (must be non-negative)
     */
    public void setBookCapacity(int bookCapacity) {
        if (bookCapacity < 0) {
            throw new IllegalArgumentException("Book capacity cannot be negative");
        }
        this.bookCapacity = bookCapacity;
    }

    /**
     * Sets the user's active status.
     * @param active true if user should be active, false otherwise
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
     * Checks if the user can borrow books.
     * Only MEMBER users can borrow books.
     * @return true if user can borrow books, false otherwise
     */
    public boolean canBorrowBooks() {
        return role == UserRole.MEMBER;
    }

    // ==================== TRANSACTION MANAGEMENT METHODS ====================
    
    /**
     * Adds a new transaction to the user's history.
     * This method is called when a user borrows or returns a book.
     * 
     * @param transaction Transaction to add to history
     */
    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactionHistory.add(transaction);
        }
    }

    /**
     * Removes a transaction from the user's history.
     * This method is typically used for cleanup or transaction correction.
     * 
     * @param transaction Transaction to remove from history
     */
    public void removeTransaction(Transaction transaction) {
        if (transaction != null) {
            transactionHistory.remove(transaction);
        }
    }

    // ==================== BUSINESS LOGIC METHODS ====================
    
    /**
     * Calculates the number of books currently borrowed by the user.
     * This method counts only active (borrowed) transactions, not returned ones.
     * 
     * @return Number of books currently borrowed
     */
    public int getActiveLoansCount() {
        return (int) transactionHistory.stream()
                .filter(transaction -> transaction.getStatus() == Transaction.TransactionStatus.ACTIVE)
                .count();
    }

    /**
     * Checks if the user has reached their borrowing limit.
     * This method is only meaningful for MEMBER users who have a borrowing capacity.
     * 
     * @return true if user cannot borrow more books, false otherwise
     */
    public boolean hasReachedBorrowingLimit() {
        // Only MEMBER users have borrowing limits
        if (role != UserRole.MEMBER) {
            return false;
        }
        return getActiveLoansCount() >= bookCapacity;
    }

    /**
     * Calculates how many more books the user can borrow.
     * This method is only meaningful for MEMBER users who have a borrowing capacity.
     * 
     * @return Number of additional books that can be borrowed (0 or positive)
     */
    public int getRemainingBorrowingSlots() {
        // Only MEMBER users have borrowing limits
        if (role != UserRole.MEMBER) {
            return 0;
        }
        return Math.max(0, bookCapacity - getActiveLoansCount());
    }

    /**
     * Get the database-driven active loans count.
     * This method should be called to get the most accurate count
     * based on actual transaction data in the database.
     * 
     * @param databaseManager The database manager to query
     * @return Number of active loans from database
     */
    public int getActiveLoansCountFromDatabase(DatabaseManager databaseManager) {
        if (databaseManager != null) {
            return databaseManager.getActiveLoansCountFromDatabase(this.userId);
        }
        return getActiveLoansCount(); // Fallback to memory count
    }

    /**
     * Get the database-driven remaining borrowing slots.
     * This ensures accurate capacity checking based on real transaction data.
     * 
     * @param databaseManager The database manager to query
     * @return Number of remaining borrowing slots
     */
    public int getRemainingBorrowingSlotsFromDatabase(DatabaseManager databaseManager) {
        if (databaseManager != null) {
            int activeLoans = getActiveLoansCountFromDatabase(databaseManager);
            return Math.max(0, bookCapacity - activeLoans);
        }
        return getRemainingBorrowingSlots(); // Fallback to memory calculation
    }

    /**
     * Check if user has reached borrowing limit based on database data.
     * This ensures accurate limit enforcement.
     * 
     * @param databaseManager The database manager to query
     * @return True if borrowing limit reached
     */
    public boolean hasReachedBorrowingLimitFromDatabase(DatabaseManager databaseManager) {
        if (databaseManager != null) {
            int activeLoans = getActiveLoansCountFromDatabase(databaseManager);
            return activeLoans >= bookCapacity;
        }
        return hasReachedBorrowingLimit(); // Fallback to memory check
    }

    /**
     * Check if user can borrow books based on database data.
     * This ensures accurate borrowing permission checking.
     * 
     * @param databaseManager The database manager to query
     * @return True if user can borrow books
     */
    public boolean canBorrowBooksFromDatabase(DatabaseManager databaseManager) {
        if (databaseManager != null) {
            return active && !hasReachedBorrowingLimitFromDatabase(databaseManager);
        }
        return active && !hasReachedBorrowingLimit(); // Fallback to memory check
    }

    // ==================== UTILITY METHODS ====================
    
    /**
     * Returns a string representation of the user.
     * Format: "FullName (Username) - Role - Active: Yes/No"
     * 
     * @return Formatted string representation
     */
    @Override
    public String toString() {
        return String.format("%s (%s) - %s - Active: %s", 
                           fullName, username, role, active ? "Yes" : "No");
    }

    // ==================== INNER ENUM ====================
    
    /**
     * Enum representing the different roles a user can have in the system.
     * Each role has different permissions and capabilities.
     */
    public enum UserRole {
        /**
         * Administrator role with full system access.
         * Can manage all books, users, and transactions.
         */
        ADMIN,
        
        /**
         * Librarian role with book and transaction management access.
         * Can manage books and process borrowing/returning operations.
         */
        LIBRARIAN,
        
        /**
         * Member role with limited access for borrowing books.
         * Can view available books and manage their own borrowing history.
         */
        MEMBER
    }
}
