# Inventory Backend

This is the backend for a simple inventory management application, built with Spring Boot and secured with JSON Web Tokens (JWT). It provides a RESTful API for managing products, including creation, retrieval, updates, and deletion.

##  Technologies Used

  * **Framework:** Spring Boot 3.1.2
  * **Language:** Java 21
  * **Build Tool:** Maven
  * **Database:** PostgreSQL
  * **Security:** Spring Security, JWT (JSON Web Token)
  * **Persistence:** Spring Data JPA
  * **API Documentation:** Swagger/OpenAPI (if implemented)

##  Prerequisites

To run this backend application, you will need the following installed on your machine:

  * **Java Development Kit (JDK) 21**
  * **Apache Maven**
  * **PostgreSQL** (running on your local machine or a remote server)

## 🛠 Setup and Run Instructions

Follow these steps to get the backend running locally.

### 1\. Clone the Repository

Clone this project to your local machine using the following command:

```bash
git clone https://github.com/your-username/inventory-backend.git
cd inventory-backend
```

### 2\. Configure the Database

This application uses a PostgreSQL database.

  * Create a new PostgreSQL database (e.g., `inventory_db`).
  * Open the `src/main/resources/application.properties` file.
  * Update the database connection details with your own credentials:

<!-- end list -->

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventory_db
spring.datasource.username=your-db-username
spring.datasource.password=your-db-password

# Ensure your JWT secret key is set
jwt.secret=aVeryLongAndComplexSecretKeyThatIsAtleast32bytesForHS256
jwt.expiration=86400000
```

*Note: The `jwt.secret` must be a long, secure, and consistent value across your backend and any related services.*

### 3\. Build and Run the Application

Navigate to the project's root directory in your terminal and run the application using Maven:

```bash
mvn spring-boot:run
```

The application should start successfully on port `8080`. You will see a log message in the console indicating that "Tomcat started on port(s): 8080 (http)".

##  API Endpoints

This backend provides a secure REST API. You will need a JWT token for most of the endpoints.

### **Authentication Endpoints (Public)**

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/auth/login` | Authenticates a user and returns a JWT token. |
| `POST` | `/auth/register` | Registers a new user account. |

**Example Login Request (with Postman):**

```json
{
  "email": "testuser@gmail.com",
  "password": "test1234"
}
```

### **Product Endpoints (Protected)**

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/products` | Retrieves a list of all products. |
| `GET` | `/products/{id}` | Retrieves a single product by its ID. |
| `POST` | `/products` | Creates a new product. |
| `PUT` | `/products/{id}` | Updates an existing product. |
| `DELETE`| `/products/{id}` | Deletes a product by its ID. |

**Important:** For all protected endpoints, you must include the JWT token in the `Authorization` header.

**Example Protected Request (with Postman):**

`Authorization` : `Bearer <your-jwt-token-here>`
