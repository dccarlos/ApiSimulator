# ApiSimulator

How to start the ApiSimulator application
---

1. Run `mvn clean install` to build your application
1. Start application with `java -jar target/ApiSimulator-1.0-SNAPSHOT.jar server config.yml`
1. To check that your application is running enter url `http://localhost:8090`

Health Check
---

To see your applications health enter url `http://localhost:8091/healthcheck`