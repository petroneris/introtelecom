# introtelecom
Spring Boot REST API app provides an introduction to the mobile phone service management

<img src="docs/mobileNet.png" width="400">
<br/>

### THE SPRING BOOT REST API APPLICATION 'introtelecom'
  
Mobile phone services are an area of modern technology that requires the most complex software solutions nowadays. This demo application simplifies many details related to mobile services, to show and explain the mobile service basics - how to:
- register the phones, grant the monthly package frame to each phone
- register customers (owners of physical phones) and users (owners of software accounts)
- provide add-ons (additional services during the month) to customers
- create a record of mobile service (Service Detail Record -SDR, its type, duration and amount) 
- provide the user with details of the current information (what amount of service is left until the end of the month) 
- generate monthly bill facts for the previous month  

“introtelecom” application demonstrates a usage of the following technologies:
- Spring Boot - packaging and deployment for REST (REpresentational State Transfer) API 
- Spring Data JPA – ORM and transaction-driven data handling
- Hibernate ORM – used together with Spring Data JPA
- Spring Security framework – for logging and access activities
- Spring Boot Validation (with Hibernate Validator) – for input and database data validation
- PostgreSQL – RDBMS data layer
- Swagger - UI for REST API and documentation
- JWT - JSON Web Token for authentication
- MapStruct - to map between different object models (e.g. entities and DTOs)
- Lombok - java library that eliminates boilerplate code
- Slf4j-with-Logback library for logging

A standard Java servlet container (Tomcat) is used as a server platform.

Maven is used as the build system.

The code is Java8 – compliant.

A short description of coding technologies used in application design, application rules, configuration and design details are presented in [documentation](docs/introtelecom.pdf).

Prior to starting the application you need to create and populate PostgreSQL database “intro_telecom” as described in the code and documentation.

Once you have started the app, you can reach it on localhost like so:
`http://localhost:8080/swagger-ui/index.html`

Several types of tests are used in this app:
- @DataJpaTest "sliced" integration tests are used for repository testing
- JUnit testing with Mockito library is used for validation services
- @SpringBootTest with webEnvironment set to NONE are used for services
- @WebMvcTest "sliced" tests are used for controller layer
- @SpringBootTest with WebEnvironment.RANDOM_PORT is used for testing the whole application
Some of the test types use the real test database “test_intro_telecom”, described in the code and documentation.

