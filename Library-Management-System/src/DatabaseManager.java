// Import Java SQL classes for database operations
import java.sql.*;
// Import Java utility classes for collections and data structures
import java.util.*;
// Import Java time classes for date and time handling
import java.time.LocalDateTime;
// Import Java time formatter for date string formatting
import java.time.format.DateTimeFormatter;

/**
 * DatabaseManager - Handles all database operations for the Library Management System
 * This class provides a complete data access layer for users, books, and transactions.
 * It uses SQLite as the database engine and provides CRUD operations for all entities.
 * 
 * Key Responsibilities:
 * - User management (add, update, delete, retrieve)
 * - Book management (add, update, delete, search)
 * - Transaction management (add, update, retrieve)
 * - Database connection management
 * - Data persistence and retrieval
 * 
 * @author Library Management System
 * @version 2.0
 * @since 1.0
 */
public class DatabaseManager {
    // Reference to database configuration object for connection details
    private DatabaseConfig dbConfig;
    
    /**
     * Constructor - Creates a new DatabaseManager instance
     * Initializes the database configuration for SQLite operations
     */
    public DatabaseManager() {
        // Create a new database configuration object for SQLite
        this.dbConfig = new DatabaseConfig();
    }
    
    /**
     * Getter method to access the database configuration
     * @return DatabaseConfig object containing database settings
     */
    public DatabaseConfig getDbConfig() {
        return dbConfig;
    }
    
    // ==================== USER MANAGEMENT METHODS ====================
    
    /**
     * Adds a new user to the database
     * This method inserts a new user record with all required user information
     * 
     * @param user User object containing all user details to be inserted
     * @return true if user was successfully added, false otherwise
     */
    public boolean addUser(User user) {
        // SQL INSERT statement to add a new user record
        // Uses parameterized query to prevent SQL injection
        String sql = "INSERT INTO " + dbConfig.getTABLE_USERS() + 
                    " (user_id, username, password, full_name, email, role, book_capacity) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        // Use try-with-resources to automatically close database connection and statement
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set all user parameters in the prepared statement
            pstmt.setString(1, user.getUserId());        // Set user ID as first parameter
            pstmt.setString(2, user.getUsername());      // Set username as second parameter
            pstmt.setString(3, user.getPassword());      // Set password as third parameter
            pstmt.setString(4, user.getFullName());      // Set full name as fourth parameter
            pstmt.setString(5, user.getEmail());         // Set email as fifth parameter
            pstmt.setString(6, user.getRole().name());   // Set role as sixth parameter (converted to string)
            pstmt.setInt(7, user.getBookCapacity());     // Set book capacity as seventh parameter
            
            // Execute the INSERT statement and return success status
            // executeUpdate() returns the number of rows affected (should be 1 for successful insert)
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("Error adding user: " + e.getMessage());
            // Return false to indicate failure
            return false;
        }
    }
    
    /**
     * Updates an existing user in the database
     * This method modifies all user fields except the user ID (which is used for identification)
     * 
     * @param user User object containing updated user information
     * @return true if user was successfully updated, false otherwise
     */
    public boolean updateUser(User user) {
        // SQL UPDATE statement to modify existing user record
        // Updates all fields except user_id (which is used in WHERE clause)
        String sql = "UPDATE " + dbConfig.getTABLE_USERS() + 
                    " SET username=?, password=?, full_name=?, email=?, role=?, book_capacity=? WHERE user_id=?";
        
        // Use try-with-resources to automatically close database connection and statement
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set all updated user parameters in the prepared statement
            pstmt.setString(1, user.getUsername());      // Set updated username as first parameter
            pstmt.setString(2, user.getPassword());      // Set updated password as second parameter
            pstmt.setString(3, user.getFullName());      // Set updated full name as third parameter
            pstmt.setString(4, user.getEmail());         // Set updated email as fourth parameter
            pstmt.setString(5, user.getRole().name());   // Set updated role as fifth parameter (converted to string)
            pstmt.setInt(6, user.getBookCapacity());     // Set updated book capacity as sixth parameter
            pstmt.setString(7, user.getUserId());        // Set user ID as seventh parameter (for WHERE clause)
            
            // Execute the UPDATE statement and return success status
            // executeUpdate() returns the number of rows affected (should be 1 for successful update)
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("Error updating user: " + e.getMessage());
            // Return false to indicate failure
            return false;
        }
    }
    
    /**
     * Soft deletes a user by setting their active status to false
     * This method doesn't actually remove the user record from the database
     * Instead, it marks the user as inactive for data integrity purposes
     * 
     * @param userId Unique identifier of the user to be deactivated
     * @return true if user was successfully deactivated, false otherwise
     */
    public boolean deleteUser(String userId) {
        // SQL UPDATE statement to set user's active status to 0 (false)
        // This is a soft delete - the record remains but is marked as inactive
        String sql = "UPDATE " + dbConfig.getTABLE_USERS() + " SET is_active=0 WHERE user_id=?";
        
        // Use try-with-resources to automatically close database connection and statement
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set the user ID parameter in the prepared statement for the WHERE clause
            pstmt.setString(1, userId);
            
            // Execute the UPDATE statement and return success status
            // executeUpdate() returns the number of rows affected (should be 1 for successful deactivation)
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("Error deleting user: " + e.getMessage());
            // Return false to indicate failure
            return false;
        }
    }
    
    /**
     * Retrieves a single user from the database by their unique ID
     * This method fetches all user information and creates a User object
     * 
     * @param userId Unique identifier of the user to retrieve
     * @return User object if found, null if user doesn't exist or error occurs
     */
    public User getUser(String userId) {
        // SQL SELECT statement to retrieve user by ID
        // Uses wildcard (*) to select all columns from the users table
        String sql = "SELECT * FROM " + dbConfig.getTABLE_USERS() + " WHERE user_id=?";
        
        // Use try-with-resources to automatically close database connection and statement
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set the user ID parameter in the prepared statement for the WHERE clause
            pstmt.setString(1, userId);
            
            // Execute the SELECT query and get the result set
            ResultSet rs = pstmt.executeQuery();
            
            // Check if the result set contains any data
            if (rs.next()) {
                // If user found, create and return a User object from the result set
                return createUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("Error getting user: " + e.getMessage());
        }
        
        // Return null if no user found or if an error occurred
        return null;
    }
    
    /**
     * Retrieves a single user from the database by their username
     * This method is commonly used during login to authenticate users
     * 
     * @param username Login username of the user to retrieve
     * @return User object if found, null if user doesn't exist or error occurs
     */
    public User getUserByUsername(String username) {
        // SQL SELECT statement to retrieve user by username
        // Uses wildcard (*) to select all columns from the users table
        String sql = "SELECT * FROM " + dbConfig.getTABLE_USERS() + " WHERE username=?";
        
        // Use try-with-resources to automatically close database connection and statement
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set the username parameter in the prepared statement for the WHERE clause
            pstmt.setString(1, username);
            
            // Execute the SELECT query and get the result set
            ResultSet rs = pstmt.executeQuery();
            
            // Check if the result set contains any data
            if (rs.next()) {
                // If user found, create and return a User object from the result set
                return createUserFromResultSet(rs);
            }
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("Error getting user by username: " + e.getMessage());
        }
        
        // Return null if no user found or if an error occurred
        return null;
    }
    
    /**
     * Retrieves all users from the database
     * This method fetches all user records and creates User objects for each
     * 
     * @return List of all User objects in the database, empty list if no users or error occurs
     */
    public List<User> getAllUsers() {
        // Create an empty list to store all user objects
        List<User> users = new ArrayList<>();
        
        // SQL SELECT statement to retrieve all users
        // Uses wildcard (*) to select all columns from the users table
        String sql = "SELECT * FROM " + dbConfig.getTABLE_USERS();
        
        // Use try-with-resources to automatically close database connection, statement, and result set
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Iterate through all rows in the result set
            while (rs.next()) {
                // For each row, create a User object and add it to the list
                users.add(createUserFromResultSet(rs));
            }
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("Error getting all users: " + e.getMessage());
        }
        
        // Return the list of users (empty if no users found or error occurred)
        return users;
    }
    
    /**
     * Creates a User object from a database result set
     * This helper method extracts all user data from a database row and constructs a User object
     * 
     * @param rs ResultSet containing user data from a database query
     * @return User object populated with data from the result set
     * @throws SQLException if there's an error reading data from the result set
     */
    private User createUserFromResultSet(ResultSet rs) throws SQLException {
        // Extract all user fields from the result set
        String userId = rs.getString("user_id");                    // Get user ID as string
        String username = rs.getString("username");                 // Get username as string
        String password = rs.getString("password");                 // Get password as string
        String fullName = rs.getString("full_name");               // Get full name as string
        String email = rs.getString("email");                       // Get email as string
        User.UserRole role = User.UserRole.valueOf(rs.getString("role")); // Get role and convert to enum
        int bookCapacity = rs.getInt("book_capacity");             // Get book capacity as integer
        boolean isActive = rs.getInt("is_active") == 1;            // Get active status (SQLite uses INTEGER for boolean)
        
        // Create a new User object with all the extracted data
        User user = new User(userId, username, password, fullName, email, role, bookCapacity);
        
        // Set the active status separately since it's not in the constructor
        user.setActive(isActive);
        
        // Return the fully populated User object
        return user;
    }
    
    // ==================== BOOK MANAGEMENT METHODS ====================
    
    /**
     * Adds a new book to the database
     * This method inserts a new book record with all required book information
     * 
     * @param book Book object containing all book details to be inserted
     * @return true if book was successfully added, false otherwise
     */
    public boolean addBook(Book book) {
        // SQL INSERT statement to add a new book record
        // Uses parameterized query to prevent SQL injection
        // Inserts all book fields including inventory management fields
        String sql = "INSERT INTO " + dbConfig.getTABLE_BOOKS() + 
                    " (book_id, title, author, isbn, genre, publication_year, publisher, total_copies, available_copies, borrowed_copies) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Use try-with-resources to automatically close database connection and statement
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set all book parameters in the prepared statement
            pstmt.setString(1, book.getBookId());           // Set book ID as first parameter
            pstmt.setString(2, book.getTitle());            // Set book title as second parameter
            pstmt.setString(3, book.getAuthor());           // Set book author as third parameter
            pstmt.setString(4, book.getIsbn());             // Set ISBN as fourth parameter
            pstmt.setString(5, book.getGenre());            // Set genre as fifth parameter
            pstmt.setInt(6, book.getPublicationYear());     // Set publication year as sixth parameter
            pstmt.setString(7, book.getPublisher());        // Set publisher as seventh parameter
            pstmt.setInt(8, book.getTotalCopies());         // Set total copies as eighth parameter
            pstmt.setInt(9, book.getAvailableCopies());     // Set available copies as ninth parameter
            pstmt.setInt(10, book.getBorrowedCopies());     // Set borrowed copies as tenth parameter
            
            // Execute the INSERT statement and return success status
            // executeUpdate() returns the number of rows affected (should be 1 for successful insert)
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("Error adding book: " + e.getMessage());
            // Return false to indicate failure
            return false;
        }
    }
    
    /**
     * Updates an existing book in the database
     * This method modifies all book fields except the book ID (which is used for identification)
     * 
     * @param book Book object containing updated book information
     * @return true if book was successfully updated, false otherwise
     */
    public boolean updateBook(Book book) {
        System.out.println("=== DATABASE MANAGER: UPDATING BOOK ===");
        System.out.println("Book ID: " + book.getBookId() + ", Title: " + book.getTitle());
        System.out.println("Updating book availability:");
        System.out.println("  Total Copies: " + book.getTotalCopies());
        System.out.println("  Available Copies: " + book.getAvailableCopies());
        System.out.println("  Borrowed Copies: " + book.getBorrowedCopies());
        
        // SQL UPDATE statement to modify existing book record
        // Updates all fields except book_id (which is used in WHERE clause)
        String sql = "UPDATE " + dbConfig.getTABLE_BOOKS() + 
                    " SET title=?, author=?, isbn=?, genre=?, publication_year=?, publisher=?, total_copies=?, available_copies=?, borrowed_copies=? " +
                    "WHERE book_id=?";
        
        // Use try-with-resources to automatically close database connection and statement
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set all updated book parameters in the prepared statement
            pstmt.setString(1, book.getTitle());            // Set updated title as first parameter
            pstmt.setString(2, book.getAuthor());           // Set updated author as second parameter
            pstmt.setString(3, book.getIsbn());             // Set updated ISBN as third parameter
            pstmt.setString(4, book.getGenre());            // Set updated genre as fourth parameter
            pstmt.setInt(5, book.getPublicationYear());     // Set updated publication year as fifth parameter
            pstmt.setString(6, book.getPublisher());        // Set updated publisher as sixth parameter
            pstmt.setInt(7, book.getTotalCopies());         // Set updated total copies as seventh parameter
            pstmt.setInt(8, book.getAvailableCopies());     // Set updated available copies as eighth parameter
            pstmt.setInt(9, book.getBorrowedCopies());      // Set updated borrowed copies as ninth parameter
            pstmt.setString(10, book.getBookId());          // Set book ID as tenth parameter (for WHERE clause)
            
            System.out.println("Executing SQL update...");
            System.out.println("SQL: " + sql);
            
            // Execute the UPDATE statement and return success status
            // executeUpdate() returns the number of rows affected (should be 1 for successful update)
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            
            if (rowsAffected > 0) {
                System.out.println("✓ Book updated successfully in database");
                
                // Verify the update by fetching the book from database
                Book updatedBook = getBook(book.getBookId());
                if (updatedBook != null) {
                    System.out.println("Database verification after update:");
                    System.out.println("  Total Copies: " + updatedBook.getTotalCopies());
                    System.out.println("  Available Copies: " + updatedBook.getAvailableCopies());
                    System.out.println("  Borrowed Copies: " + updatedBook.getBorrowedCopies());
                }
                
                System.out.println("=== END BOOK UPDATE ===");
                return true;
            } else {
                System.err.println("✗ No rows were updated - book may not exist");
                System.out.println("=== END BOOK UPDATE (FAILED) ===");
                return false;
            }
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("✗ Error updating book: " + e.getMessage());
            e.printStackTrace();
            System.out.println("=== END BOOK UPDATE (FAILED) ===");
            // Return false to indicate failure
            return false;
        }
    }
    
    /**
     * Soft deletes a book by setting its active status to false
     * This method doesn't actually remove the book record from the database
     * Instead, it marks the book as inactive for data integrity purposes
     * 
     * @param bookId Unique identifier of the book to be deactivated
     * @return true if book was successfully deactivated, false otherwise
     */
    public boolean deleteBook(String bookId) {
        // SQL UPDATE statement to set book's active status to 0 (false)
        // This is a soft delete - the record remains but is marked as inactive
        String sql = "UPDATE " + dbConfig.getTABLE_BOOKS() + " SET is_active=0 WHERE book_id=?";
        
        // Use try-with-resources to automatically close database connection and statement
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set the book ID parameter in the prepared statement for the WHERE clause
            pstmt.setString(1, bookId);
            
            // Execute the UPDATE statement and return success status
            // executeUpdate() returns the number of rows affected (should be 1 for successful deactivation)
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("Error deleting book: " + e.getMessage());
            // Return false to indicate failure
            return false;
        }
    }
    
    /**
     * Retrieves a single book from the database by its unique ID
     * This method fetches all book information and creates a Book object
     * 
     * @param bookId Unique identifier of the book to retrieve
     * @return Book object if found, null if book doesn't exist or error occurs
     */
    public Book getBook(String bookId) {
        // SQL SELECT statement to retrieve book by ID
        // Uses wildcard (*) to select all columns from the books table
        String sql = "SELECT * FROM " + dbConfig.getTABLE_BOOKS() + " WHERE book_id=?";
        
        // Use try-with-resources to automatically close database connection and statement
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set the book ID parameter in the prepared statement for the WHERE clause
            pstmt.setString(1, bookId);
            
            // Execute the SELECT query and get the result set
            ResultSet rs = pstmt.executeQuery();
            
            // Check if the result set contains any data
            if (rs.next()) {
                // If book found, create and return a Book object from the result set
                return createBookFromResultSet(rs);
            }
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("Error getting book: " + e.getMessage());
        }
        
        // Return null if no book found or if an error occurred
        return null;
    }
    
    /**
     * Retrieves all active books from the database
     * This method fetches all active book records and creates Book objects for each
     * 
     * @return List of all active Book objects in the database, empty list if no books or error occurs
     */
    public List<Book> getAllBooks() {
        System.out.println("=== DATABASE MANAGER: GETTING ALL BOOKS ===");
        
        // Create an empty list to store all book objects
        List<Book> books = new ArrayList<>();
        
        // SQL SELECT statement to retrieve all active books
        // Uses wildcard (*) to select all columns from the books table
        // Only retrieves books where is_active = 1 (active books)
        String sql = "SELECT * FROM " + dbConfig.getTABLE_BOOKS() + " WHERE is_active=1";
        
        // Use try-with-resources to automatically close database connection, statement, and result set
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            // Iterate through all rows in the result set
            while (rs.next()) {
                // For each row, create a Book object and add it to the list
                Book book = createBookFromResultSet(rs);
                books.add(book);
            }
            
            System.out.println("Retrieved " + books.size() + " books from database");
            
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("Error getting all books: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Return the list of books (empty if no books found or error occurred)
        System.out.println("=== END GETTING ALL BOOKS ===");
        return books;
    }
    
    /**
     * Refreshes the availability information for all books in the provided list
     * This method calculates the current available copies based on actual transaction data
     * and updates both the Book objects and the database
     * 
     * @param books List of Book objects whose availability needs to be refreshed
     */
    private void refreshAllBooksAvailability(List<Book> books) {
        // Iterate through each book in the list
        for (Book book : books) {
            // Get the current count of borrowed copies for this book from the database
            int borrowedCopies = getBorrowedCopiesForBook(book.getBookId());
            
            // Calculate available copies by subtracting borrowed copies from total copies
            int availableCopies = book.getTotalCopies() - borrowedCopies;
            
            // Update the book object with the calculated available copies
            book.setAvailableCopies(availableCopies);
            
            // Update the book availability in the database to keep it synchronized
            // This ensures the database reflects the current real-time availability
            updateBookAvailabilityInDatabase(book);
        }
    }
    
    /**
     * Searches for books in the database based on a search term
     * This method performs a case-insensitive search across multiple book fields
     * including title, author, genre, and ISBN
     * 
     * @param searchTerm The text to search for in book fields
     * @return List of Book objects that match the search criteria, empty list if no matches or error occurs
     */
    public List<Book> searchBooks(String searchTerm) {
        System.out.println("=== DATABASE MANAGER: SEARCHING BOOKS ===");
        System.out.println("Search term: '" + searchTerm + "'");
        
        // Create an empty list to store matching book objects
        List<Book> books = new ArrayList<>();
        
        // SQL SELECT statement to search for books
        // Uses wildcard (*) to select all columns from the books table
        // Only searches active books (is_active=1)
        // Searches across multiple fields using LIKE operator with OR conditions
        String sql = "SELECT * FROM " + dbConfig.getTABLE_BOOKS() + 
                    " WHERE is_active=1 AND (title LIKE ? OR author LIKE ? OR genre LIKE ? OR isbn LIKE ?)";
        
        // Use try-with-resources to automatically close database connection and statement
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Create the LIKE pattern by adding wildcards around the search term
            // This allows for partial matches (e.g., "Java" will match "Java Programming")
            String likeTerm = "%" + searchTerm + "%";
            
            // Set all search parameters in the prepared statement
            pstmt.setString(1, likeTerm);  // Search in title field
            pstmt.setString(2, likeTerm);  // Search in author field
            pstmt.setString(3, likeTerm);  // Search in genre field
            pstmt.setString(4, likeTerm);  // Search in ISBN field
            
            // Execute the SELECT query and get the result set
            ResultSet rs = pstmt.executeQuery();
            
            // Iterate through all matching rows in the result set
            while (rs.next()) {
                // For each matching row, create a Book object and add it to the list
                Book book = createBookFromResultSet(rs);
                books.add(book);
            }
            
            System.out.println("Found " + books.size() + " matching books");
            
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("Error searching books: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Return the list of matching books (empty if no matches found or error occurred)
        System.out.println("=== END SEARCHING BOOKS ===");
        return books;
    }
    
    /**
     * Creates a Book object from a database result set
     * This helper method extracts all book data from a database row and constructs a Book object
     * 
     * @param rs ResultSet containing book data from a database query
     * @return Book object populated with data from the result set
     * @throws SQLException if there's an error reading data from the result set
     */
    private Book createBookFromResultSet(ResultSet rs) throws SQLException {
        // Extract all book fields from the result set
        String bookId = rs.getString("book_id");                    // Get book ID as string
        String title = rs.getString("title");                       // Get book title as string
        String author = rs.getString("author");                     // Get book author as string
        String isbn = rs.getString("isbn");                         // Get ISBN as string
        String genre = rs.getString("genre");                       // Get genre as string
        int publicationYear = rs.getInt("publication_year");        // Get publication year as integer
        String publisher = rs.getString("publisher");               // Get publisher as string
        int totalCopies = rs.getInt("total_copies");                // Get total copies as integer
        int availableCopies = rs.getInt("available_copies");        // Get available copies as integer
        int borrowedCopies = rs.getInt("borrowed_copies");          // Get borrowed copies as integer
        boolean isActive = rs.getInt("is_active") == 1;             // Get active status (SQLite uses INTEGER for boolean)
        
        System.out.println("Creating book from database: " + bookId + " - " + title);
        System.out.println("  Total: " + totalCopies + ", Available: " + availableCopies + ", Borrowed: " + borrowedCopies);
        
        // Create a new Book object with all the extracted data
        // Note: Using a default description since it's not stored in the database
        // Converting publication year to Date object using Calendar.Builder
        Book book = new Book(bookId, title, author, isbn, genre, totalCopies, "Description", 
                           new java.util.Calendar.Builder().setDate(publicationYear, 0, 1).build().getTime(), publisher);
        
        // Set additional book properties that are not in the constructor
        book.setActive(isActive);                                   // Set the active status
        book.setAvailableCopies(availableCopies);                   // Set the available copies count
        book.setBorrowedCopies(borrowedCopies);                     // Set the borrowed copies count
        
        // Verify the book availability is consistent
        int calculatedAvailable = totalCopies - borrowedCopies;
        if (availableCopies != calculatedAvailable) {
            System.out.println("⚠️  WARNING: Book " + bookId + " availability mismatch in database!");
            System.out.println("   Database shows: Available=" + availableCopies + ", Total=" + totalCopies + ", Borrowed=" + borrowedCopies);
            System.out.println("   Calculated available should be: " + calculatedAvailable);
            System.out.println("   Synchronizing availability...");
            
            // Synchronize the availability
            book.setAvailableCopies(calculatedAvailable);
            book.setBorrowedCopies(borrowedCopies);
            
            // Update the database to fix the inconsistency
            updateBook(book);
        } else {
            System.out.println("✓ Book " + bookId + " availability is consistent");
        }
        
        // Return the fully populated Book object
        return book;
    }
    
    /**
     * Helper method to count the number of borrowed copies for a specific book
     * This method queries the transactions table to find all active loans for the given book
     * 
     * @param bookId Unique identifier of the book to count borrowed copies for
     * @return Number of currently borrowed copies for the book, 0 if error occurs
     */
    private int getBorrowedCopiesForBook(String bookId) {
        // SQL SELECT COUNT statement to count active transactions for a specific book
        // Only counts transactions with status 'ACTIVE' (currently borrowed books)
        String sql = "SELECT COUNT(*) FROM " + dbConfig.getTABLE_TRANSACTIONS() + 
                    " WHERE book_id=? AND status='ACTIVE'";
        
        // Use try-with-resources to automatically close database connection and statement
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set the book ID parameter in the prepared statement for the WHERE clause
            pstmt.setString(1, bookId);
            
            // Execute the COUNT query and get the result set
            ResultSet rs = pstmt.executeQuery();
            
            // Check if the result set contains any data
            if (rs.next()) {
                // Return the count from the first (and only) column
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("Error counting borrowed copies for book " + bookId + ": " + e.getMessage());
        }
        
        // Return 0 if no borrowed copies found or if an error occurred
        return 0;
    }
    
    // ==================== TRANSACTION MANAGEMENT METHODS ====================
    
    /**
     * Adds a new transaction to the database
     * This method inserts a new transaction record with all required transaction information
     * After successful insertion, it updates the book's availability in the database
     * 
     * @param transaction Transaction object containing all transaction details to be inserted
     * @return true if transaction was successfully added, false otherwise
     */
    public boolean addTransaction(Transaction transaction) {
        // SQL INSERT statement to add a new transaction record
        // Uses parameterized query to prevent SQL injection
        // Inserts all transaction fields including dates, type, status, and fine information
        String sql = "INSERT INTO " + dbConfig.getTABLE_TRANSACTIONS() + 
                    " (transaction_id, book_id, user_id, borrow_date, due_date, transaction_type, status, fine_amount, notes) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        // Use try-with-resources to automatically close database connection and statement
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set all transaction parameters in the prepared statement
            pstmt.setString(1, transaction.getTransactionId());        // Set transaction ID as first parameter
            pstmt.setString(2, transaction.getBook().getBookId());    // Set book ID as second parameter
            pstmt.setString(3, transaction.getUser().getUserId());    // Set user ID as third parameter
            pstmt.setString(4, transaction.getBorrowDate().toString()); // Set borrow date as fourth parameter (SQLite uses TEXT for datetime)
            pstmt.setString(5, transaction.getDueDate().toString());   // Set due date as fifth parameter
            pstmt.setString(6, transaction.getType().name());          // Set transaction type as sixth parameter (converted to string)
            pstmt.setString(7, transaction.getStatus().name());        // Set status as seventh parameter (converted to string)
            pstmt.setDouble(8, transaction.getFineAmount());           // Set fine amount as eighth parameter
            pstmt.setString(9, transaction.getNotes() != null ? transaction.getNotes() : ""); // Set notes as ninth parameter (empty string if null)
            
            // Execute the INSERT statement and check if it was successful
            boolean success = pstmt.executeUpdate() > 0;
            
            // If transaction was successfully added, update the book's availability
            if (success) {
                // Update the book's available copies in the database to reflect the new loan
                // This ensures the inventory count stays accurate
                updateBookAvailabilityInDatabase(transaction.getBook());
            }
            
            // Return the success status
            return success;
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            System.err.println("Error adding transaction: " + e.getMessage());
            // Return false to indicate failure
            return false;
        }
    }
    
    /**
     * Updates the book's available copies count in the database
     * This method synchronizes the book object's availability with the database
     * to ensure consistency between in-memory objects and persistent storage
     * 
     * @param book Book object whose availability needs to be updated in the database
     */
    private void updateBookAvailabilityInDatabase(Book book) {
        // SQL UPDATE statement to modify the available_copies field for a specific book
        // Only updates the available_copies field, leaving other fields unchanged
        String sql = "UPDATE " + dbConfig.getTABLE_BOOKS() + 
                    " SET available_copies=? WHERE book_id=?";
        
        // Use try-with-resources to automatically close database connection and statement
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set the available copies parameter in the prepared statement
            pstmt.setInt(1, book.getAvailableCopies());     // Set available copies as first parameter
            pstmt.setString(2, book.getBookId());            // Set book ID as second parameter (for WHERE clause)
            
            // Execute the UPDATE statement to persist the changes
            // No return value needed as this is a maintenance operation
            pstmt.executeUpdate();
        } catch (SQLException e) {
            // Print error message to console if database operation fails
            // Note: This method doesn't throw exceptions to avoid disrupting the calling method
            System.err.println("Error updating book availability in database: " + e.getMessage());
        }
    }
    
    public boolean updateTransaction(Transaction transaction) {
        System.out.println("Updating transaction in database: " + transaction.getTransactionId());
        System.out.println("Return date: " + transaction.getReturnDate());
        System.out.println("Status: " + transaction.getStatus());
        System.out.println("Fine amount: " + transaction.getFineAmount());
        
        String sql = "UPDATE " + dbConfig.getTABLE_TRANSACTIONS() + 
                    " SET return_date=?, status=?, fine_amount=?, notes=? WHERE transaction_id=?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set return date
            if (transaction.getReturnDate() != null) {
                pstmt.setString(1, transaction.getReturnDate().toString());
                System.out.println("Setting return date: " + transaction.getReturnDate().toString());
            } else {
                pstmt.setNull(1, Types.VARCHAR);
                System.out.println("Setting return date to NULL");
            }
            
            // Set status
            pstmt.setString(2, transaction.getStatus().name());
            System.out.println("Setting status: " + transaction.getStatus().name());
            
            // Set fine amount
            pstmt.setDouble(3, transaction.getFineAmount());
            System.out.println("Setting fine amount: " + transaction.getFineAmount());
            
            // Set notes
            pstmt.setString(4, transaction.getNotes() != null ? transaction.getNotes() : "");
            System.out.println("Setting notes: " + (transaction.getNotes() != null ? transaction.getNotes() : ""));
            
            // Set transaction ID for WHERE clause
            pstmt.setString(5, transaction.getTransactionId());
            System.out.println("Updating transaction ID: " + transaction.getTransactionId());
            
            // Execute the update
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected by update: " + rowsAffected);
            
            boolean success = rowsAffected > 0;
            if (success) {
                System.out.println("Transaction updated successfully in database");
                // Update the book's available copies in the database
                updateBookAvailabilityInDatabase(transaction.getBook());
            } else {
                System.out.println("No rows were updated - transaction may not exist");
            }
            return success;
        } catch (SQLException e) {
            System.err.println("Error updating transaction: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public Transaction getTransaction(String transactionId) {
        String sql = "SELECT t.*, b.*, u.* FROM " + dbConfig.getTABLE_TRANSACTIONS() + " t " +
                    "JOIN " + dbConfig.getTABLE_BOOKS() + " b ON t.book_id = b.book_id " +
                    "JOIN " + dbConfig.getTABLE_USERS() + " u ON t.user_id = u.user_id " +
                    "WHERE t.transaction_id=?";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, transactionId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return createTransactionFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error getting transaction: " + e.getMessage());
        }
        return null;
    }
    
    public List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, b.*, u.* FROM " + dbConfig.getTABLE_TRANSACTIONS() + " t " +
                    "JOIN " + dbConfig.getTABLE_BOOKS() + " b ON t.book_id = b.book_id " +
                    "JOIN " + dbConfig.getTABLE_USERS() + " u ON t.user_id = u.user_id " +
                    "WHERE t.is_active = 1";
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Transaction transaction = createTransactionFromResultSet(rs);
                if (transaction != null) {
                    transactions.add(transaction);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting all transactions: " + e.getMessage());
        }
        return transactions;
    }

    /**
     * Get all transactions for a specific user from the database.
     * This ensures transaction history persists across app sessions.
     * 
     * @param userId The user ID to get transactions for
     * @return List of all transactions for the user
     */
    public List<Transaction> getUserTransactionsFromDatabase(String userId) {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT t.*, b.*, u.* FROM " + dbConfig.getTABLE_TRANSACTIONS() + " t " +
                    "JOIN " + dbConfig.getTABLE_BOOKS() + " b ON t.book_id = b.book_id " +
                    "JOIN " + dbConfig.getTABLE_USERS() + " u ON t.user_id = u.user_id " +
                    "WHERE t.user_id = ? " +
                    "ORDER BY t.borrow_date DESC";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Transaction transaction = createTransactionFromResultSet(rs);
                if (transaction != null) {
                    transactions.add(transaction);
                    System.out.println("Retrieved transaction: " + transaction.getTransactionId() + 
                                     " Status: " + transaction.getStatus() + 
                                     " Return Date: " + (transaction.getReturnDate() != null ? transaction.getReturnDate() : "null"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting user transactions from database: " + e.getMessage());
        }
        return transactions;
    }

    /**
     * Get the count of active (borrowed but not returned) books for a specific user.
     * This is used to enforce borrowing limits based on actual transaction data.
     * 
     * @param userId The user ID to count active loans for
     * @return Number of active loans for the user
     */
    public int getActiveLoansCountFromDatabase(String userId) {
        String sql = "SELECT COUNT(*) FROM " + dbConfig.getTABLE_TRANSACTIONS() + 
                    " WHERE user_id = ? AND status = 'ACTIVE' AND is_active = 1";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting active loans for user " + userId + ": " + e.getMessage());
        }
        return 0;
    }

    /**
     * Get the count of overdue books for a specific user.
     * This helps track overdue status based on actual transaction data.
     * 
     * @param userId The user ID to count overdue books for
     * @return Number of overdue books for the user
     */
    public int getOverdueBooksCountFromDatabase(String userId) {
        String sql = "SELECT COUNT(*) FROM " + dbConfig.getTABLE_TRANSACTIONS() + 
                    " WHERE user_id = ? AND status = 'ACTIVE' AND is_active = 1 " +
                    "AND due_date < datetime('now')";
        
        try (Connection conn = dbConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("Error counting overdue books for user " + userId + ": " + e.getMessage());
        }
        return 0;
    }
    
    private Transaction createTransactionFromResultSet(ResultSet rs) throws SQLException {
        try {
            // Create Book object
            String bookId = rs.getString("book_id");
            String title = rs.getString("title");
            String author = rs.getString("author");
            String isbn = rs.getString("isbn");
            String genre = rs.getString("genre");
            int publicationYear = rs.getInt("publication_year");
            String publisher = rs.getString("publisher");
            int totalCopies = rs.getInt("total_copies");
            int availableCopies = rs.getInt("available_copies");
            boolean isActive = rs.getInt("is_active") == 1;
            
            // Fix Book constructor call to match the correct signature
            Book book = new Book(bookId, title, author, isbn, genre, totalCopies, "Description", new java.util.Calendar.Builder().setDate(publicationYear, 0, 1).build().getTime(), publisher);
            // Set book properties
            book.setAvailableCopies(availableCopies);
            book.setActive(isActive);
            
            // Create User object
            String userId = rs.getString("user_id");
            String username = rs.getString("username");
            String password = rs.getString("password");
            String fullName = rs.getString("full_name");
            String email = rs.getString("email");
            User.UserRole role = User.UserRole.valueOf(rs.getString("role"));
            boolean userActive = rs.getInt("is_active") == 1;
            
            User user = new User(userId, username, password, fullName, email, role);
            user.setActive(userActive);
            
            // Create Transaction object
            String transactionId = rs.getString("transaction_id");
            String borrowDateStr = rs.getString("borrow_date");
            String dueDateStr = rs.getString("due_date");
            String returnDateStr = rs.getString("return_date");
            String transactionType = rs.getString("transaction_type");
            String status = rs.getString("status");
            double fineAmount = rs.getDouble("fine_amount");
            String notes = rs.getString("notes");
            
            // Parse dates (simplified - you might want to use a proper date parser)
            LocalDateTime borrowDate = LocalDateTime.parse(borrowDateStr.replace(" ", "T"));
            LocalDateTime dueDate = LocalDateTime.parse(dueDateStr.replace(" ", "T"));
            LocalDateTime returnDate = null;
            if (returnDateStr != null && !returnDateStr.trim().isEmpty()) {
                returnDate = LocalDateTime.parse(returnDateStr.replace(" ", "T"));
            }
            
            // Create transaction based on type
            Transaction transaction;
            if ("BORROW".equals(transactionType)) {
                transaction = new Transaction(transactionId, book, user);
            } else {
                transaction = new Transaction(transactionId, book, user, returnDate);
            }
            
            // Set additional properties from database
            transaction.setFineAmount(fineAmount);
            if (notes != null && !notes.trim().isEmpty()) {
                transaction.setNotes(notes);
            }
            
            // IMPORTANT: Set the return date from database regardless of transaction type
            // This ensures return dates are properly displayed in the GUI
            if (returnDate != null) {
                transaction.setReturnDate(returnDate);
                System.out.println("Set return date for transaction " + transactionId + ": " + returnDate);
            }
            
            // Set the status from database to ensure it's correct
            if ("ACTIVE".equals(status)) {
                transaction.setStatus(Transaction.TransactionStatus.ACTIVE);
            } else if ("RETURNED".equals(status)) {
                transaction.setStatus(Transaction.TransactionStatus.RETURNED);
                // Ensure return date is set for returned transactions
                if (returnDate != null) {
                    System.out.println("Setting return date for transaction " + transactionId + ": " + returnDate);
                }
            } else if ("OVERDUE".equals(status)) {
                transaction.setStatus(Transaction.TransactionStatus.ACTIVE); // Overdue is still active
            }
            
            System.out.println("Created transaction: " + transactionId + " with status: " + status + 
                             " and return date: " + (returnDate != null ? returnDate : "null"));
            
            return transaction;
        } catch (Exception e) {
            System.err.println("Error creating transaction from result set: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Initializes the database with sample data for testing purposes
     * This method creates sample users and books to demonstrate the system functionality
     * Note: This method should only be called during initial setup or testing
     */
    public void initializeSampleData() {
        System.out.println("Initializing sample data...");
        
        // Add sample users
        User admin = new User("U001", "admin", "admin123", "System Administrator", "admin@library.com", User.UserRole.ADMIN, 0);
        User librarian = new User("U002", "librarian", "lib123", "John Librarian", "librarian@library.com", User.UserRole.LIBRARIAN, 0);
        User member1 = new User("U003", "member1", "mem123", "Alice Member", "alice@email.com", User.UserRole.MEMBER, 5);
        User member2 = new User("U004", "member2", "mem123", "Bob Member", "bob@email.com", User.UserRole.MEMBER, 5);
        
        addUser(admin);
        addUser(librarian);
        addUser(member1);
        addUser(member2);
        
        // Add sample books with correct constructor signature
        // Parameters: bookId, title, author, isbn, category, totalCopies, description, publicationDate, publisher
        Book book1 = new Book("B001", "Java Programming", "John Smith", "978-0-123456-78-9", "Programming", 3, "Comprehensive guide to Java programming", new java.util.Calendar.Builder().setDate(2023, 0, 1).build().getTime(), "Tech Books");
        Book book2 = new Book("B002", "Data Structures", "Jane Doe", "978-0-987654-32-1", "Computer Science", 2, "Fundamental data structures and algorithms", new java.util.Calendar.Builder().setDate(2022, 0, 1).build().getTime(), "Academic Press");
        Book book3 = new Book("B003", "Web Development", "Mike Johnson", "978-0-555555-55-5", "Programming", 4, "Modern web development techniques", new java.util.Calendar.Builder().setDate(2024, 0, 1).build().getTime(), "Web Publishers");
        Book book4 = new Book("B004", "Database Design", "Sarah Wilson", "978-0-111111-11-1", "Computer Science", 2, "Database design principles and practices", new java.util.Calendar.Builder().setDate(2021, 0, 1).build().getTime(), "Data Books");
        
        addBook(book1);
        addBook(book2);
        addBook(book3);
        addBook(book4);
        
        System.out.println("Sample data initialized successfully!");
        System.out.println("Note: No initial transactions created - database starts clean");
    }

    /**
     * Check the database schema to verify table structure
     */
    public void checkDatabaseSchema() {
        System.out.println("=== DATABASE SCHEMA CHECK ===");
        
        try (Connection conn = dbConfig.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Check transactions table structure
            String sql = "PRAGMA table_info(" + dbConfig.getTABLE_TRANSACTIONS() + ")";
            ResultSet rs = stmt.executeQuery(sql);
            
            System.out.println("Transactions table columns:");
            while (rs.next()) {
                String columnName = rs.getString("name");
                String columnType = rs.getString("type");
                boolean notNull = rs.getBoolean("notnull");
                String defaultValue = rs.getString("dflt_value");
                
                System.out.println("  - " + columnName + " (" + columnType + ") " + 
                                 (notNull ? "NOT NULL" : "NULL") + 
                                 (defaultValue != null ? " DEFAULT " + defaultValue : ""));
            }
            
            // Check if return_date column exists
            boolean hasReturnDate = false;
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                if ("return_date".equals(rs.getString("name"))) {
                    hasReturnDate = true;
                    break;
                }
            }
            
            if (hasReturnDate) {
                System.out.println("✓ return_date column exists in transactions table");
            } else {
                System.out.println("✗ return_date column MISSING from transactions table!");
            }
            
            // Check sample transaction data
            System.out.println("\nSample transaction data:");
            String sampleSql = "SELECT transaction_id, status, return_date, fine_amount FROM " + 
                             dbConfig.getTABLE_TRANSACTIONS() + " LIMIT 3";
            rs = stmt.executeQuery(sampleSql);
            
            while (rs.next()) {
                String id = rs.getString("transaction_id");
                String status = rs.getString("status");
                String returnDate = rs.getString("return_date");
                double fine = rs.getDouble("fine_amount");
                
                System.out.println("  - ID: " + id + ", Status: " + status + 
                                 ", Return Date: " + (returnDate != null ? returnDate : "NULL") + 
                                 ", Fine: " + fine);
            }
            
        } catch (SQLException e) {
            System.err.println("Error checking database schema: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("=== END SCHEMA CHECK ===");
    }
}
