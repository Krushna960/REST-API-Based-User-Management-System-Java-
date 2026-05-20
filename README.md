# User Account Management System

A menu-driven Java backend application for user registration, login, profile management, and admin operations. Built with **Core Java**, **JDBC**, **MySQL**, and **OOP** principles.

## Features

| # | Feature | Description |
|---|---------|-------------|
| 1 | User Registration | Signup with validation |
| 2 | User Login | Email/password authentication |
| 3 | Update Profile | Change name, email, password |
| 4 | Delete User | Remove user by ID |
| 5 | View All Users | Admin listing |
| 6 | Password Validation | Strength rules enforced |

## Project Structure

```
UserManagementSystem/
├── src/
│   ├── Main.java          # Menu-driven console UI
│   ├── User.java          # Model (entity)
│   ├── UserDAO.java       # JDBC data access
│   ├── DBConnection.java  # MySQL connection
│   └── UserService.java   # Business logic & validation
├── sql/
│   └── schema.sql         # Database setup script
└── README.md
```

## Architecture (OOP Layers)

```
Main (Controller/UI)
    ↓
UserService (Business Logic)
    ↓
UserDAO (Data Access - JDBC)
    ↓
DBConnection → MySQL
```

## Prerequisites

- **JDK 11+** (or JDK 8+)
- **MySQL 8.x** running locally
- **MySQL Connector/J** (`mysql-connector-j-8.x.x.jar`)

## Database Setup

1. Start MySQL server.
2. Run the schema script:

```bash
mysql -u root -p < sql/schema.sql
```

Or execute `sql/schema.sql` in MySQL Workbench.

## Configuration

Edit `src/DBConnection.java` and set your credentials:

```java
private static final String USER = "root";
private static final String PASSWORD = "your_password";
```

## Compile & Run

### Windows (PowerShell)

```powershell
cd UserManagementSystem

# Download connector if needed:
# https://dev.mysql.com/downloads/connector/j/

javac -cp ".;lib\mysql-connector-j-8.3.0.jar" -d out src\*.java
java -cp "out;lib\mysql-connector-j-8.3.0.jar" Main
```

### Linux / macOS

```bash
cd UserManagementSystem
javac -cp ".:lib/mysql-connector-j-8.3.0.jar" -d out src/*.java
java -cp "out:lib/mysql-connector-j-8.3.0.jar" Main
```

> Place `mysql-connector-j-*.jar` in a `lib/` folder, or use your IDE to add it to the classpath.

## Password Rules

Passwords must:

- Be at least **8 characters**
- Contain **uppercase** and **lowercase** letters
- Contain at least **one digit**
- Contain at least **one special character**: `@#$%^&+=!`

**Example valid password:** `Secure@123`

## Sample Usage

1. **Register** – Menu option `1`, enter name, email, password.
2. **Login** – Menu option `2`, use registered credentials.
3. **Update** – Menu option `3`, provide user ID and new details.
4. **Delete** – Menu option `4`, confirm with `yes`.
5. **Admin list** – Menu option `5`, view all users (passwords not shown in list).

## Design Highlights

- **PreparedStatement** – All queries use parameterized statements (SQL injection safe).
- **Exception handling** – SQLException and validation errors handled in `Main`.
- **Separation of concerns** – Model, DAO, Service, and UI are separate classes.
- **Validation** – Centralized in `UserService`.

## Optional: Spring Boot Upgrade

This project uses plain Java + JDBC. To migrate to Spring Boot later:

- Replace `Main` with REST `@RestController` endpoints
- Use `spring-boot-starter-data-jpa` or keep JDBC with `JdbcTemplate`
- Add `application.properties` for datasource config

## License

Educational / portfolio use.
