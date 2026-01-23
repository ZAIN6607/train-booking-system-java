Train Booking System – Core Java Project

This is a console-based Train Booking System developed using Core Java. The project is designed to demonstrate backend fundamentals such as object-oriented programming, authentication logic, file-based data persistence, and basic application flow without using any frameworks.

The application supports the following features:
- User signup and login
- Secure password storage using hashing
- Searching trains based on source and destination
- Viewing seat availability
- Booking train seats
- Cancelling bookings

User and train data are stored using JSON files, which act as a local database. The Jackson library is used to serialize Java objects into JSON and deserialize JSON back into Java objects. Passwords are hashed using BCrypt to avoid storing plain-text credentials.

The project follows a simple layered structure:
- Model classes represent core entities such as User, Train, and Ticket
- Service classes contain business logic for authentication, booking, and train management
- A console-based entry point handles user interaction

This project focuses on applying core Java concepts including:
- Classes and objects
- Encapsulation and constructors
- Collections and nested data structures
- Java Streams for filtering and searching data
- Exception handling
- File handling and persistence
- JSON serialization and deserialization
- Basic authentication logic
- Git and GitHub version control workflow

Known limitations of the project:
- Uses JSON files instead of a real database
- Console-based user interface
- No session management
- Limited input validation and error handling
- Not thread-safe
- JSON files can break if corrupted
- Not production-ready

This project is intended as a learning-oriented implementation to understand backend logic and system flow at a foundational level. Future improvements may include converting it into a Spring Boot application, integrating a real database, exposing REST APIs, and adding a frontend interface.
