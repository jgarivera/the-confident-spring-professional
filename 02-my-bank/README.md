# my-bank

An exercise to create a mock bank Java servlet application.

It uses an embedded Tomcat server and Jackson to parse JSON data from HTTP requests.

## Usage

### Build

The project is built using Maven:

```bash
mvn clean install
```

### Run

Run the generated fat JAR after building:

```bash
java -Dserver.port=9090 -jar target/02-my-bank-1.0-SNAPSHOT.jar
```

The Tomcat server port can be specified using the `server.port` command-line parameter.

### Test

Run JUnit tests:

```bash
mvn test
```

While the application is running, you can use [HTTPie CLI](https://httpie.io/cli) to test out the Java servlet:

#### Create transaction

```bash
http -f POST localhost:9090/transactions reference=Bob amount=9999.99
```

#### Find all transactions

```bash
http localhost:9090/transactions
```
