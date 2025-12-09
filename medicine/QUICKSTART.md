# Quick Start Guide - Medicine Reminder Application

## ⚡ 5-Minute Setup

### Step 1: Database Setup (2 minutes)

1. Open MySQL command line or MySQL Workbench

2. Create database:
```sql
CREATE DATABASE medireminderdb;
```

3. Run the schema:
```bash
# From project root directory
mysql -u root -p medireminderdb < schema.sql
```

**OR** copy and paste the contents of `schema.sql` into MySQL Workbench and execute.

### Step 2: Configure Database (1 minute)

Edit `src/main/resources/db.properties`:

```properties
db.url=jdbc:mysql://localhost:3306/medireminderdb
db.username=root
db.password=YOUR_PASSWORD_HERE
```

Replace `YOUR_PASSWORD_HERE` with your MySQL password.

### Step 3: Build & Run (2 minutes)

Open terminal in project directory:

```bash
# Build the project
mvn clean package

# Run with embedded Tomcat
mvn tomcat7:run
```

### Step 4: Access Application

Open browser: **http://localhost:8080/medicine-reminder**

**That's it!** 🎉

---

## 📝 Default Test Account (Optional)

If you want to test immediately, uncomment the sample data section in `schema.sql`:

```sql
INSERT INTO users (username, password) VALUES ('testuser', 'password123');
```

Then login with:
- Username: `testuser`
- Password: `password123`

---

## 🔧 Common Commands

### Build only:
```bash
mvn clean compile
```

### Package WAR file:
```bash
mvn clean package
```

### Run application:
```bash
mvn tomcat7:run
```

### Stop application:
Press `Ctrl + C` in terminal

---

## 📱 Usage Flow

1. **Register** → Create new account
2. **Login** → Enter credentials
3. **Add Medicine** → Name, dosage, notes
4. **Add Reminder** → Select medicine, date, time
5. **Mark as Taken** → Click button when you take medicine

---

## 🆘 Quick Troubleshooting

**Can't connect to database?**
- Check MySQL is running
- Verify password in `db.properties`

**Port 8080 in use?**
- Change port in `pom.xml` (line 73): `<port>8081</port>`

**Styles not loading?**
- Clear browser cache (Ctrl+F5)
- Make sure URL has `/medicine-reminder/` in path

---

## 📂 Project Highlights

✅ Pure Java Servlets (no Spring Boot)
✅ JDBC for database (no ORM/Hibernate)
✅ Vanilla JavaScript (no React/Angular)
✅ Complete CRUD operations
✅ Session management
✅ REST-style JSON API
✅ Clean 3-layer architecture

---

**Enjoy your Medicine Reminder App!** 💊
