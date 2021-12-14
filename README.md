This is a forked repo from ProjectEKA/health-information-user and contains changes required for opt out feature and other ABDM capabilities.
## :hospital: Health Information User

> "Health information User" refers to an entity that wishes to consume the
>  services of the Health Data Consent Manager and obtain aggregated health
>  information for providing services to the Customer.

## :muscle: Motivation

> HIU Service acts as requester of patient's health information. When consent is granted, it needs to manage and maintain the
> health information provided  in secure and safe manner andd in compliance with terms of the
> consent granted by the patient.

## Build Status

[![Build](https://github.com/ProjectEKA/health-information-user/workflows/HIU%20master%20build/badge.svg)](https://github.com/ProjectEKA/health-information-user/actions)

## :+1: Code Style

[JAVA Naming Conventions](https://google.github.io/styleguide/javaguide.html)

## :tada: Language/Frameworks

-   [JAVA](https://docs.microsoft.com/en-us/dotnet/csharp/language-reference/)
-   [spring webflux](https://docs.microsoft.com/en-us/aspnet/core/?view=aspnetcore-3.1)
-   [Easy Random](https://github.com/j-easy/easy-random)
-   [Vavr](https://www.vavr.io/vavr-docs/)

## :checkered_flag: Requirements

-   [docker >= 19.03.5](https://www.docker.com/)

## :whale: Running From The Docker Image

Create docker image

```
docker build -t hiu .
```

To run the image

```
docker run -d -p 8002:8080 hiu
```

## :rocket: Running From Source

## Test / Build

To run the tests / build <br />
```
./gradlew clean build
./gradlew clean test
```

## Local setup
This setup is only useful to run the api locally (and may be UI). If you need to setup entire ProjectEka services locally then follow this [developers guide](https://projecteka.github.io/content/developers.html)
### 1) Provision dependencies locally
Start with docker compose `docker-compose-infra-lite.yml` which will setup requried dependencies to run the API locally such as
Postgres, RabitMQ, and orthanc-plugins

Start all containers 
```
docker compose -f docker-compose-infra-lite.yml up -d
```
Stop all containers
```
docker compose -f docker-compose-infra-lite.yml down --volumes
```

Postgres container would be created with following connection string `postgres://postgres:password@localhost:5432/health_information_user` 

### 2) Database
Once you have the local Postgres running, we need to now create the schema.
Clone the db initializer repo [hiu-db-initializer](https://github.com/ProjectEKA/hiu-db-initializer)

please refer the readme of the repo [hiu-db-initializer](https://github.com/ProjectEKA/hiu-db-initializer) for latest and greatest instruction
```
❯ mvn clean install

❯ mvn package

❯ java -Djdbc.url=jdbc:postgresql://localhost:5432/health_information_user -Djdbc.username=postgres -Djdbc.password=password -jar target/hiu-db-initializer-1.0-SNAPSHOT.jar
```

### 3) Setup Admin user
The next step is to create an Admin user for HIU application. 
This is done manually by creating an entry in the “user” table. 

To do this, you need to hash the password for admin first. You can go to this [website](https://bcrypt-generator.com/) or any other bcrypt generator online.

e.g. password: `nimda` <br />
encrypted:  `$2a$12$aVIG0KWbdWZsXzTyyOm2Wu4GXlW0RntDoTuK2xWAvWSs53qez9BMW`

Use the above encrypt valu to create a user in postgres database health_information_user

You can use CLI to log into the DB or any other client like TablePlus or pgAdmin

```
example with the CLI (Assuming your container name is postgres - else do a docker ps to get the container ID or name to use with exec)

❯ docker exec -it postgres /bin/bash

root@6ad86a6bc881:/# psql -U admin health_information_user
psql (14.0 (Debian 14.0-1.pgdg110+1))
Type "help" for help.

insert into "user" (username, password, role, verified) values ('admin', '$2a$12$aVIG0KWbdWZsXzTyyOm2Wu4GXlW0RntDoTuK2xWAvWSs53qez9BMW', 'ADMIN', true);
``` 

### 4) Run the API locally
You are now ready to run the API locally

```
./gradlew bootRun --args='--spring.profiles.active=local'
```

or if you want to run in local environment setup with client secret and frontend
```
CLIENT_SECRET=${CLIENT_SECRET} HFR_AFFINITY_DOMAINS=projecteka.in ./gradlew bootRun --args='--spring.profiles.active=local'
```
or
```
CLIENT_SECRET=${CLIENT_SECRET} HFR_AFFINITY_DOMAINS=projecteka.in ./gradlew bootRunLocal
```
Note, in the above case, remember to set the **gatewayservice.clientId** appropriately in application*.yml

#### Note:
The API requires a valid jwk from the gateway to bootstrap. 
You can either configure your non production environment cert / jwk provider URL in `application-local.yml` (as jwkUrl) or you can also use the local `jwk.json` under `/localCerts/gateway`

For using the local jwk - goto your terminal and expose your CWD over http with python

`python -m SimpleHTTPServer 8000`
or
`python3 -m http.server 8000`

This should start the service `Serving HTTP on :: port 8000 (http://[::]:8000/) ...`
you can access the jwk on
http://localhost:8000/localCerts/gateway/jwk.json (this is already defaulted in application-local.yml). Remember to do this before you start the api

### 5) Verify
Now with the above admin user, you can create HIU users via API. 
To do that, you need to first authenticate the admin user and get a token.

```
curl --location --request POST 'http://localhost:8003/sessions' \
--header 'Content-Type: application/json' \
--header 'Content-Type: text/plain' \
--data-raw '{
    "username" : "admin", 
    "password" : "password"
}
'
``` 

Copy the bearer token from above and then you can make further API calls to create users. 

```
curl --location --request POST 'http://localhost:8003/users' \
--header 'Authorization: Bearer token-from-above-step' \
--header 'Content-Type: application/json' \
--data-raw '{
	"username" : "lakshmi",
	"password" : "password", 
	"role" : "DOCTOR",
	"verified": true
}'

```

Do check in the database, whether the user created in the above step is verified or not, and if not, set as verified.
```
docker exec -it $(docker ps -aqf "name=^postgres$") /bin/bash
psql -U postgres
\c health_information_user
select * from "user" where username = 'lakshmi';
update "user" set verified=true where username='lakshmi';
``` 

## API Contract

Once ran the application, navigate to below URL see the API doc. The service is now running

```alpha
http://localhost:8003/index.html
```
