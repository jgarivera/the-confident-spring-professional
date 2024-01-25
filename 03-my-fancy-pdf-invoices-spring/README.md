# my-fancy-pdf-invoices-spring

A Spring application that is a mock invoice processing service.

It uses Spring Context, an embedded Tomcat server, and Jackson to parse JSON data from HTTP requests.

## Usage

### Build

The project is built using Maven:

```bash
mvn clean install
```

### Run

Run the generated fat JAR after building:

```bash
java -Dspring.profiles.active=dev -jar target/03-my-fancy-pdf-invoices-spring-1.0-SNAPSHOT.jar
```

The Spring active profile can be specified using the `spring.profiles.active` command-line parameter.

When the `dev` profile is active, the invoice CDN URL changes and three dummy invoices are inserted on startup.

### Test

Run JUnit tests:

```bash
mvn test
```

While the application is running, you can use [HTTPie CLI](https://httpie.io/cli) to test out the Java servlet:

### Create invoice

```bash
http -f POST localhost:8080/invoices user_id=Bob amount=1000
```

### Find all invoices

```bash
http localhost:8080/invoices
```
