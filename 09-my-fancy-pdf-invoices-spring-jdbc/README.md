# my-fancy-pdf-invoices-spring-jdbc

A Spring application that is a mock invoice processing service.

It uses Spring JDBC, Thymeleaf, Spring MVC, Spring Context, an embedded Tomcat server, H2 database, and Jackson to parse
JSON data from HTTP requests.

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
java -Dspring.profiles.active=dev -jar target/09-my-fancy-pdf-invoices-spring-jdbc-1.0-SNAPSHOT.jar
```

The Spring active profile can be specified using the `spring.profiles.active` command-line parameter.

When the `dev` profile is active, the invoice CDN URL changes and three dummy invoices are inserted on startup.

#### Database configuration

The following are used to configure the H2 datasource in the `application.properties`:

- `datasource.url` - connection string of the data source
- `datasource.user`
- `datasource.password`

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

#### Web pages

The following web pages can be viewed from the browser:

- `/` - Mock home page
    - Displays the username on the page using the `username` query parameter.
    - Displays the current date.
    - Displays a message when the username starts with a lower-case letter `z`.
- `/login` - Mock login page
    - Displays a login form with `username` and `password` fields.
    - One is able to "log in" successfully (simply redirects to home page) when both fields are the same value.
    - Both fields are validated and their errors are displayed in the form.
