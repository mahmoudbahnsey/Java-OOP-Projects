@echo off
title Library Management System - Clean
echo.
echo ========================================
echo    Library Management System
echo ========================================
echo.
echo Cleaning compiled class files...
echo.

REM Remove classes directory and all its contents
if exist "classes" (
    echo Removing classes folder...
    rmdir /s /q classes
    echo Classes folder removed successfully!
) else (
    echo Classes folder not found. Nothing to clean.
)

echo.
echo Cleanup complete!
pause
