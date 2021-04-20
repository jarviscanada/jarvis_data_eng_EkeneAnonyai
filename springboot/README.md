Table of contents
* [Introduction](#Introduction)
* [Quick Start](#QuickStart)
* [Implementation](#Implementation)
* [Test](#Test)
* [Deployment](#Deployment)
* [Improvements](#Improvements)

# Introduction
This application is a Proof of Concept trading system for stock traders.
This project aims to replace the Jarvis Trading system, an app that facilitates online stock trading,
with a more scalable and maintainable implementation that leverages a three-tier microservice
architecture in order to switch from the monolithic architecture the legacy trading system was 
based on. The system allows users to manage client profiles and accounts, monitor portfolio performance, and trade securities. The system
was implemented in Java8 using Springboot for dependency management, JUnit, and Mockito for coding and integration/unit testing, 
Docker and Maven for packaging and deployment, as well as, connecting to other microservices. REST API endpoints from IEX Cloud 
allowed for the system to handle HTTP requests/send out HTTP responses accordingly, managing all the business and core 
logic of the application. 

Some other technologies used in this app were:
- Apache Tomcat for the webapplet that maps REST requests to Java controller classes
- PSQL for storing quote and trader account/profile/portfolio information
- JDBC for interacting with PSQL
- Swagger to implement the web service that connects our API to a front end
- Model View Controller design pattern

# Quick Start

## Prerequistes:

- Docker 17.05 or higher
- IEX access token

## Docker setup

```bash
#Download images
docker pull ekeneanonyai/trading-psql
docker pull ekeneanonyai/trading-app

#Setup network
docker network create --driver bridge trading-net

#Run database
docker run -d --rm --name trading-psql-local \
-e POSTGRES_PASSWORD=password \
-e POSTGRES_DB=jrvstrading \
-e POSTGRES_USER=postgres \
--network trading-net \
-p 5432:5432 ekeneanonyai/trading-psql

IEX_PUB_TOKEN="YOUR_TOKEN"

#Run app
docker run --name ekeneanonyai-trading-app-local \
-e "PSQL_URL=jdbc:postgresql://trading-psql-dev:5432/jrvstrading" \
-e "PSQL_USER=postgres" \
-e "PSQL_PASSWORD=password" \
-e "IEX_PUB_TOKEN=${IEX_PUB_TOKEN}" \
--network trading-net \
-p 8080:8080 ekeneanonyai/trading-app
```
* **Navigate to http://localhost:8080/swagger-ui.html in your browser**
  ![Swagger](assets/Swagger_UI.png)
    * **Stopping the Containers**

# Implemenation
## Architecture
- Draw a component diagram that contains controllers, services, DAOs, SQL, IEX Cloud, WebServlet/Tomcat, HTTP client, and SpringBoot. (you must create your own diagram)
- briefly explain the following components and services (3-5 sentences for each)
    - Controller layer (e.g. handles user requests....)
    - Service layer
    - DAO layer
    - SpringBoot: webservlet/TomCat and IoC
    - PSQL and IEX

## REST API Usage
### Swagger
What's swagger (1-2 sentences, you can copy from swagger docs). Why are we using it or who will benefit from it?
### Quote Controller
- High-level description for this controller. Where is market data coming from (IEX) and how did you cache the quote data (PSQL). Briefly talk about data from within your app
- briefly explain each endpoint
  e.g.
    - GET `/quote/dailyList`: list all securities that are available to trading in this trading system blah..blah..
### Trader Controller
- High-level description for trader controller (e.g. it can manage trader and account information. it can deposit and withdraw fund from a given account)
- briefly explain each endpoint
### Order Controller
- High-level description for this controller.
- briefly explain each endpoint
### App controller
- briefly explain each endpoint
### Optional(Dashboard controller)
- High-level description for this controller.
- briefly explain each endpoint

# Test
The application was tested using JUnit4 through integration tests at the DAO and Service layers, and unit tests where possible 
with Mockito. The rest of the application was tested manually through API interaction with Swagger UI and Postman. 
The code coverage for the integration and unit tests was at least 70% for all files tested.

# Deployment
![Docker](./assets/docker.jpg)
- trading-psql: This image pulls the postgres image and contains the sql statements which are run upon initialization of the database.
- trading-app: This image first builds the application by pulling a maven image to create a jar file, which will be executed when the 
trading-app is run. The image then pulls an open-jdk:8 image. From this image, an entrypoint is set up causing the jar file to be executed
  when a container of trading-app is run.

# Improvements
- Increase the test coverage to cover layers that are not being tested.
- Implement better UI than Swagger homepage to allow for easy usage.
- Allow for multiple orders to be processed in one API request