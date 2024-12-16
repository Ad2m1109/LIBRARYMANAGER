# LIBRARYMANAGER

## Project Overview
LIBRARYMANAGER is a comprehensive library management system designed to facilitate the management of books, loans, subscribers, and statistics. It provides a user-friendly interface for library staff to manage library operations efficiently.
## Technology Stack
- **Programming Language**: Java
- **Java Version**: 11
- **Build Tool**: Apache Ant
- **Database**: MySQL
## Features
- **User Authentication**: Secure login and management of user sessions.
- **Book Management**: Add, update, and delete books in the library catalog.
- **Loan Management**: Manage the lending process of books to subscribers.
- **Subscriber Management**: Add and manage library subscribers.
- **Statistics Generation**: Generate reports and statistics on library usage.

## Installation Instructions
1. Clone the repository:
   ```bash
   git clone <repository-url>
   ```
2. Navigate to the project directory:
   ```bash
   cd LIBRARYMANAGER
   ```
3. Build the project using Ant:
   ```bash
   ant build
   ```

## Usage
To run the application, ensure the database is set up using the provided SQL script. Launch the application from your IDE or command line.

## File Structure
- **build/**: Build artifacts and output.
- **build.xml**: Ant build file for the project.
- **database/**: 
  - [bd.sql](database/bd.sql): SQL script for initializing the database.
- **src/librarymanager/**: Contains the main Java interfaces:
  - [AuthenticationInterface.java](src/librarymanager/AuthenticationInterface.java): Manages user authentication.
  - [BookManagementInterface.java](src/librarymanager/BookManagementInterface.java): Handles book-related operations.
  - [DashboardInterface.java](src/librarymanager/DashboardInterface.java): Provides a dashboard for library statistics.
  - [LoanManagementInterface.java](src/librarymanager/LoanManagementInterface.java): Manages book loans.
  - [StatisticsInterface.java](src/librarymanager/StatisticsInterface.java): Generates library usage statistics.
  - [SubscriberManagementInterface.java](src/librarymanager/SubscriberManagementInterface.java): Manages library subscribers.
- **test/**: Intended for test files (currently empty).

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request. Make sure to follow the project's coding standards and guidelines.

## License
This project is licensed under the MIT License.