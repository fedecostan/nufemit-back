# NUFEMIT-BACK

This is the backend application in charge of managing all NUFEMIT application functionality exposing all endpoints and database administration.

## Prerequisites

Before running this application, ensure that the following prerequisites are met:

- Java Development Kit (JDK) 11 or higher is installed.
- Maven build tool is installed.
- MySQL database configured

## Getting Started

To get started with the application, follow these steps:

1. Clone the repository: `git clone [<repository-url>](https://github.com/fedecostan/nufemit-back.git)`
2. Navigate to the project directory: `cd nufemit-back`
3. Build the application: `mvn clean install`
4. Run the application: `mvn spring-boot:run`

The application will start running on `http://localhost:8080`. You can access the application in your browser or by sending HTTP requests to the provided endpoints.

## Configuration

The application can be configured using the `application.yml` file located in the `src/main/resources` directory. Modify this file to customize the application behavior, such as database connection details, server port, and other properties.

## Project Structure

The project structure follows the standard Spring Boot application layout. The main components of the project are:

- `src/main/java`: Contains the Java source code files.
- `src/main/resources`: Contains the application configuration files.
- `src/test`: Contains the test cases for the application.

## Documentation

For detailed information on using Spring Boot, refer to the official [Spring Boot documentation](https://spring.io/projects/spring-boot).

## Contributing

If you'd like to contribute to this project, please follow these guidelines:

1. Fork the repository.
2. Create a new branch for your feature: `git checkout -b my-feature`.
3. Commit your changes: `git commit -am 'Add new feature'`.
4. Push the branch to your forked repository: `git push origin my-feature`.
5. Submit a pull request with your changes.
