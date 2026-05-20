-- Create database and users table for User Management System

CREATE DATABASE IF NOT EXISTS user_management_db;
USE user_management_db;

CREATE TABLE IF NOT EXISTS users (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Sample admin user (password: Admin@123) - for testing only
-- INSERT INTO users (name, email, password) VALUES ('Admin', 'admin@test.com', 'Admin@123');
