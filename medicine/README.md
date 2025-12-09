# Medicine Reminder Web Application

A complete, production-ready web application for managing medicine schedules and reminders built with core Java, Servlets, JDBC, MySQL, and vanilla JavaScript.

## 📋 Features

- **User Authentication**: Register and login with username/password
- **Medicine Management**: Add, view, and delete medicines with dosage and notes
- **Reminder System**: Create reminders for medicines with date and time
- **Daily Overview**: View all reminders scheduled for today
- **Mark as Taken**: Track which medicines have been taken

## 💻 Tech Stack

### Backend
- **Java 11+** (core Java, no Spring Boot)
- **Servlets/JSP** for HTTP request handling
- **JDBC** for database access (no ORM)
- **Maven** for build management
- **MySQL 8.0+** for data persistence
- **Gson** for JSON serialization

### Frontend
- **HTML5** for structure
- **CSS3** for styling
- **Vanilla JavaScript** for interactivity (no frameworks)

## 🚀 Getting Started

### Prerequisites

1. **Java Development Kit (JDK) 11 or higher**
   - Download from: https://adoptium.net/
   - Verify installation: `java -version`

2. **Apache Maven 3.6+**
   - Download from: https://maven.apache.org/download.cgi
   - Verify installation: `mvn -version`

3. **MySQL Server 8.0+**
   - Download from: https://dev.mysql.com/downloads/mysql/
   - Ensure MySQL service is running

4. **Apache Tomcat 9.0+** (optional, Maven plugin included)
   - Download from: https://tomcat.apache.org/download-90.cgi

### Step 1: Database Setup

1. **Start MySQL Server** (if not already running)

2. **Create the database**:
   ```sql
   CREATE DATABASE medireminderdb;
   USE medireminderdb;
   ```

3. **Run the schema script**:
   ```bash
   mysql -u root -p medireminderdb < schema.sql
   ```
   
   Or manually execute the SQL commands in `schema.sql` using MySQL Workbench or command line.

4. **Verify tables were created**:
   ```sql
   SHOW TABLES;
   -- Should show: users, medicines, reminders
   ```

### Step 2: Configure Database Connection

Edit `src/main/resources/db.properties`:

```properties
# Update these values to match your MySQL setup
db.url=jdbc:mysql://localhost:3306/medireminderdb
db.username=root
db.password=YOUR_MYSQL_PASSWORD
db.driver=com.mysql.cj.jdbc.Driver
```

**Important**: Replace `YOUR_MYSQL_PASSWORD` with your actual MySQL root password.

### Step 3: Build the Project

Navigate to the project directory and run:

```bash
mvn clean package
```

This will:
- Download all dependencies
- Compile Java classes
- Run tests (if any)
- Create a WAR file in `target/medicine-reminder.war`

### Step 4: Run the Application

#### Option A: Using Maven Tomcat Plugin (Recommended for Development)

```bash
mvn tomcat7:run
```

The application will start on: **http://localhost:8080/medicine-reminder**

#### Option B: Deploy to External Tomcat

1. Copy the WAR file:
   ```bash
   cp target/medicine-reminder.war [TOMCAT_HOME]/webapps/
   ```

2. Start Tomcat:
   ```bash
   [TOMCAT_HOME]/bin/startup.sh   # Linux/Mac
   [TOMCAT_HOME]\bin\startup.bat  # Windows
   ```

3. Access the application at: **http://localhost:8080/medicine-reminder**

## 📖 How to Use

### First Time Setup

1. **Open the application** in your browser: `http://localhost:8080/medicine-reminder`

2. **Register a new account**:
   - Click "Register here"
   - Enter username and password
   - Click "Register"

3. **Login**:
   - Enter your credentials
   - Click "Login"

### Managing Medicines

1. **Add a Medicine**:
   - Fill in medicine name (required)
   - Optionally add dosage (e.g., "500mg")
   - Optionally add notes (e.g., "Take with food")
   - Click "Add Medicine"

2. **Delete a Medicine**:
   - Click the "Delete" button next to any medicine
   - Confirm the deletion
   - Note: This also deletes all reminders for that medicine

### Managing Reminders

1. **Add a Reminder**:
   - Select a medicine from the dropdown
   - Choose date (defaults to today)
   - Choose time (24-hour format)
   - Click "Add Reminder"

2. **View Today's Reminders**:
   - Automatically displays all reminders scheduled for today
   - Shows medicine name, time, and status

3. **Mark as Taken**:
   - Click "Mark as Taken" button for any pending reminder
   - Reminder will be marked as completed

## 🗂️ Project Structure

```
medicine/
├── pom.xml                          # Maven build configuration
├── schema.sql                       # Database schema
├── README.md                        # This file
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/example/medireminder/
    │   │       ├── model/           # POJO classes
    │   │       │   ├── User.java
    │   │       │   ├── Medicine.java
    │   │       │   └── Reminder.java
    │   │       ├── dao/             # Data Access Layer (JDBC)
    │   │       │   ├── UserDao.java
    │   │       │   ├── MedicineDao.java
    │   │       │   └── ReminderDao.java
    │   │       ├── service/         # Business Logic Layer
    │   │       │   ├── UserService.java
    │   │       │   ├── MedicineService.java
    │   │       │   └── ReminderService.java
    │   │       ├── servlet/         # HTTP Controllers
    │   │       │   ├── RegisterServlet.java
    │   │       │   ├── LoginServlet.java
    │   │       │   ├── MedicineServlet.java
    │   │       │   ├── DeleteMedicineServlet.java
    │   │       │   ├── ReminderServlet.java
    │   │       │   └── MarkReminderServlet.java
    │   │       └── util/            # Utility Classes
    │   │           └── DBConnection.java
    │   ├── resources/
    │   │   └── db.properties        # Database configuration
    │   └── webapp/                  # Frontend files
    │       ├── WEB-INF/
    │       │   └── web.xml          # Servlet configuration
    │       ├── index.html           # Login/Register page
    │       ├── dashboard.html       # Main dashboard
    │       ├── style.css            # Styles
    │       └── app.js               # JavaScript logic
```

## 🔌 API Endpoints

### User Management
- `POST /register` - Register new user
- `POST /login` - Login user

### Medicine Management
- `GET /medicines` - Get all medicines for logged-in user
- `POST /medicines` - Add new medicine
- `POST /medicines/delete` - Delete medicine by ID

### Reminder Management
- `GET /reminders` - Get today's reminders for logged-in user
- `POST /reminders` - Add new reminder
- `POST /reminders/markTaken` - Mark reminder as taken

## 🔒 Security Notes

**Important**: This is a demonstration application. For production use, implement:

1. **Password Hashing**: Currently stores plain text passwords
   - Use BCrypt or similar hashing algorithm

2. **Input Validation**: Add server-side validation
   - Prevent SQL injection (PreparedStatements already used)
   - Validate data formats and lengths

3. **HTTPS**: Use SSL/TLS for encrypted communication

4. **Session Security**: Implement session timeouts and CSRF protection

5. **Authentication**: Add proper authentication filters

## 🛠️ Troubleshooting

### Database Connection Issues

**Error**: `Unable to connect to database`

**Solution**:
1. Verify MySQL is running: `mysql -u root -p`
2. Check credentials in `db.properties`
3. Ensure database exists: `SHOW DATABASES;`
4. Check MySQL port (default: 3306)

### Maven Build Fails

**Error**: `Failed to execute goal`

**Solution**:
1. Check Java version: `java -version` (requires 11+)
2. Clear Maven cache: `mvn clean`
3. Update dependencies: `mvn dependency:purge-local-repository`

### Tomcat Port Already in Use

**Error**: `Port 8080 already in use`

**Solution**:
1. Change port in `pom.xml` (Tomcat plugin configuration)
2. Or kill process using port 8080

### CSS/JS Not Loading

**Issue**: Styles or scripts not loading on dashboard

**Solution**:
1. Ensure you're accessing via `/medicine-reminder/` context path
2. Clear browser cache (Ctrl+F5)
3. Check browser console for 404 errors

## 📝 Development Notes

### Why Maven?
- Standardized dependency management
- Easy integration with IDEs (IntelliJ, Eclipse)
- Built-in Tomcat plugin for quick testing
- Wide adoption in Java community

### Database Design
- **Foreign keys with CASCADE DELETE**: Automatically removes related records
- **Indexes on user_id**: Optimizes queries for user-specific data
- **TINYINT for boolean**: MySQL-compatible boolean representation

### Code Architecture
- **3-Layer Architecture**: Model → DAO → Service → Servlet
- **Separation of Concerns**: Each layer has specific responsibility
- **PreparedStatements**: Prevents SQL injection attacks
- **Try-with-resources**: Ensures database connections are closed

## 📄 License

This project is provided as-is for educational purposes.

## 🤝 Contributing

This is a demonstration project. Feel free to fork and modify for your needs.

## 📧 Support

For issues or questions, please check the troubleshooting section above.

---

**Built with ❤️ using Core Java, JDBC, and Vanilla JavaScript**
