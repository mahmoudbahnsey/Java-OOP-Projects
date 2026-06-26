@echo off
title Library Management System
echo.
echo ========================================
echo    Library Management System
echo ========================================
echo.
echo Starting application...
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

REM Check if compiled classes exist, if not compile them
if not exist "classes\LibraryManagementSystem.class" (
    echo Compiling Java files...
    javac -cp "lib/sqlite-jdbc.jar" -d classes src\*.java
    if %errorlevel% neq 0 (
        echo ERROR: Compilation failed!
        echo.
        echo Please check that all Java files are present in the src folder
        echo.
        pause
        exit /b 1
    )
    echo Compilation successful!
    echo.
)

REM Run the application
echo Running Library Management System...
echo.
java -cp "classes;lib/sqlite-jdbc.jar" LibraryManagementSystem

REM If we get here, the application has closed
echo.
echo Application closed.
pause
