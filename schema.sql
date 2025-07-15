CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    student_number VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    surname VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login TIMESTAMP
);

-- Add comments to explain the table structure
COMMENT ON TABLE users IS 'Stores user account information for the BC Student Wellness system';
COMMENT ON COLUMN users.user_id IS 'Unique identifier for each user';
COMMENT ON COLUMN users.student_number IS 'Student number, must be unique';
COMMENT ON COLUMN users.name IS 'First name of the student';
COMMENT ON COLUMN users.surname IS 'Last name of the student';
COMMENT ON COLUMN users.email IS 'Email address, must be unique';
COMMENT ON COLUMN users.phone IS 'Phone number';
COMMENT ON COLUMN users.password IS 'Hashed password for security';
COMMENT ON COLUMN users.created_at IS 'Timestamp when the account was created';
COMMENT ON COLUMN users.last_login IS 'Timestamp of the last successful login';

-- Create an index for faster lookups
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_student_number ON users(student_number);