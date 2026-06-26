# SecurePass Analyzer

SecurePass Analyzer is a full-stack cybersecurity-themed password strength checker built with Spring Boot, Thymeleaf, JavaScript, and MySQL. It demonstrates DSA concepts through a custom Trie for dictionary-pattern detection and a HashMap for weak-password lookup.

## Features

- Password strength scoring with length, casing, digits, symbols, repeated characters, sequential patterns, keyboard patterns, and entropy estimation
- HashMap-based detection for common dangerous passwords such as `123456`, `password`, `admin`, `qwerty`, and `welcome`
- Custom Trie with insert, search, prefix matching, and dictionary-pattern analysis for values like `password123`, `admin@2025`, and `welcome1`
- Simulated breached-password lookup from a local dataset
- Secure random password suggestion generator with configurable length
- Password history tracking in MySQL using SHA-256 fingerprints instead of storing raw passwords
- Cybersecurity dashboard UI with dark/light mode, progress meter, alerts, toast notifications, copy button, and real-time validation
- CSRF protection, server-side validation, XSS-safe Thymeleaf rendering, and secure response headers

## Tech Stack

- Java 21
- Spring Boot 3
- Spring MVC
- Spring Security
- Spring Data JPA
- Thymeleaf
- MySQL
- HTML, CSS, JavaScript
- Maven Wrapper


## Project Structure

```text
src/main/java/com/securepass/analyzer
  config/             Security configuration
  controller/         MVC and REST controllers
  datastructures/     Custom Trie implementation
  dto/                Request and response records
  model/              JPA entities and enums
  repository/         Spring Data repositories
  service/            Password analysis business logic
src/main/resources
  data/               Local breach simulation dataset
  static/             CSS and JavaScript
  templates/          Thymeleaf pages
```

## Installation

1. Clone the repository.

2. Start MySQL:

```bash
docker compose up -d
```

3. Configure environment variables if you are not using the Docker defaults:

```bash
DB_URL=jdbc:mysql://localhost:3306/securepass_analyzer?createDatabaseIfNotExist=true
DB_USERNAME=root
DB_PASSWORD=securepass
```

4. Download the Maven wrapper jar if it is not already committed:

```bash
curl -o .mvn/wrapper/maven-wrapper.jar https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar
```

5. Run the application:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

6. Open:

```text
http://localhost:8080
```

## Environment Variables

| Variable | Default | Purpose |
| --- | --- | --- |
| `DB_URL` | `jdbc:mysql://localhost:3306/securepass_analyzer...` | MySQL connection URL |
| `DB_USERNAME` | `root` | MySQL username |
| `DB_PASSWORD` | empty | MySQL password |
| `JPA_DDL_AUTO` | `update` | Schema management mode |
| `PORT` | `8080` | Server port |

## Future Improvements

- Integrate a real k-anonymity breach API
- Add authenticated user accounts and personal analysis history
- Export password-health reports as PDF
- Add rate limiting for public deployments
- Expand dictionary datasets by language and domain

