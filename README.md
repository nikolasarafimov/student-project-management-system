# Student Project Management System

A full-stack web application for managing student project proposals and approvals, developed as part of the **Web Programming** course at **Faculty of Computer Science and Engineering (FINKI), Ss. Cyril and Methodius University in Skopje**.

**Developed by:**
- Simeon Minoski
- Nikola Sarafimov

**Course Instructor:** 
Prof. Sasho Gramatikov

**Course:** Web Programming

**Academic Year:** 2024/2025

---

## 📌 Project Overview

The **Student Project Management System** provides a structured way for students to create, manage, and submit project ideas, while professors are responsible for reviewing, validating, and approving them.

It streamlines project submission and prevents duplicate efforts by enabling powerful search and validation workflows.

---

## 🚀 Features

### 👩‍🎓 Student Features
- Register and log in securely.
- Create and edit projects with the following fields:
    - Topic
    - Description
    - Labels (multiple, comma-separated)
    - Technologies (multiple, comma-separated; seeded values: Spring Boot, Java, React)
    - Course
    - Responsible Teacher
    - Extension from another project (optional)
    - Repository link
- Projects start in **Draft** mode.
- Submit projects for professor validation.
- Cancel a submission if still in **Submitted** state.
- Edit or delete projects when in **Draft** or **Rejected** state.
- Search existing projects by **topic, description, or labels**.

### 👨‍🏫 Professor Features
- Review and validate submitted projects.
- Approve or reject projects.
- Prevent duplicate project ideas.
- View and search all projects.

### 🔍 Search
- Global search across **topic, description, and labels**.
- Ensures awareness of existing or similar project ideas.

---

## 🛠️ Tech Stack

- **Backend:** Spring Boot 3 (Spring MVC, Spring Data JPA, Spring Security)
- **Frontend:** Thymeleaf with Bootstrap 5
- **Database:** PostgreSQL / H2 (for testing)
- **Build Tool:** Maven
- **Language:** Java 21

---

## 📂 Project Structure
`src/main/java/mk/ukim/finki/wp/seminarska`:
- config/ # Security and initialization configuration
- model/ # Domain models: Project, Label, Technology, AppUser, Role, Status
- repository/ # JPA repositories
- service/ # Service layer interfaces
    - impl/ # Service implementations
- web/ # Web controllers (ProjectController, RegistrationController)
- SeminarskaApplication.java

Templates in `src/main/resources/templates`:
- `projects.html` – project list and search page
- `form.html` – add/edit project form
- `register.html` – user registration form

---

## ⚙️ Installation & Setup

### 1. Clone the repository
`bash`
git clone https://github.com/simeminoski87/seminarska.git
cd seminarska

### 2. Configure database
Edit `src/main/resources/application.properties` to match your PostgreSQL setup,
or use the default H2 in-memory database for quick testing.

### 3. Run the application
mvn spring-boot:run

Visit the app at:
👉 http://localhost:8080/projects

### 4. Default users

The system includes demo accounts initialized at startup:
- Student
Username: `student`
Password: `student`

- Teacher
Username: `teacher`
Password: `teacher`

Additional demo teachers and projects are also seeded.

## Docker setup

The application can be started with Docker Compose using the following command:

```bash
docker compose up --build
```

The Docker Compose setup includes three services:
- Spring Boot web application
- PostgreSQL database
- pgAdmin database administration tool

Application URL:
```bash
http://localhost:8080
```

pgAdmin URL:
```bash
http://localhost:5050
```

Default pgAdmin credentials:
- Email: `admin@example.com`
- Password: `admin`