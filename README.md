# Ecommerce Shop

`ecommerce_shop` is a robust backend e-commerce system developed using Spring Boot. It provides core functionalities for managing users, products, categories, orders, and more. Built with a modular, service-oriented architecture, the project leverages JWT for security, AWS S3 for media storage, and MySQL for database management.

## Table of Contents
- [Acknowledgements](#acknowledgements)

- [Project Structure](#Project-Structure)
- [Dependencies](#Dependencies)
- [Getting Started](#Getting-Started)
- [Authors](#authors)
- [Badges](#badges)
- [Color Reference](#color-reference)




  
- [Environment Variables](#environment-variables)
- [Tech Stack](#tech-stack)
- [Features](#features)

- [Lessons Learned](#lessons-learned)
- [License](#license)


- [Related Projects](#related-projects)


- [Screenshots](#screenshots)
- [Support](#support)






## Acknowledgements
Special thanks to the Spring Boot, MySQL, and AWS developer communities for their extensive documentation and resources, which greatly assisted in building this project.




## Project-Structure
The project is organized into the following main packages:
- **controller**: Contains REST API controllers like `AddressController`, `AuthController`, `CategoryController`, `OrderItemController`, `ProductController`, and `UserController`.
- **dto**: Data Transfer Objects to handle request and response models.
- **entity**: Core entities that map to database tables, such as `Product`, `User`, `Order`, etc.
- **enums**: Enumerations for managing constants like roles, order statuses, etc.
- **exceptions**: Custom exception classes to handle application-specific errors.
- **mapper**: Utility classes for mapping entities to DTOs.
- **repository**: Repositories for data persistence with Spring Data JPA.
- **security**: JWT and Spring Security configurations for authentication and authorization.
- **service**: Business logic layer, with separate `impl` and `interf` sub-packages.
  - **impl**: Contains the service implementations (`AddressServiceImpl`, `CategoryServiceImpl`, `ProductServiceImpl`, `UserServiceImpl`).
  - **interf**: Interfaces defining service layer methods.
  - **AwsS3Service**: Service for handling AWS S3 operations.

## Dependencies
The project uses the following dependencies:
- **Spring Boot Starter JPA**: For ORM and database interaction.
- **Spring Boot Starter Security**: For implementing authentication and authorization.
- **Spring Boot Starter Validation**: For validating API requests.
- **Spring Boot Starter Web**: To build RESTful web services.
- **MySQL Connector/J**: To connect to a MySQL database.
- **Lombok**: To reduce boilerplate code with annotations.
- **Spring Boot Starter Test**: For unit testing.
- **Spring Security Test**: For security-related testing.
- **AWS Java SDK S3**: For integration with AWS S3 for image storage.
- **jjwt**: For JWT token generation and validation.
  
## Getting-Started
 # 1.Clone the Repository:
 1.git clone https://github.com/RakeshJambula/ecommerceshop.git.
 
 2. cd ecommerce_shop.

 3. install dependencies.
 
 4. mvn clean install.

# 2.Set up the MySQL Database:

 1. Ensure MySQL is running and create a database:

 2. CREATE DATABASE ecommerce_shop_db;

 3. ./mvnw spring-boot:run

# 3.Configure Application Properties:

Update application.properties with your MySQL and AWS credentials.

# 4.Build and Run the Application:

./mvnw spring-boot:run

The application should now be running at http://localhost:8080.

# 5.Testing:

Run the test suite with:

./mvnw test

## Authors
- **Rakeshjambula** - [Your GitHub](https://github.com/RakeshJambula)

## Badges
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.1.2-green)
![License](https://img.shields.io/badge/license-MIT-blue)

## Color Reference
- Primary Color: `#3498db`
- Secondary Color: `#2ecc71`








## Environment Variables
To run this project, you will need to add the following environment variables to your `.env` file or `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_shop_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JWT Secret Key
secreteJwtString=your_jwt_secret

# AWS S3 Configurations
aws.s3.access=your_aws_access_key
aws.s3.secrete=your_aws_secret_key

# Tech Stack
• Java: Version 17
• Spring Boot: Version 3.1.2
• MySQL: Version 8.0
• AWS S3: SDK for Java

# Features
• User Authentication & Authorization: JWT-based login and role management.
• Product & Category Management: CRUD operations for products and categories.
• Order & Cart Management: Supports order placement and order item management.
• Image Upload: AWS S3 integration for media storage.
• Exception Handling: Centralized global exception handling.



# Lessons Learned
• Streamlining REST API design with Spring Boot.
• Efficiently managing cloud storage for media files.
• Implementing role-based access with Spring Security.



# License
This project is licensed under the MIT License. See the LICENSE file for details.

# Related Projects
Ecommerce Frontend- react-ecommerce

# Support
For support, reach out at -jambularakeshreddy123@gmail.com.






Thank you for using ecommerce_shop. Contributions are welcome!


