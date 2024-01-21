# my-fancy-pdf-invoices

A Java servlet application that is a mock invoice processing service.

It uses an embedded Tomcat server and Jackson to parse JSON data from HTTP requests.

## Usage

### Build

The project is built using Maven:

```bash
mvn clean install
```

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
