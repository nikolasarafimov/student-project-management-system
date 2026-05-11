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

## DevOps Setup

This project was extended with a complete DevOps setup including Docker, Docker Compose, GitHub Actions CI/CD pipeline, DockerHub image publishing, and Kubernetes manifests.

---

## Technologies Used

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Thymeleaf
- Maven
- PostgreSQL
- Docker
- Docker Compose
- GitHub Actions
- DockerHub
- Kubernetes

---

## Docker Compose Setup

The application can be started locally using Docker Compose.

The Docker Compose configuration contains three services:

1. `student-project-app` - Spring Boot web application
2. `postgres-db` - PostgreSQL database
3. `pgadmin` - database administration tool

Run the application with:

```bash
docker compose up --build
```

Application URL:

```text
http://localhost:8080/projects
```

pgAdmin URL:

```text
http://localhost:5050
```

Default pgAdmin credentials:

```text
Email: admin@example.com
Password: admin
```

PostgreSQL connection inside pgAdmin:

```text
Host: postgres-db
Port: 5432
Database: student_projects_db
Username: postgres
Password: postgres
```

To stop the containers:

```bash
docker compose down
```

To stop the containers and remove the database volume:

```bash
docker compose down -v
```

---

## CI/CD Pipeline

The project contains a GitHub Actions workflow located in:

```text
.github/workflows/ci-cd.yml
```

The pipeline is triggered on every push or pull request to the main/master branch.

The pipeline performs the following steps:

1. Checks out the source code
2. Sets up Java 21
3. Builds the Spring Boot application using Maven
4. Logs in to DockerHub
5. Builds a Docker image
6. Pushes the image to DockerHub

DockerHub image:

```text
nikolasarafimov/student-project-management-system:latest
```

---

## Kubernetes Setup

The Kubernetes manifests are located in:

```text
k8s/
```

The Kubernetes setup includes:

- Namespace
- ConfigMap for application configuration
- Secret for database credentials
- Deployment for the Spring Boot application
- Service for the Spring Boot application
- Ingress for HTTP access
- StatefulSet for PostgreSQL
- Service for PostgreSQL
- PersistentVolumeClaim for PostgreSQL storage

Apply all Kubernetes manifests with:

```bash
kubectl apply -k k8s
```

Check created resources:

```bash
kubectl get all -n student-project
```

Check persistent volume claim:

```bash
kubectl get pvc -n student-project
```

Check ingress:

```bash
kubectl get ingress -n student-project
```

Check application logs:

```bash
kubectl logs -n student-project deployment/student-project-app
```

Open the application locally using port-forward:

```bash
kubectl port-forward -n student-project service/student-project-service 8080:8080
```

Then open:

```text
http://localhost:8080/projects
```

Delete all Kubernetes resources:

```bash
kubectl delete namespace student-project
```

---

## Project Repository

GitHub repository:

```text
https://github.com/nikolasarafimov/student-project-management-system
```