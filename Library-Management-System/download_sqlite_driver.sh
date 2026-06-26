#!/bin/bash

echo "Downloading SQLite JDBC Driver..."
echo

# Download SQLite JDBC Driver
curl -L -o sqlite-jdbc.jar "https://github.com/xerial/sqlite-jdbc/releases/download/3.44.1.0/sqlite-jdbc-3.44.1.0.jar"

if [ -f "sqlite-jdbc.jar" ]; then
    echo
    echo "SQLite JDBC Driver downloaded successfully!"
    echo "File: sqlite-jdbc.jar"
    echo
    echo "You can now compile and run your application:"
    echo "javac -cp \".:sqlite-jdbc.jar\" *.java"
    echo "java -cp \".:sqlite-jdbc.jar\" LibraryManagementSystem"
else
    echo
    echo "Failed to download SQLite JDBC Driver."
    echo "Please download manually from:"
    echo "https://github.com/xerial/sqlite-jdbc/releases"
    echo
    echo "Place the JAR file in this directory and rename it to sqlite-jdbc.jar"
fi

echo
read -p "Press Enter to continue..."
