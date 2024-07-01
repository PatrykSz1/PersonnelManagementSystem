Overview
The Person Management System is a robust application designed to manage various types of individuals, including employees, students, and retirees. The system is built to accommodate future types of individuals without requiring modifications to existing classes. Key features include flexible search capabilities, pagination, data import via CSV, and comprehensive validation and error handling.

Key Features
Single Endpoint for Retrieving Persons: Allows retrieval of persons from the database based on various search criteria including type, name, surname, age, PESEL, gender, height range, weight range, and email address. Additional filters are available for employees (current salary) and students (university name). The endpoint supports pagination.

Single Endpoint for Adding Persons: Enables the addition of any type of person to the system with a contract that supports the future extension of person types without modifying existing classes. It includes sensible validation and error handling.

Single Endpoint for Editing Persons: Facilitates the editing of any person's details. The system ensures data consistency by handling concurrent modifications and raising exceptions if the data being edited has changed during the transaction.

Endpoint for Managing Employee Positions: Manages an employee's positions, including the position name, salary, and date range. The system ensures no overlapping dates for positions.

CSV File Import Endpoint: Allows the import of persons from a CSV file, handling large file sizes (e.g., 3GB) in a non-blocking manner. An additional endpoint provides the import status, including creation date, start date, and the number of processed rows. Only one import can be processed at a time.

Technologies Used
Spring Boot: Framework for building the application.
Spring Data JPA: For data persistence and database interaction.
Spring MVC: For building RESTful web services.
Spring AOP: For implementing method execution time logging.
Spring Batch: For handling CSV file processing and import.
Hibernate: For ORM and handling concurrent data modifications.
MySQL: Database for persisting person data.
Docker: Containerization for easier deployment and environment setup.
Spring Boot Test Containers: For integration testing.
Project Setup
Prerequisites
Java 11+
Maven 3.6+
Docker
