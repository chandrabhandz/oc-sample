# Open Channel API Communication Sample (Java/Spring Boot)

### Minimum Requirements:
1. JDK 1.8
2. Maven

### Configuration
* Application is configuration with `yml` file locate at `src/main/resources/application.yml.
* `openchannel` properties are configurable, and once application is loaded; those will be available in `io.openchannel.sample.config.OpenChannelProperties` class which is a spring bean.
* Other properties are Spring/Server realted configuration which do not require any change as of now.
### Run:
1. Open command line in root folder of source code.
2. `mvn clean package -Dmaven.test.skip=true` - This will generate a JAR named `<App_Root>/target/sample-0.0.1-SNAPSHOT.jar`.
3. `java -jar target/sample-0.0.1-SNAPSHOT.jar`. - will bootstrap spring boot application with embedded undertow container.