# my-bank-spring-jdbc

An exercise to create a mock bank Spring application.

It uses Spring JDBC, Thymeleaf, Spring MVC, Spring Context, an embedded Tomcat server, H2 database, and Jackson (with
JSR310 support) to parse JSON data from HTTP requests.

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
java -Dserver.port=9090 -jar target/10-my-bank-spring-jdbc-1.0-SNAPSHOT.jar
```

The Tomcat server port can be specified using the `server.port` command-line parameter.

#### Database configuration

The following are used to configure the H2 datasource in the `application.properties`:

- `datasource.url` - connection string of the data source
- `datasource.user`
- `datasource.password`

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
