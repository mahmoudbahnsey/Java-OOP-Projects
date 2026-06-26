import java.util.Date;

/**
 * Book class represents a book entity in the Library Management System.
 * 
 * This class manages book information including basic details (title, author, ISBN),
 * inventory management (total copies, available copies, borrowed copies), and
 * provides methods for book availability calculations and status updates.
 * 
 * Key Features:
 * - Book inventory tracking with automatic availability calculation
 * - Dynamic calculation of available copies based on total and borrowed copies
 * - Book borrowing and return operations
 * - Availability percentage calculation for reporting
 * 
 * @author Library Management System
 * @version 2.0
 * @since 1.0
 */
public class Book {
    // Unique identifier for the book
    private String bookId;
    
    // Basic book information
    private String title;
    private String author;
    private String isbn;        // International Standard Book Number
    private String category;    // Book genre/category (e.g., Fiction, Science, History)
    
    // Inventory management fields
    private int totalCopies;    // Total number of copies owned by the library
    private int availableCopies; // Number of copies currently available for borrowing
    private int borrowedCopies;  // Number of copies currently borrowed
    
    // Additional book details
    private String description;     // Book description or summary
    private Date publicationDate;   // When the book was published
    private String publisher;       // Publishing company name

    /**
     * Constructor to create a new Book instance.
     * 
     * @param bookId Unique identifier for the book
     * @param title Book title
     * @param author Book author
     * @param isbn International Standard Book Number
     * @param category Book category/genre
     * @param totalCopies Total number of copies available
     * @param description Book description
     * @param publicationDate Book publication date
     * @param publisher Publishing company
     */
    public Book(String bookId, String title, String author, String isbn, String category, 
                int totalCopies, String description, Date publicationDate, String publisher) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.totalCopies = totalCopies;
        this.description = description;
        this.publicationDate = publicationDate;
        this.publisher = publisher;
        
        // Initialize available copies to total copies (all copies start as available)
        this.availableCopies = totalCopies;
        this.borrowedCopies = 0;
    }

    // ==================== GETTER METHODS ====================
    
    /**
     * Gets the unique book identifier.
     * @return Book ID string
     */
    public String getBookId() {
        return bookId;
    }

    /**
     * Gets the book title.
     * @return Book title string
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the book author.
     * @return Author name string
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets the book ISBN.
     * @return ISBN string
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Gets the book category.
     * @return Category string
     */
    public String getCategory() {
        return category;
    }

    /**
     * Gets the total number of copies owned by the library.
     * @return Total copies count
     */
    public int getTotalCopies() {
        return totalCopies;
    }

    /**
     * Gets the number of copies currently available for borrowing.
     * This value is stored and maintained by the system.
     * @return Available copies count
     */
    public int getAvailableCopies() {
        // Return the stored value to ensure consistency with database operations
        return availableCopies;
    }

    /**
     * Gets the number of copies currently borrowed.
     * @return Borrowed copies count
     */
    public int getBorrowedCopies() {
        return borrowedCopies;
    }

    /**
     * Gets the book description.
     * @return Description string
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the book publication date.
     * @return Publication date
     */
    public Date getPublicationDate() {
        return publicationDate;
    }

    /**
     * Gets the publisher name.
     * @return Publisher string
     */
    public String getPublisher() {
        return publisher;
    }

    /**
     * Gets the book genre/category (alias for getCategory for backward compatibility).
     * @return Genre/category string
     */
    public String getGenre() {
        return category;
    }

    /**
     * Gets the book publication year (extracted from publication date).
     * @return Publication year as integer
     */
    public int getPublicationYear() {
        if (publicationDate != null) {
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(publicationDate);
            return cal.get(java.util.Calendar.YEAR);
        }
        return 0;
    }

    /**
     * Checks if the book is active/available in the system.
     * @return true if book is active, false otherwise
     */
    public boolean isActive() {
        return true; // All books are considered active by default
    }

    /**
     * Sets the book's active status.
     * @param active true if book should be active, false otherwise
     */
    public void setActive(boolean active) {
        // This method is provided for database compatibility
        // Currently all books are always active
    }

    /**
     * Gets the date when the book was added to the system.
     * @return Date added (using publication date as fallback)
     */
    public Date getDateAdded() {
        return publicationDate != null ? publicationDate : new Date();
    }

    // ==================== SETTER METHODS ====================
    
    /**
     * Sets the total number of copies for this book.
     * This method automatically recalculates available copies to maintain consistency.
     * 
     * @param totalCopies New total copies count (must be non-negative)
     */
    public void setTotalCopies(int totalCopies) {
        if (totalCopies < 0) {
            throw new IllegalArgumentException("Total copies cannot be negative");
        }
        
        this.totalCopies = totalCopies;
        
        // Ensure available copies don't exceed total copies
        if (this.availableCopies > totalCopies) {
            this.availableCopies = totalCopies;
        }
        
        // Recalculate available copies based on current borrowed count
        updateAvailableCopies();
    }

    /**
     * Sets the available copies count.
     * Note: This method is primarily used by the system for database operations.
     * For normal operations, use borrowCopy() and returnCopy() methods.
     * 
     * @param availableCopies New available copies count
     */
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    /**
     * Sets the borrowed copies count.
     * Note: This method is primarily used by the system for database operations.
     * For normal operations, use borrowCopy() and returnCopy() methods.
     * 
     * @param borrowedCopies New borrowed copies count
     */
    public void setBorrowedCopies(int borrowedCopies) {
        this.borrowedCopies = borrowedCopies;
    }

    // ==================== BUSINESS LOGIC METHODS ====================
    
    /**
     * Checks if the book is available for borrowing.
     * A book is available if there are available copies greater than 0.
     * 
     * @return true if book can be borrowed, false otherwise
     */
    public boolean isAvailable() {
        return getAvailableCopies() > 0;
    }

    /**
     * Processes a book copy being borrowed.
     * Decrements available copies and increments borrowed copies.
     * This method maintains the relationship: total = available + borrowed
     */
    public void borrowCopy() {
        if (availableCopies > 0) {
            availableCopies--;
            borrowedCopies++;
        }
    }

    /**
     * Processes a book copy being returned.
     * Increments available copies and decrements borrowed copies.
     * This method maintains the relationship: total = available + borrowed
     */
    public void returnCopy() {
        if (borrowedCopies > 0) {
            borrowedCopies--;
            availableCopies++;
        }
    }

    /**
     * Calculates the current available copies based on total and borrowed copies.
     * This method ensures the mathematical relationship: available = total - borrowed
     * 
     * @return Calculated available copies count (never negative)
     */
    public int calculateAvailableCopies() {
        return Math.max(0, totalCopies - borrowedCopies);
    }

    /**
     * Updates the available copies to match the calculated value.
     * This method ensures data consistency between total, available, and borrowed copies.
     */
    public void updateAvailableCopies() {
        this.availableCopies = Math.max(0, totalCopies - borrowedCopies);
    }
    
    /**
     * Synchronizes the book availability after database operations.
     * This method ensures that the stored available copies match the calculated value.
     * Call this method after loading from database or after manual updates.
     */
    public void synchronizeAvailability() {
        updateAvailableCopies();
        System.out.println("Book " + bookId + " availability synchronized:");
        System.out.println("  Total: " + totalCopies + ", Available: " + availableCopies + ", Borrowed: " + borrowedCopies);
    }

    /**
     * Calculates the percentage of copies that are currently available.
     * Useful for reporting and inventory management.
     * 
     * @return Availability percentage (0.0 to 100.0)
     */
    public double getAvailabilityPercentage() {
        if (totalCopies == 0) {
            return 0.0;
        }
        return (getAvailableCopies() * 100.0) / totalCopies;
    }

    // ==================== UTILITY METHODS ====================
    
    /**
     * Returns a string representation of the book.
     * Format: "Title by Author (ID: bookId) - Available: X/Y copies"
     * 
     * @return Formatted string representation
     */
    @Override
    public String toString() {
        return String.format("%s by %s (ID: %s) - Available: %d/%d copies", 
                           title, author, bookId, getAvailableCopies(), totalCopies);
    }
}
