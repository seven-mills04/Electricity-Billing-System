# ⚡ Electricity Billing System

A full-stack Electricity Billing System built using **Spring Boot**, **React**, and **MySQL** to simplify electricity consumer management, meter reading, bill generation, and payment processing.

---

## 📌 Overview

The Electricity Billing System is designed to automate the workflow of electricity distribution companies by managing consumers, electricity connections, meter readings, bill generation, and payments through separate Admin and Consumer portals.

---

## 🔗 Project Links

🌐 **Live Demo:** [Open Website](https://electricity-billing-ui.vercel.app/)

💻 **Frontend Repository:** [electricity-billing-ui](https://github.com/seven-mills04/electricity-billing-ui)

⚙️ **Backend Repository:** [Electricity-Billing-System](https://github.com/seven-mills04/Electricity-Billing-System)

---

## 🚀 Features

### 👨‍💼 Admin Portal

- Consumer Management (Add, Update, Delete)
- Electricity Connection Management
- Meter Reading Management
- Automatic Bill Generation
- Payment Tracking
- Dashboard Analytics
- Bill Status Monitoring
- Consumer Search & Filtering

### 👤 Consumer Portal

- Secure Login
- View Personal Dashboard
- View Current Bills
- Payment History
- Electricity Usage Details
- Download Bills
- Profile Management

---

## 🛠 Tech Stack

### Backend

- Java 17
- Spring Boot
- Spring Data JPA
- Spring Security
- Hibernate
- REST APIs
- Gradle

### Frontend

- React
- Material UI
- React Router
- Axios
- Recharts

### Database

- MySQL

### Tools

- IntelliJ IDEA
- VS Code
- Postman
- Git
- GitHub

---

# 📂 Project Structure

```
Electricity-Billing-System
│
├── backend
│   ├── Controller
│   ├── Service
│   ├── Repository
│   ├── Entity
│   ├── DTO
│   ├── Security
│   └── Configuration
│
├── frontend
│   ├── Components
│   ├── Pages
│   ├── Services
│   ├── Routes
│   └── Assets
│
└── Database
```

---

# ⚙️ Modules

## Consumer Module

- Register Consumers
- Edit Consumer Details
- Delete Consumers
- View Consumer Information

---

## Connection Module

- Create Electricity Connections
- Assign Consumer Numbers
- Manage Connection Status

---

## Meter Reading Module

- Add Meter Readings
- Calculate Units Consumed
- Store Reading History

---

## Billing Module

- Automatic Bill Generation
- Tariff Calculation
- Due Date Management
- Paid / Unpaid Status

---

## Payment Module

- Bill Payment
- Payment History
- Transaction Tracking

---

## Dashboard

Displays

- Total Consumers
- Total Connections
- Paid Bills
- Pending Bills
- Revenue Statistics

---

# 🔒 Authentication

The application provides separate authentication for:

- Admin
- Consumer

---

# 📊 Bill Calculation

The system automatically calculates electricity bills based on:

- Units Consumed
- Sanctioned Load
- Fixed Charges
- Energy Charges

---

## 📸 Screenshots

### 🏠 Landing Page

![Landing Page] <img width="1119" height="608" alt="Screenshot 2026-08-04 125507" src="https://github.com/user-attachments/assets/e9d0d52b-40b9-4e8c-8307-5940251da1a2" />


---

### 📊 Admin Dashboard

![Admin Dashboard] <img width="1119" height="605" alt="image" src="https://github.com/user-attachments/assets/672ae72f-8759-4ed6-a9bc-4d9aa3c2c3dd" />


---

### 👥 Consumer Management

![Consumer Management]  <img width="1119" height="613" alt="image" src="https://github.com/user-attachments/assets/54af0526-ae76-455c-877d-e8b7bb7785d4" />


---

### ⚡ Meter Reading

![Meter Reading] <img width="1119" height="615" alt="image" src="https://github.com/user-attachments/assets/d3fe33a9-2fd6-4955-8891-942906b1ad02" />


---

### 🧾 Bill Generation

![Bill Generation]  <img width="1118" height="610" alt="image" src="https://github.com/user-attachments/assets/fcc7ed83-a8d0-4026-86f2-62d675ed4e80" />


---

### 💳 Payment History

![Payment History] <img width="1119" height="609" alt="image" src="https://github.com/user-attachments/assets/38fda384-e1d0-44a5-bcd5-5a4a1876e8cb" />


---

# ▶️ Running the Project

## Backend

```bash
git clone https://github.com/seven-mills04/Electricity-Billing-System.git

cd Electricity-Billing-System

./gradlew bootRun
```

---

## Frontend

```bash
cd electricity-billing-ui

npm install

npm run dev
```

---

## Database

Create a MySQL database.

Update

```
application.properties
```

with your

- Database URL
- Username
- Password

---

# 📡 REST APIs

Major APIs include

```
/api/consumers
/api/connections
/api/meter-readings
/api/bills
/api/payments
```

---

# 🌟 Future Improvements

- Email Notifications
- Online Payment Gateway
- SMS Alerts
- PDF Bill Download
- Smart Meter Integration
- Docker Deployment
- Cloud Hosting

---

# 📈 Project Highlights

✔ Full Stack Development

✔ RESTful API Architecture

✔ Responsive React UI

✔ Role-Based Authentication

✔ Automatic Bill Generation

✔ Dashboard Analytics

✔ CRUD Operations

✔ MySQL Database Integration

---

# 👨‍💻 Author

**Krishna Navneet Kumar**

GitHub:
https://github.com/seven-mills04

LinkedIn:
(Add your LinkedIn Profile)

---

## ⭐ If you found this project helpful, consider giving it a star!
