# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.5.0] - 11.06.2025 - Author: Mikita Shapel

### Added

- Request filter to set current tenant info from JWT into request context
- Logic for new database schema creation/deletion on tenant
  subscription/unsubscription locally
- Logic for new HANA schema creation/deletion via Service Manager
  API on tenant subscription/unsubscription in cloud
- Configured connection to required schema using Hibernate
- Data source with connection pools per tenant
- Endpoint /dependencies that returns Destination service xsappname
  during subscription
- Destination with SMTP server credentials on the Subscriber subaccount
  level with the same name as at Provider subaccount level

### Changed

- Adapt Liquibase schema migration to multitenant mode
- Adjust logic with Destination service integration to multitenant mode

## [1.4.0] - 04.06.2025 - Author: Mikita Shapel

### Added

- Approuter
- API that returns tokenUrl, clientId, clientSecret of bound XSUAA
  service instance and allow access for ADMIN
  role only
- xs-security.json file with roles templates for XSUAA
- API to receive subscription/unsubscription requests from SaaS
  Provisioning Service that returns tenant-specific approuter URL + endpoint
  from Step 2 on subscription
- saas-provisioning.json file with application details for SaaS
  Provisioning Service to register application in marketplace
- Subscriber subaccount

### Changed

- Update Approuter’s welcomFile path to endpoint from Step 2

## [1.3.0] - 27.05.2025 - Author: Mikita Shapel

### Added

- New entities ClassroomLesson and VideoLesson and keep common fields in
  Lesson class
- Language field to Student for email message localization purpose
- Email message templates for notifications about course start in different
  languages using Mustache
- Audit fields
- Pagination for GET all requests
- Surefire maven plugin for unit tests
- Failsafe maven plugin for integration tests
- Jacoco maven plugin for test coverage reports generation

### Fixed

- N+1 problem
- Split tests into two folders (unit and integration) based on type of test

## [1.2.0] - 15.05.2025 - Author: Mikita Shapel

### Added

- Storage of user details in memory for Basic Auth flow
- Configured security for Spring Actuator endpoints locally and allow access for
  MANAGER role only
- Configured security for Spring Actuator endpoints in cloud (Basic Auth) and allow
  access for MANAGER role only
- Run PostgreSQL in Docker and switch to it locally

### Changed

- Replace manifest.yaml with mta.yaml and redeploy application

### Fixed

- Secured API endpoints locally and allow access for all authenticated users
- Secured API endpoints in cloud using XSUAA service and allow access for all
  authenticated users

### Removed

- manifest.yaml
- H2 database

## [1.1.0] - 06.05.2025 - Author: Mikita Shapel

### Added

- manifest.yaml for application deployment
- Binding of HANA DB, Application Logging Service, Destination Service and
  Feature Flags Service to application
- User-provided service with SMTP server credentials
- Destination with SMTP server credentials
- Logic for retrieving SMTP server credentials via Destination Service
  API
- Logic for switching SMTP server credentials retrieval strategy based
  on Feature Flag value
- Remote debug

## [1.0.0] - 28.04.2025 - Author: Mikita Shapel

### Added

- Spring Boot project backbone with pom.xml
- Setup database schema using Liquibase and configure H2 in-memory database
- CRUD for Students and Courses
- Error handler
- Spring Boot Actuator (health, info)
- Swagger for REST API
- Validation for DTOs
- Job that is triggered daily to collect list of Courses that start tomorrow
- Logic for sending notification message via email to enrolled Students
- Logic for coin-based payment
- Unit and integration testing

### Removed 

- OSIV





