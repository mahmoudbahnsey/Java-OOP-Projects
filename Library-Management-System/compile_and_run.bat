@echo off
title Library Management System - Compile and Run
echo.
echo ========================================
echo    Library Management System
echo ========================================
echo.
echo Step 1: Compiling Java files...
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo.
    echo Please install Java 8 or higher from:
    echo https://java.com or https://adoptium.net
    echo.
    pause
    exit /b 1
)

REM Check if SQLite JDBC driver exists
if not exist "lib\sqlite-jdbc.jar" (
    echo ERROR: SQLite JDBC driver not found
    echo.
    echo Please ensure sqlite-jdbc.jar is in the lib folder
    echo.
    pause
    exit /b 1
)

REM Create classes directory if it doesn't exist
if not exist "classes" mkdir classes

REM Compile all Java files from src folder to classes folder
echo Compiling Java files from src/ to classes/...
javac -cp "lib/sqlite-jdbc.jar" -d classes src\*.java
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed!
    echo.
    echo Please check that all Java files are present in the src folder
    echo.
    pause
    exit /b 1
)

echo.
echo ========================================
echo Compilation successful!
echo ========================================
echo.
echo Step 2: Running the application...
echo.

REM Run the application from classes folder
java -cp "classes;lib/sqlite-jdbc.jar" LibraryManagementSystem

REM If we get here, the application has closed
echo.
echo Application closed.
pause
