-- Create database if not exists (Docker will create from env, but this is for safety)
CREATE DATABASE IF NOT EXISTS staysearch_db;

-- Connect to database
\c staysearch_db;

-- Create extensions
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- Create tables (Hibernate will create, but this is for manual backup)
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bookings (
    id BIGSERIAL PRIMARY KEY,
    booking_code VARCHAR(255) UNIQUE,
    property_type VARCHAR(100),
    room_type VARCHAR(100),
    branch VARCHAR(255),
    check_in DATE,
    duration VARCHAR(50),
    amount DECIMAL(10,2),
    status VARCHAR(50),
    payment_method VARCHAR(100),
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS payments (
    id BIGSERIAL PRIMARY KEY,
    transaction_id VARCHAR(255) UNIQUE,
    payment_status VARCHAR(50),
    booking_id BIGINT UNIQUE REFERENCES bookings(id) ON DELETE CASCADE
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_bookings_user_id ON bookings(user_id);
CREATE INDEX IF NOT EXISTS idx_bookings_status ON bookings(status);
CREATE INDEX IF NOT EXISTS idx_payments_booking_id ON payments(booking_id);

-- Insert default admin user (password: admin123 - will be encoded by app)
INSERT INTO users (name, email, phone, address, password, role) 
VALUES ('Admin User', 'admin@staysearch.com', '9999999999', 'Admin Address', 'admin123', 'ADMIN')
ON CONFLICT (email) DO NOTHING;

-- Insert sample data
INSERT INTO users (name, email, phone, address, password, role) 
VALUES ('John Doe', 'john@example.com', '9876543210', '123 Main St', 'password123', 'USER')
ON CONFLICT (email) DO NOTHING;

-- Grant permissions
GRANT ALL PRIVILEGES ON DATABASE staysearch_db TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;