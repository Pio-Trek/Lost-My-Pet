## DEMO
Application demo has been deployed at the following link:
http://sundaydevblog.com/lostmypet

**Example user accounts:** smccrow7@parallels.com, cmulvany4@answers.com, asiggers3@de.vu, aellyatt8@theguardian.com, lsullly5@tamu.edu <br />
**Example administrator accounts:** admin@email.com, admintom@gmail.com <br />
**Password (all accounts):** 123qwe

## ABOUT
Lost My Pet is (yes, another pet application!) my graded unit project that allows a user to report missing or found pet in the area where they live, create accounts, sending notifications and messages to other members, register new account (email verification token sending), generate Excel/PDF reports and many more.
The project uses Java 8 with Spring Rest for the back-end and Spring Web (MVC), Security, Thymeleaf for front-end. During the analysis phase, I used OOA/OOD methodology and created full documentation of the web client and backend service.
For the full documentation please check **DOCUMENTATION** folder.

## API DOCUMENTATION
API documentation with playground is available at the following link:
[RESTful API Documentation](http://159.65.24.18:9000/jsondoc-ui.html?url=http://159.65.24.18:9000/jsondoc).

## TECHNOLOGY STACK
The whole project was written using Java 1.8 and Spring Boot Framework 2.0.0 version.
The backend (RESTful API) was created using:
- Spring Data JPA
- Spring Email
- Hibernate ORM
- H2 Java SQL RDBMS
- Google Guava (helper library for java.lang.String API)
- JSONDoc (document and expose REST API)
- Testing tools:
    - JUnit 4
    - Mockito
    - Hamcrest

And the web client with:
- Spring MVC
- Spring Security
- Thymeleaf
- Bootstrap 4
- Apache HttpComponents (creating and maintaining HTTP protocols)
- Apache Common Langs (helper utilities for the java.lang API)
- Apache POI (API for Microsoft Documents to generate Excel reports)
- iText PDF (PDF generation and manipulation tool)
- Ocp PrettyTime (java.util.Date API helper library)


### Browse missing pet announcements
![Sample screen](https://github.com/Pio-Trek/Lost-My-Pet/blob/master/art/app01.jpg)


### Announcement details
![Sample screen](https://github.com/Pio-Trek/Lost-My-Pet/blob/master/art/app02.jpg)


### Generate Excel/PDF report
![Sample screen](https://github.com/Pio-Trek/Lost-My-Pet/blob/master/art/app03.jpg)


### Report found pet
![Sample screen](https://github.com/Pio-Trek/Lost-My-Pet/blob/master/art/app04.jpg)


### User account dashboard
![Sample screen](https://github.com/Pio-Trek/Lost-My-Pet/blob/master/art/app05.jpg)

