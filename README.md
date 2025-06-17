# Learning-Management-System


## Description
<ins>***Learning Management System***</ins> is an application, that allows Students to enroll in variety of Courses using virtual coins. Each Course is composed of multiple Lessons. In order to keep Students informed and engaged, platform provides automated reminders for upcoming courses.

## Project goals
1. Provide starter kit with the most important and frequently repeated tasks and their solutions.
2. Good knowledge of Spring Framework:
    * Cover the most important and commonly used modules;
    * Cover main annotations;
    * Provide understanding of Spring’s main features under the hood.
3. Good knowledge of SAP BTP, understanding of how to integrate with BTP services API without libraries.

## Stack
- **Programming languages:** Java 21
- **Frameworks and Libraries:** Spring framework (Boot, MVC, Security, Data), JPA/Hibernate, Swagger, Liquibase
- **Databases:** H2, PostgreSQL, HANA DB
- **Platforms:** SAP BTP, Docker
- **Testing:** JUnit 5, Mockito
- **VCS:** Git
- **Maven plugins:** Surefire, Failsafe, JaCoCo
- **SAP BTP services:** HANA DB, XSUAA Service, Application Logging Service, Destination Service,
  Feature Flags Service, Service Manager, SaaS Registry, User-provided service

## Local setup
### Prerequisites
* Java 21
* Maven 3.9
* Docker
### Steps
1. git clone https://github.com/Zhabonchik/Learning-Management-System
2. cd Learning-Management-System
3. Add .env file in src/main/resources with properties EMAIL_USERNAME=, EMAIL_PASSWORD=.
4. docker compose-up -d
5. mvn clean install
6. mvn spring-boot:run "-Dspring-boot.run.profiles=local"