# my-fancy-pdf-invoices-spring-boot

A Spring Boot application that is a mock invoice processing service.

This project is generated using [Spring Initialzr](https://start.spring.io/).

It uses an H2 in-memory database with [vibur-spring-boot-starter](../13-vibur-spring-boot-starter) custom
auto-configuration and the Web, Validation, and JDBC starters.

For testing, Spring Test, Hamcrest, and JSON Path libraries are used.

## Usage

### Build

The project is built using Maven:

```bash
mvn clean install
```

### Run

Run the generated fat JAR after building:

```bash
java -jar target/11-my-fancy-pdf-invoices-spring-boot-1.0-SNAPSHOT.jar
```

#### Database configuration

The following are used to configure the H2 datasource in the `application.properties`:

##### Using HikariCP

- `spring.datasource.driver-class-name`
- `spring.datasource.url` - connection string of the data source
- `spring.datasource.user`
- `spring.datasource.password`

##### Using Vibur

- `vibur.datasource.driver-class-name`
- `vibur.datasource.url` - connection string of the data source
- `vibur.datasource.user`
- `vibur.datasource.password`

The `schema.sql` script in the `resources` folder is executed to create the `invoices` table in the database.

### Test

Run JUnit tests:

```bash
mvn test
```

While the application is running, you can use [HTTPie CLI](https://httpie.io/cli) to test out the Java servlet:

#### Create invoice

```bash
http -f POST localhost:8080/invoices user_id=Bob amount=1000
```

#### Find all invoices

```bash
http localhost:8080/invoices
```
