# 🛒 OTP CRUD Online Store App with Email & OTP Verification

A secure and user-friendly online store application built using **Spring Boot**, with complete **CRUD functionality** and robust **email-based OTP verification**. The system ensures user identity validation during registration via OTP sent to their email, along with role-based access for customers and admins.

---

## 📦 Installation & Setup (Running from ZIP)

1. 📁 Download the ZIP file of the OTP CRUD Store App.
2. 🗂️ Extract it to your preferred location.
3. 💻 Open a terminal or command prompt inside the extracted directory.

---

## ✅ Pre-requisites

1. **Java 17 or higher**  
   Install and verify:
   ```bash
   java -version
   ```

2. **Apache Maven**  
   Install and verify:
   ```bash
   mvn -version
   ```

3. **XAMPP (for MySQL & Apache)**  
   - Start XAMPP  
   - Ensure **Apache** and **MySQL** are running on default ports (80, 3306)

4. **Database Setup**  
   - Go to: [http://localhost/phpmyadmin](http://localhost/phpmyadmin)  
   - Create a database as configured in `application.properties`  
     Example: `crud_store_db`

---

## ⚙️ Configuration

1. Open: `src/main/resources/application.properties`

2. Update the email configuration with your SMTP and credentials:

   ```properties
   # Email account used to send verification and OTP mails
   spring.mail.username=your_email@example.com
   spring.mail.password=your_email_password
   spring.mail.host=smtp.example.com
   spring.mail.port=587
   spring.mail.protocol=smtp
   spring.mail.properties.mail.smtp.auth=true
   spring.mail.properties.mail.smtp.starttls.enable=true
   ```

3. Save the file.

---

## ▶️ Running the Application

Navigate to the extracted project folder:

```bash
cd crud_app  # or your actual folder name
```

Run the Spring Boot app:

```bash
mvn spring-boot:run
```

---

## 🌐 Accessing the Application

- Open your browser  
- Visit: [http://localhost:8080](http://localhost:8080)  
- You’ll see the Online Store homepage

---

## 🔐 How It Works

### 1. 👤 Registration  
- User registers using email and other details  
- An OTP is sent to the provided email  

### 2. 🔑 OTP Verification  
- User enters OTP to verify  
- On success, the account is activated  

### 3. 🔓 Login  
- Verified users can log in  

### 4. 🛡️ Role-Based Access  
- Admin and Customer roles  
- Admins manage store and users  
- Customers manage profile and browse items  

### 5. ✏️ CRUD Operations  
- Full Create, Read, Update, Delete access post-login  

---

## 🎯 Features

- ✅ Email verification via SMTP during registration  
- 🔐 OTP-based authentication  
- 🧩 Configurable email sender via `application.properties`  
- 🧑‍💼 Role-based access: Admin vs Customer  
- 📦 Product CRUD functionality  
- 💻 Responsive and clean HTML/CSS UI  
- 💡 Simple and secure verification process  
![Image](https://github.com/user-attachments/assets/28f8df69-acb7-437a-8235-b270877db579)
![Image](https://github.com/user-attachments/assets/6d7f79db-701c-43c2-bdbe-01da5279036d)
![Image](https://github.com/user-attachments/assets/ff1a8fa0-0da5-4eb3-9ada-0b5b43904cfe)
![Image](https://github.com/user-attachments/assets/f8f5ab51-019d-480c-9dd7-9c7135e6c3b4)
---

## 📬 Contact

📞 +91 7019890924 | 📧 binayuppensharma@gmail.com | 🔗 [LinkedIn](https://www.linkedin.com/in/binay-uppen-sharma-180501323) | 🐙 [GitHub](https://github.com/Uppen-Sharma)

---

⭐ If you found this project helpful, feel free to give it a **star**!
