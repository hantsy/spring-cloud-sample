# Spring Microservice Sample

This demo application added [Spring Cloud components](https://github.com/spring-cloud), esp [Spring Cloud Netflix](https://github.com/spring-cloud/spring-cloud-netflix) into [the original version](https://github.com/hantsy/spring-microservice-sample).

* **auth-service** based on Spring Cloud Seucrity and Spring Seucrity OAuth2, provides OAuth2 token based authentication.
* **user-service** is responsible for user management.
* **post-service** is the APIs for a simple CMS, including posts and comments.
* **gateway** uses Netflix Zuul to implement a simple reverse proxy.
* **eureka-server** provdies Service Discovery service, it uses Netflix Eureka Server as the service registry. The **auth-service**, **user-service**, **post-service** and **gateway** will be registered as service discovery clients.

curl -X POST http://localhost:9999/uaa/oauth/token?grant_type=password&username=user&password=test123&client_id=acme&client_secret=acmesecret -H 'Accept: application/json'

Authentication code.

http://localhost:9999/uaa/oauth/authorize?response_type=code&
  client_id=acme&redirect_uri=http://localhost&scope=openid