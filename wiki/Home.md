# Bus Management System Wiki

Welcome to the Bus Management System documentation. This wiki provides detailed information about the system's architecture, features, and implementation.

## Overview

The Bus Management System is a Java-based application designed to streamline bus booking operations. It provides an intuitive interface for managing bus routes, bookings, and passenger information.

## System Architecture

### Components
- GUI Layer (Java Swing)
- Business Logic Layer
- Data Access Layer (JDBC)
- MySQL Database

### Key Classes
- `ViewBusesWindow`: Main interface for viewing and booking buses
- `BusManagementSystem`: Core system functionality
- Database Tables: Buses, Routes, Bookings

## Features

### 1. Bus Management
- View all available buses
- Search functionality
- Real-time updates
- Export data to CSV

### 2. Booking System
- Quick ticket booking
- Instant confirmation
- Digital receipt generation
- Booking history

### 3. Route Management
- View bus routes
- Source and destination tracking
- Route capacity management

## Database Schema

### Buses Table
```sql
CREATE TABLE Buses (
    bus_id INT PRIMARY KEY,
    bus_number VARCHAR(20),
    bus_type VARCHAR(50),
    capacity INT,
    route_id INT
);