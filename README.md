# ☕ Java OOP Projects Collection

A professional collection of Java-based Object-Oriented Programming systems demonstrating real-world software design, modular architecture, and database-driven desktop applications.

This repository reflects practical implementation of software engineering principles through two complete management systems.

---

## 🎯 Purpose

This project was built to strengthen core computer science skills including:

- Object-Oriented Programming (OOP) design
- Modular software architecture
- GUI development using Java Swing
- Database integration and data persistence
- Real-world system simulation

---

## 🧩 Included Systems

## ✈️ 1. Airplane Management System

A desktop-based airline management platform that simulates flight operations and passenger workflows.

### 🔹 Key Features:
- Passenger registration and profile management
- Flight scheduling and trip selection
- Booking and reservation system
- Manager control dashboard
- Refund request handling
- Role-based system (Passenger / Manager)

### 🏗 Architecture:
- Java Swing for GUI
- Modular package structure:
  - `passengers`
  - `manager`
  - `Database`
- Event-driven UI design (.form integration)
- Separation of UI and business logic

---

## 📚 2. Library Management System

A fully functional library automation system built with Java and SQLite database integration.

### 🔹 Key Features:
- User authentication system
- Role-based access control:
  - Admin
  - Librarian
  - Member
- Book management (CRUD operations)
- Borrowing & return transactions
- Database-backed persistence (SQLite)

### 🏗 Architecture:
- MVC-inspired structure
- JDBC-based database layer
- Clean separation between:
  - UI layer
  - Business logic
  - Data access layer

---

## 🛠 Tech Stack

- Java SE
- Java Swing (GUI Development)
- JDBC (Database Connectivity)
- SQLite Database
- NetBeans IDE structure

---

## 📁 Project Structure


Java-OOP-Projects/
│
├── Airplane-Management-System/
│ ├── src/
│ ├── build/
│ ├── nbproject/
│ └── Database modules
│
├── Library-Management-System/
│ ├── src/
│ ├── classes/
│ ├── lib/
│ └── database file (.db)


---

## 📊 Skills Demonstrated

This repository demonstrates:

- Strong understanding of OOP principles
- System design and modular architecture
- Desktop application development
- Database integration using JDBC
- Real-world business logic simulation
- UI event-driven programming

---

## 🚀 How to Run

### ▶ Airplane System
- Open project in NetBeans / IntelliJ IDEA
- Run main login or dashboard class

### ▶ Library System
```bash
javac -cp lib/sqlite-jdbc.jar src/*.java
java LibraryManagementSystem

Or run directly via IDE.

👨‍💻 Author

Mahmoud Bahnsey
Cybersecurity & Networks Student