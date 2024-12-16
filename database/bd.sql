-- Create Database
CREATE DATABASE LibraryManager;
USE LibraryManager;

-- 1. Create BOOKS Table
CREATE TABLE Books (
    Book_ID INT PRIMARY KEY AUTO_INCREMENT,
    Title VARCHAR(255) NOT NULL,
    Author VARCHAR(255) NOT NULL,
    Genre VARCHAR(50),
    Quantity_Available INT NOT NULL CHECK (Quantity_Available >= 0)
);

-- 2. Create SUBSCRIBERS Table
CREATE TABLE Subscribers (
    Subscriber_ID INT PRIMARY KEY AUTO_INCREMENT,
    Last_Name VARCHAR(255) NOT NULL,
    First_Name VARCHAR(255) NOT NULL,
    Address VARCHAR(255) NOT NULL,
    Telephone VARCHAR(15) UNIQUE NOT NULL
);
-- 3. Create BORROWING Table
CREATE TABLE Borrowing (
    Borrowing_ID INT PRIMARY KEY AUTO_INCREMENT,
    Book_ID INT NOT NULL,
    Subscriber_ID INT NOT NULL,
    Borrowing_Date DATE NOT NULL,
    Return_Date DATE,
    FOREIGN KEY (Book_ID) REFERENCES Books(Book_ID),
    FOREIGN KEY (Subscriber_ID) REFERENCES Subscribers(Subscriber_ID),
    CHECK (Return_Date > Borrowing_Date) -- Table-level CHECK constraint
);
DELIMITER //
-- 4. Create Trigger
CREATE TRIGGER CheckBorrowingDates BEFORE INSERT ON Borrowing
FOR EACH ROW
BEGIN
    IF NEW.Return_Date <= NEW.Borrowing_Date THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Return_Date must be after Borrowing_Date';
    END IF;
END; //

DELIMITER ;
-- 5. Create USERS Table
CREATE TABLE USERS (
    Username VARCHAR(50) PRIMARY KEY,
    Password VARCHAR(50) NOT NULL,
    Role ENUM('Admin', 'User') NOT NULL
);
-- Insert Example Data for Testing

-- Insert Books
INSERT INTO Books (Title, Author, Genre, Quantity_Available)
VALUES 
('To Kill a Mockingbird', 'Harper Lee', 'Fiction', 5),
('1984', 'George Orwell', 'Dystopian', 4),
('The Great Gatsby', 'F. Scott Fitzgerald', 'Classic', 3);

-- Insert Subscribers
INSERT INTO Subscribers (Last_Name, First_Name, Address, Telephone)
VALUES 
('Doe', 'John', '123 Elm Street', '1234567890'),
('Smith', 'Jane', '456 Oak Avenue', '0987654321');


-- Insert admin and user credentials
INSERT INTO Users (Username, Password, Role)
VALUES 
('admin', 'admin123', 'Admin'),
('user1', 'password1', 'User');

-- Insert Borrowing Records
INSERT INTO Borrowing (Book_ID, Subscriber_ID, Borrowing_Date, Return_Date)
VALUES 
(1, 1, '2024-12-01', '2024-12-15'),
(2, 2, '2024-12-05', '2024-12-20');

-- Example Queries for Testing

-- 1. Get All Books
SELECT * FROM Books;

-- 2. Get All Subscribers
SELECT * FROM Subscribers;

-- 3. Get Borrowing Records
SELECT Borrowing_ID, Title, CONCAT(First_Name, ' ', Last_Name) AS Subscriber, Borrowing_Date, Return_Date
FROM Borrowing
JOIN Books ON Borrowing.Book_ID = Books.Book_ID
JOIN Subscribers ON Borrowing.Subscriber_ID = Subscribers.Subscriber_ID;

-- 4. Find Most Borrowed Books
SELECT Books.Title, COUNT(Borrowing.Book_ID) AS BorrowCount
FROM Borrowing
JOIN Books ON Borrowing.Book_ID = Books.Book_ID
GROUP BY Books.Title
ORDER BY BorrowCount DESC;

-- 5. Find Most Active Subscribers
SELECT CONCAT(First_Name, ' ', Last_Name) AS Subscriber, COUNT(Borrowing.Subscriber_ID) AS LoanCount
FROM Borrowing
JOIN Subscribers ON Borrowing.Subscriber_ID = Subscribers.Subscriber_ID
GROUP BY Subscriber
ORDER BY LoanCount DESC;



