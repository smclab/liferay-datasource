# Liferay Parser

This is a parser to extract contents from Liferay portal. In particular, users, calendars and contents from the document media library are extracted.\
Build image of service, run as Docker container and configure appropriate plugin to call it.\
The container takes via environment variable INGESTION_URL, which must match the url of the Ingestion Api.

## Build

To build images of service you can simply run:

```
docker build -t liferay-parser .
```

If you want to run this parser for test purposes and independently from Openk9 ru:

```
docker run -it liferay-parser:latest
```

If you want to use in Openk9 environment add it to docker-compose file in Openk9 repository.

A pre-built image of liferay parser is present in Smc Docker Hub at following link: https://hub.docker.com/repository/docker/smclab/liferay-parser/general.\
Then add service in main docker-compose file in the following way:

```
liferay-parser:
    image: smclab/liferay-parser:latest
    container_name: liferay-parser
    command: gunicorn -w 1 -t 120 -b 0.0.0.0:80 main:app
    ports:
        - "5005:80"
    environment:
        INGESTION_URL: <insert here url of Ingestion Api>
        DELETE_URL: <insert here url of Ingestion Api>  
```


## Liferay Parser Api

The service exposes APIs through Swagger on root url.
This Rest service exposes three different endpoints:

### Get dml enpoint

This endpoint allows you to execute and start extraction of document media library from Liferay portal instance.

This endpoint takes different arguments in JSON raw body:

- **domain**: domain where is located liferay portal
- **username**: username of specific liferay account
- **password**: password of specific liferay account


Follows an example of Curl call:

```
curl --location --request POST 'http://localhost:5007/execute' \
--header 'Content-Type: application/json' \
--header 'Cookie: COOKIE_SUPPORT=true; GUEST_LANGUAGE_ID=en_US' \
--data-raw '{
    "domain": "http://liferay-portal:8080",
    "username": "test@liferay.com",
    "password": "test"
}'
```

### Get calendar enpoint

This endpoint allows you to execute and start extraction of calendars from Liferay portal instance.

This endpoint takes different arguments in JSON raw body:

- **domain**: domain where is located liferay portal
- **username**: username of specific liferay account
- **password**: password of specific liferay account

Follows an example of Curl call:

```
curl --location --request POST 'http://localhost:5007/execute' \
--header 'Content-Type: application/json' \
--header 'Cookie: COOKIE_SUPPORT=true; GUEST_LANGUAGE_ID=en_US' \
--data-raw '{
    "domain": "http://liferay-portal:8080",
    "username": "test@liferay.com",
    "password": "test"
}'
```

### Get users enpoint

This endpoint allows you to execute and start extraction of users from Liferay portal instance.

This endpoint takes different arguments in JSON raw body:

- **domain**: domain where is located liferay portal
- **username**: username of specific liferay account
- **password**: password of specific liferay account

Follows an example of Curl call:

```
curl --location --request POST 'http://localhost:5007/execute' \
--header 'Content-Type: application/json' \
--header 'Cookie: COOKIE_SUPPORT=true; GUEST_LANGUAGE_ID=en_US' \
--data-raw '{
    "domain": "http://liferay-portal:8080",
    "username": "test@liferay.com",
    "password": "test"
}'
```