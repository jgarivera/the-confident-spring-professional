# my-bank-spring-boot

An exercise to create a mock bank Spring Boot application.

This project is generated using [Spring Initialzr](https://start.spring.io/).

It uses an H2 in-memory database and the Web, Validation, and JDBC starters.

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
java -Dserver.port=9090 -jar target/12-my-bank-spring-jdbc-1.0-SNAPSHOT.jar
```

The Tomcat server port can be specified using the `server.port` command-line parameter.

#### Database configuration

The following are used to configure the H2 datasource in the `application.properties`:

- `spring.datasource.url` - connection string of the data source
- `spring.datasource.user`
- `spring.datasource.password`

The `schema.sql` script in the `resources` folder is executed to create the `transactions` table in the database.

### Test

Run JUnit tests:

```bash
mvn test
```

While the application is running, you can use [HTTPie CLI](https://httpie.io/cli) to test out the Java servlet:

#### Create transaction

To send data in JSON:

```bash
http POST localhost:9090/transactions reference=Bob amount=9999.99 receivingUserId=Jess
```

To send data in XML:

```bash
http POST localhost:9090/transactions <<<'<?xml version="1.0" encoding="UTF-8"?><root><amount>1000</amount><reference>bob</reference><receivingUserId>jess</receivingUserId></root>' Content-Type:application/xml
```

#### Find all transactions

To retrieve data in JSON:

```bash
http localhost:9090/transactions -j
```

To retrieve data in XML:

```bash
http localhost:9090/transactions
```

#### Web pages

The following web pages can be viewed from the browser:

- `/` - Mock welcome page
    - Displays the bank slogan.
- `/account/{userId}` - Mock account page for chosen receiving user ID
    - Displays a list of transactions intended for the receiving user ID.
    - Displays a create transaction form with `receivingUserId`, `amount`, and `reference` fields.
    - Both fields are validated and their errors are displayed in the form.
