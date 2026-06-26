import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.io.Serializable;

/**
 * Transaction class represents a book borrowing and returning transaction in the Library Management System.
 * 
 * This class manages the complete lifecycle of a book transaction, from initial borrowing to final return.
 * It tracks important dates (borrow, due, return), calculates fines for overdue books, and maintains
 * transaction status throughout the process.
 * 
 * Key Features:
 * - Complete transaction lifecycle management (borrow → return)
 * - Automatic due date calculation (default 14 days from borrow)
 * - Fine calculation for overdue books ($1 per day)
 * - Transaction status tracking (Active, Returned, Overdue, Cancelled)
 * - Comprehensive transaction history and notes
 * 
 * @author Library Management System
 * @version 2.0
 * @since 1.0
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Transaction identification
    private String transactionId;    // Unique identifier for the transaction
    
    // Associated entities
    private Book book;               // Book being borrowed/returned
    private User user;               // User borrowing/returning the book
    
    // Temporal information
    private LocalDateTime borrowDate; // When the book was borrowed
    private LocalDateTime dueDate;    // When the book is due to be returned
    private LocalDateTime returnDate; // When the book was actually returned (null if not returned)
    
    // Transaction metadata
    private TransactionType type;    // Type of transaction (BORROW or RETURN)
    private TransactionStatus status; // Current status of the transaction
    private double fineAmount;       // Fine amount if book is overdue
    private String notes;            // Additional notes about the transaction
    
    // Constants for fine calculation and business rules
    private static final double DAILY_FINE_RATE = 1.0;    // Fine amount per day overdue ($1)
    private static final int DEFAULT_BORROW_DAYS = 14;    // Default borrowing period (2 weeks)

    // ==================== INNER ENUMS ====================
    
    /**
     * Enum representing the type of transaction.
     * Transactions can be either borrowing a book or returning a book.
     */
    public enum TransactionType {
        /**
         * Transaction type for borrowing a book from the library.
         */
        BORROW("Borrow"),
        
        /**
         * Transaction type for returning a book to the library.
         */
        RETURN("Return");
        
        private final String displayName;
        
        TransactionType(String displayName) {
            this.displayName = displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    /**
     * Enum representing the current status of a transaction.
     * Status changes as the transaction progresses through its lifecycle.
     */
    public enum TransactionStatus {
        /**
         * Book is currently borrowed and not yet returned.
         */
        ACTIVE("Active"),
        
        /**
         * Book has been successfully returned to the library.
         */
        RETURNED("Returned"),
        
        /**
         * Book is overdue (past due date) and not yet returned.
         */
        OVERDUE("Overdue"),
        
        /**
         * Transaction has been cancelled (e.g., due to error or user request).
         */
        CANCELLED("Cancelled");
        
        private final String displayName;
        
        TransactionStatus(String displayName) {
            this.displayName = displayName;
        }
        
        @Override
        public String toString() {
            return displayName;
        }
    }
    
    // ==================== CONSTRUCTORS ====================
    
    /**
     * Constructor for creating a new borrowing transaction.
     * This constructor is used when a user borrows a book from the library.
     * 
     * @param transactionId Unique identifier for the transaction
     * @param book Book being borrowed
     * @param user User borrowing the book
     */
    public Transaction(String transactionId, Book book, User user) {
        this.transactionId = transactionId;
        this.book = book;
        this.user = user;
        
        // Set temporal information
        this.borrowDate = LocalDateTime.now();
        this.dueDate = this.borrowDate.plusDays(DEFAULT_BORROW_DAYS);
        this.returnDate = null;  // Not returned yet
        
        // Set transaction metadata
        this.type = TransactionType.BORROW;
        this.status = TransactionStatus.ACTIVE;
        this.fineAmount = 0.0;   // No fine initially
        this.notes = "";          // Empty notes initially
    }
    
    /**
     * Constructor for creating a return transaction.
     * This constructor is used when a user returns a book to the library.
     * 
     * @param transactionId Unique identifier for the transaction
     * @param book Book being returned
     * @param user User returning the book
     * @param returnDate When the book was returned
     */
    public Transaction(String transactionId, Book book, User user, LocalDateTime returnDate) {
        this.transactionId = transactionId;
        this.book = book;
        this.user = user;
        this.returnDate = returnDate;
        
        // Set transaction metadata for return
        this.type = TransactionType.RETURN;
        this.status = TransactionStatus.RETURNED;
        this.fineAmount = 0.0;   // Fine will be calculated if overdue
        this.notes = "";
    }
    
    // ==================== GETTER METHODS ====================
    
    /**
     * Gets the unique transaction identifier.
     * @return Transaction ID string
     */
    public String getTransactionId() { 
        return transactionId; 
    }
    
    /**
     * Sets the transaction identifier.
     * @param transactionId New transaction ID
     */
    public void setTransactionId(String transactionId) { 
        this.transactionId = transactionId; 
    }
    
    /**
     * Gets the book associated with this transaction.
     * @return Book object
     */
    public Book getBook() { 
        return book; 
    }
    
    /**
     * Sets the book for this transaction.
     * @param book Book object
     */
    public void setBook(Book book) { 
        this.book = book; 
    }
    
    /**
     * Gets the user associated with this transaction.
     * @return User object
     */
    public User getUser() { 
        return user; 
    }
    
    /**
     * Sets the user for this transaction.
     * @param user User object
     */
    public void setUser(User user) { 
        this.user = user; 
    }
    
    /**
     * Gets the date when the book was borrowed.
     * @return Borrow date
     */
    public LocalDateTime getBorrowDate() { 
        return borrowDate; 
    }
    
    /**
     * Sets the borrow date for this transaction.
     * @param borrowDate Borrow date
     */
    public void setBorrowDate(LocalDateTime borrowDate) { 
        this.borrowDate = borrowDate; 
    }
    
    /**
     * Gets the date when the book is due to be returned.
     * @return Due date
     */
    public LocalDateTime getDueDate() { 
        return dueDate; 
    }
    
    /**
     * Sets the due date for this transaction.
     * @param dueDate Due date
     */
    public void setDueDate(LocalDateTime dueDate) { 
        this.dueDate = dueDate; 
    }
    
    /**
     * Gets the date when the book was actually returned.
     * @return Return date (null if not yet returned)
     */
    public LocalDateTime getReturnDate() { 
        return returnDate; 
    }
    
    /**
     * Sets the return date for this transaction.
     * @param returnDate Return date
     */
    public void setReturnDate(LocalDateTime returnDate) { 
        this.returnDate = returnDate; 
    }
    
    /**
     * Gets the current status of the transaction.
     * @return Transaction status
     */
    public TransactionStatus getStatus() { 
        return status; 
    }
    
    /**
     * Sets the status of the transaction.
     * @param status New transaction status
     */
    public void setStatus(TransactionStatus status) { 
        this.status = status; 
    }
    
    /**
     * Gets the fine amount for this transaction.
     * @return Fine amount in dollars
     */
    public double getFine() { 
        return fineAmount; 
    }
    
    /**
     * Sets the fine amount for this transaction.
     * @param fineAmount Fine amount in dollars
     */
    public void setFine(double fineAmount) { 
        this.fineAmount = fineAmount; 
    }
    
    /**
     * Gets the transaction type (BORROW or RETURN).
     * @return Transaction type
     */
    public TransactionType getType() { 
        return type; 
    }
    
    /**
     * Sets the transaction type.
     * @param type Transaction type
     */
    public void setType(TransactionType type) { 
        this.type = type; 
    }
    
    /**
     * Gets the notes associated with this transaction.
     * @return Transaction notes
     */
    public String getNotes() { 
        return notes; 
    }
    
    /**
     * Sets the notes for this transaction.
     * @param notes Transaction notes
     */
    public void setNotes(String notes) { 
        this.notes = notes; 
    }

    /**
     * Gets the fine amount (alias for getFine for backward compatibility).
     * @return Fine amount
     */
    public double getFineAmount() {
        return fineAmount;
    }

    /**
     * Sets the fine amount for the transaction.
     * @param fineAmount New fine amount
     */
    public void setFineAmount(double fineAmount) {
        this.fineAmount = fineAmount;
    }

    /**
     * Gets a human-readable description of the transaction status.
     * @return Status description string
     */
    public String getStatusDescription() {
        switch (status) {
            case ACTIVE:
                return "Borrowed";
            case RETURNED:
                return "Returned";
            case OVERDUE:
                return "Overdue";
            case CANCELLED:
                return "Cancelled";
            default:
                return "Unknown";
        }
    }

    // ==================== BUSINESS LOGIC METHODS ====================
    
    /**
     * Checks if the book is overdue.
     * A book is overdue if the current date is past the due date and it hasn't been returned.
     * 
     * @return true if book is overdue, false otherwise
     */
    public boolean isOverdue() {
        // Book is overdue if it's past due date and not returned
        return LocalDateTime.now().isAfter(dueDate) && returnDate == null;
    }
    
    /**
     * Checks if the book can be returned.
     * A book can be returned if it's currently borrowed (ACTIVE status).
     * 
     * @return true if book can be returned, false otherwise
     */
    public boolean canBeReturned() {
        return status == TransactionStatus.ACTIVE;
    }
    
    /**
     * Processes the return of a book.
     * This method updates the transaction status to RETURNED and sets the return date.
     * It also calculates any overdue fines if applicable.
     */
    public void returnBook() {
        if (canBeReturned()) {
            this.returnDate = LocalDateTime.now();
            this.status = TransactionStatus.RETURNED;
            
            // Calculate fine if book is overdue
            if (isOverdue()) {
                this.fineAmount = calculateFine();
            }
        }
    }
    
    /**
     * Calculates the fine amount for an overdue book.
     * Fine is calculated as $1 per day overdue, starting from the day after the due date.
     * 
     * @return Fine amount in dollars
     */
    public double calculateFine() {
        if (returnDate == null || !returnDate.isAfter(dueDate)) {
            return 0.0; // No fine if not returned or not overdue
        }
        
        // Calculate days overdue (excluding the due date itself)
        long daysOverdue = ChronoUnit.DAYS.between(dueDate, returnDate);
        return Math.max(0, daysOverdue) * DAILY_FINE_RATE;
    }
    
    /**
     * Calculates the number of days the book is overdue.
     * Returns 0 if the book is not overdue.
     * 
     * @return Number of days overdue (0 if not overdue)
     */
    public int getDaysOverdue() {
        if (returnDate == null) {
            // Book not returned yet, check if current date is past due
            if (LocalDateTime.now().isAfter(dueDate)) {
                return (int) ChronoUnit.DAYS.between(dueDate, LocalDateTime.now());
            }
            return 0;
        } else {
            // Book returned, calculate days between due date and return date
            return (int) ChronoUnit.DAYS.between(dueDate, returnDate);
        }
    }

    // ==================== UTILITY METHODS ====================
    
    /**
     * Returns a string representation of the transaction.
     * Format: "Transaction{id='transactionId', book='bookTitle', user='username', status='status'}"
     * 
     * @return Formatted string representation
     */
    @Override
    public String toString() {
        return String.format("Transaction{id='%s', book='%s', user='%s', status='%s'}", 
                           transactionId, book.getTitle(), user.getUsername(), status);
    }
    
    /**
     * Checks if this transaction equals another object.
     * Two transactions are equal if they have the same transaction ID.
     * 
     * @param obj Object to compare with
     * @return true if objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction that = (Transaction) obj;
        return Objects.equals(transactionId, that.transactionId);
    }
    
    /**
     * Generates a hash code for this transaction.
     * Hash code is based on the transaction ID.
     * 
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}
