# vibur-spring-boot-starter

An exercise to create a Spring Boot starter and custom auto-configuration
for [vibur-dbcp](http://www.vibur.org/#introduction) JDBC connection pool.

## Usage

### Build

The project is built using Maven:

```bash
mvn clean install
```

#### Database configuration

The following are used to configure the Vibur datasource in the `application.properties` of a Spring project:

- `vibur.datasource.driver-class-name`
- `vibur.datasource.url` - connection string of the data source
- `vibur.datasource.user`
- `vibur.datasource.password`
