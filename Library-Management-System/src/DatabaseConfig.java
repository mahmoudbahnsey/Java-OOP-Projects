import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConfig class manages the database connection and configuration for the Library Management System.
 * 
 * This class provides centralized database configuration including connection parameters, table creation
 * SQL statements, and connection management utilities. It uses SQLite as the database engine for
 * simplicity and portability.
 * 
 * Key Features:
 * - SQLite database connection management
 * - Automatic table creation with proper schema
 * - Centralized database configuration constants
 * - Connection cleanup utilities
 * 
 * Database Schema:
 * - Books table: Stores book information and inventory
 * - Users table: Stores user accounts and borrowing capacity
 * - Transactions table: Stores borrowing/returning history
 * 
 * @author Library Management System
 * @version 2.0
 * @since 1.0
 */
public class DatabaseConfig {
    // Database connection constants
    private static final String DB_URL = "jdbc:sqlite:library_management.db";  // SQLite database file path
    private static final String DB_NAME = "library_management";                 // Database name
    
    // Table names for easy reference and maintenance
    private static final String TABLE_BOOKS = "books";           // Books inventory table
    private static final String TABLE_USERS = "users";           // User accounts table
    private static final String TABLE_TRANSACTIONS = "transactions"; // Transaction history table

    /**
     * Default constructor for DatabaseConfig.
     * This class is designed to be instantiated to access its configuration methods.
     */
    public DatabaseConfig() {
        // No initialization required for configuration class
    }

    /**
     * Gets the books table name.
     * @return Books table name string
     */
    public String getTABLE_BOOKS() {
        return TABLE_BOOKS;
    }

    /**
     * Gets the users table name.
     * @return Users table name string
     */
    public String getTABLE_USERS() {
        return TABLE_USERS;
    }

    /**
     * Gets the transactions table name.
     * @return Transactions table name string
     */
    public String getTABLE_TRANSACTIONS() {
        return TABLE_TRANSACTIONS;
    }

    /**
     * Establishes a connection to the SQLite database.
     * This method creates a new database file if it doesn't exist, or connects to an existing one.
     * 
     * @return Database connection object
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        try {
            // Load SQLite JDBC driver
            Class.forName("org.sqlite.JDBC");
            
            // Create connection to SQLite database (creates file if it doesn't exist)
            Connection connection = DriverManager.getConnection(DB_URL);
            
            // Enable foreign key constraints for data integrity
                connection.createStatement().execute("PRAGMA foreign_keys = ON");
            
            return connection;
            } catch (ClassNotFoundException e) {
                throw new SQLException("SQLite JDBC Driver not found: " + e.getMessage());
            }
        }

    /**
     * Returns the SQL statement to create the books table.
     * This table stores all book information including inventory management.
     * 
     * @return SQL CREATE TABLE statement for books
     */
    public String getCreateBooksTableSQL() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKS + " (" +
               "book_id TEXT PRIMARY KEY," +                    // Unique book identifier
               "title TEXT NOT NULL," +                         // Book title
               "author TEXT NOT NULL," +                        // Book author
               "isbn TEXT," +                                   // International Standard Book Number
               "genre TEXT," +                               // Book category/genre
               "total_copies INTEGER NOT NULL," +               // Total number of copies owned
               "available_copies INTEGER NOT NULL DEFAULT 0," +           // Number of copies available for borrowing
               "borrowed_copies INTEGER NOT NULL DEFAULT 0," +            // Number of copies currently borrowed
               "description TEXT," +                            // Book description
               "publication_year INTEGER," +                       // Publication year
               "publisher TEXT," +                              // Publishing company
               "is_active INTEGER DEFAULT 1," +                    // Whether the book is active
               "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + // When the record was created
               ")";
    }

    /**
     * Returns the SQL statement to create the users table.
     * This table stores user account information and borrowing capacity.
     * 
     * @return SQL CREATE TABLE statement for users
     */
    public String getCreateUsersTableSQL() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + " (" +
               "user_id TEXT PRIMARY KEY," +                    // Unique user identifier
               "username TEXT UNIQUE NOT NULL," +               // Login username (must be unique)
               "password TEXT NOT NULL," +                      // Login password
               "full_name TEXT NOT NULL," +                     // User's full name
               "email TEXT," +                                  // User's email address
               "role TEXT NOT NULL," +                          // User role (ADMIN, LIBRARIAN, MEMBER)
               "book_capacity INTEGER DEFAULT 5," +             // Maximum books user can borrow (default 5 for members)
               "is_active INTEGER DEFAULT 1," +                    // Whether account is active
               "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" + // When the account was created
               ")";
    }

    /**
     * Returns the SQL statement to create the transactions table.
     * This table stores the complete history of book borrowing and returning.
     * 
     * @return SQL CREATE TABLE statement for transactions
     */
    public String getCreateTransactionsTableSQL() {
        return "CREATE TABLE IF NOT EXISTS " + TABLE_TRANSACTIONS + " (" +
               "transaction_id TEXT PRIMARY KEY," +              // Unique transaction identifier
               "book_id TEXT NOT NULL," +                       // Reference to the book
               "user_id TEXT NOT NULL," +                       // Reference to the user
               "borrow_date TEXT NOT NULL," +                   // When the book was borrowed
               "due_date TEXT NOT NULL," +                      // When the book is due
               "return_date TEXT," +                            // When the book was returned (null if not returned)
               "transaction_type TEXT NOT NULL," +              // Transaction type (BORROW, RETURN)
               "status TEXT NOT NULL," +                        // Transaction status (ACTIVE, RETURNED, OVERDUE, CANCELLED)
               "fine_amount REAL DEFAULT 0.0," +                // Fine amount if overdue
               "notes TEXT," +                                  // Additional transaction notes
               "is_active INTEGER DEFAULT 1," +                 // Whether transaction record is active
               "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," + // When the transaction was created
               "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," + // When the transaction was last updated
               "FOREIGN KEY (book_id) REFERENCES " + TABLE_BOOKS + "(book_id)," + // Referential integrity
               "FOREIGN KEY (user_id) REFERENCES " + TABLE_USERS + "(user_id)" +   // Referential integrity
               ")";
    }

    /**
     * Creates all database tables if they don't exist.
     * This method ensures the database schema is properly initialized.
     * 
     * @throws SQLException if table creation fails
     */
    public void createTables() throws SQLException {
        try (Connection connection = getConnection()) {
            // Create books table
            connection.createStatement().execute(getCreateBooksTableSQL());
            
            // Create users table
            connection.createStatement().execute(getCreateUsersTableSQL());
            
            // Create transactions table
            connection.createStatement().execute(getCreateTransactionsTableSQL());
            
            System.out.println("Database tables created successfully.");
        }
    }

    /**
     * Safely closes a database connection.
     * This method handles null connections gracefully and logs any errors.
     * 
     * @param connection Database connection to close (can be null)
     */
    public void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Tests the database connection to ensure it's working properly.
     * This method is used during system initialization to verify database connectivity.
     * 
     * @return true if connection is successful, false otherwise
     */
    public boolean testConnection() {
        try (Connection connection = getConnection()) {
            // Try to execute a simple query to test the connection
            connection.createStatement().execute("SELECT 1");
            System.out.println("Database connection test successful!");
            return true;
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Initializes the database by creating all necessary tables.
     * This method ensures the database schema is properly set up before the application starts.
     * 
     * @throws SQLException if table creation fails
     */
    public void initializeDatabase() throws SQLException {
        try (Connection connection = getConnection()) {
            // Create all tables in the correct order (respecting foreign key dependencies)
            System.out.println("Creating database tables...");
            
            // Create users table first (no dependencies)
            connection.createStatement().execute(getCreateUsersTableSQL());
            System.out.println("Users table created/verified.");
            
            // Create books table (no dependencies)
            connection.createStatement().execute(getCreateBooksTableSQL());
            System.out.println("Books table created/verified.");
            
            // Create transactions table last (depends on users and books)
            connection.createStatement().execute(getCreateTransactionsTableSQL());
            System.out.println("Database initialization completed successfully!");
        }
    }
}
