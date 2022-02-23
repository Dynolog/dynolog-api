# appointments api

### Project structure

```
.
├── docker-compose.yml
├── HELP.md
├── pom.xml
├── readme.md
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── com
│   │   │   │   └── github
│   │   │   │       └── appointmentsio
│   │   │   │           └── api
│   │   │   │               ├── configurations
│   │   │   │               ├── controllers
│   │   │   │               ├── domain
│   │   │   │               ├── errors
│   │   │   │               │   ├── Error.java
│   │   │   │               │   ├── exception
│   │   │   │               │   └── ValidationHandlers.java
│   │   │   │               ├── Main.java
│   │   │   │               ├── middlewares
│   │   │   │               └── utils
│   │   │   └── db
│   │   │       └── migration
│   │   │           └── database migrations (in java)
│   │   └── resources
│   │       ├── application.properties
│   │       ├── db
│   │       │   └── migration
│   │       │       └── database migrations (in SQL)
│   │       ├── messages.properties
│   │       └── messages_pt_BR.properties
│   └── test
│       ├── java
│       │   └── com
│       │       └── github
│       │           └── appointmentsio
│       │               └── api
│       │                   ├── domain
│       │                   └── MainTests.java
│       └── resources
│           └── application.properties
└── system.properties
```

## Table of Contents

- [Requirements](#requirements)
- [Installation](#installation)
- [Swagger](#swagger)
- [Tests](#tests)
- [Database Migrations](#database-migrations)
- [Environment variables](#environment-variables)

## Requirements

- Postgres: `^13`
- Java: `^17`
- Maven: `^3.8.4`

## Installation

> 🚨 attention make sure the database exists
>> you can up a database using docker with "`docker-compose up -d postgresql`"

```shell
# clone the repository and access the directory.
$ git clone git@github.com:appointments-io/appointments-server.git server && cd server

# download dependencies
$ mvn install -DskipTests

# run the application
$ mvn spring-boot:run

# run the tests
$ mvn test

# to build for production
$ mvn clean package
```

## Swagger
Once the application is up, it is available at: [localhost:8080/documentation](localhost:8080/documentation)

> [development server](https://appointments-io.herokuapp.com)

## Tests

[![Coverage Status](https://coveralls.io/repos/github/appointments-io/appointments-server/badge.svg?branch=main)](https://coveralls.io/github/appointments-io/appointments-server?branch=main)

### Running a specific test
use the parameter `-Dtest=<class>#<method>`

> for example the integration test. create token:
> ```
> $ mvn test -Dtest=SessionControllerTests#should_return_OK_when_password_is_correct
> ```

## Database Migrations
Creating database migration files

- Java based migrations
  ```bash
  mvn migration:generate -Dname=my-migration-name
  ```

- SQL based migrations
  ```bash
  mvn migration:generate -Dname=my-migration-name -Dsql
  ```

---

## Environment variables

| **Descrição**                            | **Parameter**                      | **Default values**          |
|------------------------------------------|------------------------------------|-----------------------------|
| Server port                              | `SERVER_PORT`                      | 8080                        |
| database url                             | `DB_URL`                           | localhost:5432/appointments |
| username (database)                      | `DB_USERNAME`                      | root                        |
| user password (database)                 | `DB_PASSWORD`                      | root                        |
| displays the generated sql in the logger | `DB_SHOW_SQL`                      | false                       |
| set maximum database connections         | `DB_MAX_CONNECTIONS`               | 5                           |
| secret value in token generation         | `TOKEN_SECRET`                     | secret                      |
| token expiration time in hours           | `TOKEN_EXPIRATION_IN_HOURS`        | 24                          |
| refresh token expiry time in days        | `REFRESH_TOKEN_EXPIRATION_IN_DAYS` | 7                           |
| max requests per minute                  | `MAX_REQUESTS_PER_MINUTE`          | 10                          |

> these variables are defined in: [**application.properties**](./src/main/resources/application.properties)
>
> ```shell
> # to change the value of some environment variable at runtime
> # on execution, just pass it as a parameter. (like --SERVER_PORT=80).
> $ java -jar appointments-io-0.0.1-SNAPSHOT.jar --SERVER_PORT=80
> ```
