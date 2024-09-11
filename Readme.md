# Mplex Demo Project in Spring Boot

This project is a demonstration of a Spring Boot application simulating key functionalities of the Modaplex software. It
serves as a simplified, backend-focused example of the original JavaFX-based desktop application, designed to showcase
proficiency in Spring Boot and to illustrate a transition from desktop to web-based architecture.

## Project Structure

The project is organized into the following main components:

**client**

- The main application that contains the logic for controlling the laboratory device.
- it runs on [http://localhost:8090](http://localhost:8090).
- The application includes a service client, which is used by a service technician for maintenance or control of the
  device. (Username + Password = `service`)

**emulator**

- Application for simulating the hardware environment for testing purposes.
- It runs on [http://localhost:8091](http://localhost:8091).
- It allows the software to be tested without needing the physical device.

**device**

- This is a shared module that provides services for communication between the client and the hardware (or the
  emulator).
- It acts as an interface between the software (client) and the actual or simulated hardware (emulator).

## Technologies Used

- **Java 21**: The primary programming language.
    - **_java.net.http_** for WebSocket client connection in the client.
    - **_Lombok_** for reducing boilerplate code.
- **JUnit**: For unit testing the components.
- **Maven**: For project management and dependency resolution.
- **Spring Boot**: For building the RESTful backend services:
    - **_Jackson_** for device interface API.
    - **_Spring Security_** for logging in as service technician in client (Username + Password: `service`)
    - **_Spring Events_** for receiving events from the device.
    - **_org.springframework.web.socket_** for WebSocket server connection in the emulator.

## Getting Started

- Ensure that JDK 21 or higher is installed and that it is set as your JAVA_HOME

1. Clone the repository:
    ```bash
    git clone https://github.com/cmiethling/mplex_demo_app.git
    ```  
2. Navigate to the project directory:
   ```bash
   cd mplex_demo_app
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

#### Running the Applications:

Start the Spring Boot applications using the script:

```bash
./start-all.sh
```

- The client will start on http://localhost:8090.
- The emulator will start on http://localhost:8091.
- Stop the apps by pressing `Ctrl + C`

#### Running Tests

Execute the unit tests using Maven:

```bash
mvn test
```

## Project Scope

This demo project is an ongoing effort and future updates may include additional features.