# Appointment Scheduling System

This project implements an Appointment Scheduling System using Java and Maven.
It is designed following a layered architecture and applies key software engineering principles and design patterns.

## Features
- Administrator login and logout
- View available appointment slots
- Book appointments with validation rules
- Modify and cancel appointments
- Administrator management of reservations
- Support for multiple appointment types
- Appointment reminders using:
- Mock notification service (for testing)
- Email notification service (real-world simulation)

## Design Patterns
- Strategy Pattern: Used for implementing flexible booking validation rules (e.g., duration and participant limits)
- Observer Pattern: Used for notification services (mock and email reminders)

## Testing
The project includes comprehensive unit testing using:
- JUnit
- Mockito
- Test coverage exceeds 80%, fulfilling the project requirements.
- Mocking is used to simulate external services such as notifications and time handling.

## Documentation
The codebase includes detailed JavaDoc documentation for key classes and methods to improve readability and maintainability.

## Technologies Used
- Java
- Maven
- JUnit
- Mockito

## Group Members
- Noora Yousef Odeh — 12217418
- Nada Hamad Jallad — 12217495
