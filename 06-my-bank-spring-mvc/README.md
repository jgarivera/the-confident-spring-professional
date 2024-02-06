# my-bank-spring-mvc

An exercise to create a mock bank Spring application.

It uses Spring MVC, Spring Context, an embedded Tomcat server, and Jackson to parse JSON data from HTTP requests.

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
java -Dserver.port=9090 -jar target/06-my-bank-spring-mvc-1.0-SNAPSHOT.jar
```

The Tomcat server port can be specified using the `server.port` command-line parameter.

### Test

Run JUnit tests:

```bash
mvn test
```

While the application is running, you can use [HTTPie CLI](https://httpie.io/cli) to test out the Java servlet:

#### Create transaction

To send data in JSON:

```bash
http POST localhost:9090/transactions reference=Bob amount=9999.99
```

To send data in XML:

```bash
http POST localhost:9090/transactions <<<'<?xml version="1.0" encoding="UTF-8"?><root><amount>1000</amount><reference>bob</reference></root>' Content-Type:application/xml
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
