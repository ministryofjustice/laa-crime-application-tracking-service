# laa-crime-application-tracking-service

This is a Java 21 based Spring Boot application hosted on [MOJ Cloud Platform](https://user-guide.cloud-platform.service.justice.gov.uk/documentation/concepts/about-the-cloud-platform.html).

[![MIT license](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

### Decrypting docker-compose.override.yml

The `docker-compose.override.yml` is encrypted using [git-crypt](https://github.com/AGWA/git-crypt).

To run the app locally you need to be able to decrypt this file.

You will first need to create a GPG key. See [Create a GPG Key](https://docs.publishing.service.gov.uk/manual/create-a-gpg-key.html) for details on how to do this with `GPGTools` (GUI) or `gpg` (command line).
You can install either from a terminal or just download the UI version.

```
brew update
brew install gpg
brew install git-crypt
```

Once you have done this, a team member who already has access can add your key by running `git-crypt add-gpg-user USER_ID`\* and creating a pull request to this repo.

Once this has been merged you can decrypt your local copy of the repository by running `git-crypt unlock`.

\*`USER_ID` can be your key ID, a full fingerprint, an email address, or anything else that uniquely identifies a public key to GPG (see "HOW TO SPECIFY A USER ID" in the gpg man page).
The apps should then startup cleanly if you run

### Application Set up

Clone Repository

```sh
git clone git@github.com:ministryofjustice/laa-crime-application-tracking-service.git

cd crime-application-tracking
```

Make sure all tests are passed by running following ‘gradle’ Command

```sh
./gradlew clean test
```

You will need to build the artifacts for the source code, using `gradle`.

```sh
./gradlew clean build
```

```sh
docker-compose build
docker-compose up
```

laa-crime-application-tracking-service application will be running on http://localhost:8490

### Metrics and Health Check endpoints

There are a number of out of the box endpoints provided by Spring Boot for verifying application health and various metrics, some of which are enabled and accessible.

These endpoints include:

- http://localhost:8500/actuator
- http://localhost:8500/actuator/prometheus
- http://localhost:8500/actuator/health
- http://localhost:8500/actuator/info
- http://localhost:8500/actuator/metrics
- http://localhost:8088/api-docs (generated by `springdoc-openapi-starter-webmvc-ui` with URL path defined in `application.yaml` by `springdoc.api-docs.path: /api-docs`)

For a complete list of all out of the box actuator endpoints see [Spring Boot 3.2.3 - Actuator](https://docs.spring.io/spring-boot/docs/3.1.5/reference/html/actuator.html#actuator).

### Swagger and API Docs

- http://localhost:8490/swagger-ui/index.html
- http://localhost:8490/api-docs (generated by `springdoc-openapi-starter-webmvc-ui` with URL path defined in `application.yaml` by `springdoc.api-docs.path: /api-docs`)

### Liquibase documentation

- https://docs.liquibase.com/home.html
