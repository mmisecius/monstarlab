# Getting Started
To build application run:

    gradlew clean build docker

this assembles the application and creates a docker image. To start the whole 'system' use attached 
_docker-compose_ file by executing of the following command

    docker-compose up

it's gonna start postgesql database, rabbitMQ and application itself. Port mapping could be found in compose file


### Playing with application 

Application listens on **8080** port and has exposed several endpoints

* For obtaining the bank customer invoke

http://localhost:8080/mis055/api/v1.0/customer/123e4567-e89b-12d3-a456-426614174000

where the 
uuid '123e4567-e89b-12d3-a456-426614174000' is 'customer id' - which is created by **Flyway** and could be found in 

    src/main/resources/db folder

The mentioned command returns the json payload with the two associated accounts

* To list account detail execute

http://localhost:8080/mis055/api/v1.0/customer/bankaccount/bcd234

where bcd234 is accountNumber - there are two accounts - second one is 'abc123'

* to list transactions belong to associated account serves the endpoint

http://localhost:8080/mis055/api/v1.0/customer/bankaccount/abc123/transactions

where 'abc123' is accountNumber

This endpoint supports pagination - so you can sort it and obtain the required slices of data

like 

http://localhost:8080/mis055/api/v1.0/customer/bankaccount/abc123/transactions?page=0&size=1

see springboot pagination

* Now the sending money

from terminal run the following


    curl --location --request PUT 'http://localhost:8080/mis055/api/v1.0/customer/abc123/transactions/send' \
    --header 'Content-Type: application/json' \
    --data-raw '{
    "accountNumber":"bcd234",
    "amount":50,
    "currency": "EUR",
    "variableSymbol": "test234"
    }'

this sends the money from the abc123 --> bcd234 and creates the appropriated transactions

### Most interesting parts of code

* For integration tests I used testcontainers to run the real db
* in tests is used mockk and assertjk which are really great for kotlin
* for mapping is used mapstruct
* database access is served by JPA
* in non integration tests but with context is used H2 database
* there is also jaccoco for computation of code coverage - see the result in the file *build/sites/index.html*
* there is openApi/swagger documentation - reachable under 
  * http://localhost:8080/mis055/api
  * http://localhost:8080/mis055/api/swagger.html
* For locking of the transactions processing is used RabbitMQ broker

### Epilogue

I wanted to implement security and use spring cloud as well - but was not enough time ..