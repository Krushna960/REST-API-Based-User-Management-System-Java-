# User Account Management System

User registration, login, profile CRUD, admin listing, and password validation.

**Two versions in one project:**

| Version | Stack | How to run |
|---------|-------|------------|
| **Spring Boot (main)** | Spring Boot 3, REST API, JPA, MySQL | `mvn spring-boot:run` |
| **Legacy console** | Core Java, JDBC, MySQL | `legacy-console\run-legacy.bat` |

---

## Spring Boot REST API (recommended)

### Architecture

```
UserController (REST)
    ↓
UserService (business logic + validation)
    ↓
UserRepository (Spring Data JPA)
    ↓
MySQL (users table)
```

### Prerequisites

- **JDK 17+**
- **Maven 3.8+**
- **MySQL 8.x**

### Database setup

```bash
mysql -u root -p < sql/schema.sql
```

### Configuration

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

### Run

```powershell
cd UserManagementSystem
mvn spring-boot:run
```

Or double-click `run.bat`.

Server starts at: **http://localhost:8080**

### REST endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/users/register` | Signup |
| `POST` | `/api/users/login` | Login |
| `PUT` | `/api/users/{id}` | Update profile |
| `DELETE` | `/api/users/{id}` | Delete user |
| `GET` | `/api/users` | List all users (admin) |
| `POST` | `/api/users/validate-password` | Password strength check |

### Example requests (curl / Postman)

**Register**

```bash
curl -X POST http://localhost:8080/api/users/register ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"password\":\"Secure@123\"}"
```

**Login**

```bash
curl -X POST http://localhost:8080/api/users/login ^
  -H "Content-Type: application/json" ^
  -d "{\"email\":\"john@example.com\",\"password\":\"Secure@123\"}"
```

**Update user** (id = 1)

```bash
curl -X PUT http://localhost:8080/api/users/1 ^
  -H "Content-Type: application/json" ^
  -d "{\"name\":\"John Updated\",\"email\":\"john.new@example.com\",\"password\":\"\"}"
```

**Delete user**

```bash
curl -X DELETE http://localhost:8080/api/users/1
```

**Get all users**

```bash
curl http://localhost:8080/api/users
```

**Validate password**

```bash
curl -X POST http://localhost:8080/api/users/validate-password ^
  -H "Content-Type: application/json" ^
  -d "{\"password\":\"Secure@123\"}"
```

### Password rules

- Minimum **8** characters
- At least one **uppercase**, **lowercase**, **digit**, and **special** (`@#$%^&+=!`)
- Example: `Secure@123`

---

## Legacy console app (JDBC)

Original menu-driven app preserved in `legacy-console/`:

- `User.java`, `UserDAO.java`, `DBConnection.java`, `UserService.java`, `Main.java`

Run:

```powershell
legacy-console\run-legacy.bat
```

Update MySQL credentials in `legacy-console/DBConnection.java`.

---

## Project structure

```
UserManagementSystem/
├── pom.xml                          # Maven / Spring Boot
├── src/main/java/com/usermanagement/
│   ├── UserManagementApplication.java
│   ├── controller/UserController.java
│   ├── service/UserService.java
│   ├── repository/UserRepository.java
│   ├── entity/User.java
│   ├── dto/                         # Request/response objects
│   └── exception/                   # Global error handling
├── src/main/resources/
│   └── application.properties
├── legacy-console/                  # Original JDBC + menu app
├── sql/schema.sql
└── README.md
```

## Build JAR (optional)

```bash
mvn clean package
java -jar target/user-management-system-1.0.0.jar
```

## License

Educational / portfolio use.
