# Spring Microservice Sample

This demo application added [Spring Cloud components](https://github.com/spring-cloud), esp [Spring Cloud Netflix](https://github.com/spring-cloud/spring-cloud-netflix) into [the original version](https://github.com/hantsy/spring-microservice-sample).

* **auth-service** based on Spring Cloud Seucrity and Spring Seucrity oAuth2, provides oAuth2 token based authentication.
* **user-service** is responsible for user management.
* **post-service** is the APIs for a simple CMS, including posts and comments.
* **gateway** uses Zuul to implement reverse proxy.
* **eureka-server** uses Netflix Eureka as a service registry.