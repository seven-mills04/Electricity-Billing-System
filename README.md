# ⚡ Electricity Billing System

A full-stack Electricity Billing System built using **Spring Boot**, **React**, and **MySQL** to simplify electricity consumer management, meter reading, bill generation, and payment processing.

---

## 📌 Overview

The Electricity Billing System is designed to automate the workflow of electricity distribution companies by managing consumers, electricity connections, meter readings, bill generation, and payments through separate Admin and Consumer portals.

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

# 📷 Screenshots

> Add screenshots here after uploading them.

Example:

```
screenshots/
    dashboard.png
    consumer-management.png
    meter-reading.png
    billing.png
    payments.png
```

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
