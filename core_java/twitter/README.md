# Introduction
The Twitter CLI Application is an MVC project that allows users to post, show or delete tweets on twitter 
via Twitter REST API. The MVC architecture is used to implement the sending of requests to the Twitter REST 
API and responses are retrieved via the HttpClient. Tweets  are retrieved in JSON format and are then converted
into Tweet Objects. The project was packaged by Maven dependencies and was deployed to Docker, while 
exploring the different Spring Frameworks. Testing was done using Junit, Integration and Mockito testing.

# Quick Start
To package the app using Maven:

```bash
mvn clean package spring-boot:repackage -DskipTests
```

To run the app on Docker:

```bash
docker run -rm /
-e consumerKey="consumerKey goes here" /
-e consumerSecret="consumerSecret goes here" /
-e accessToken="accessToken goes here" /
-e tokenSecret="tokenSecret goes here" /
ekeneanonyai/twitter "post|show|delete" [options]"
```

# Design
## UML diagram
## explain each component(app/main, controller, service, DAO) (30-50 words each)
## Models
The Tweet model is the DTO used throughout the entire App. It contains returns data from the Twitter API
response that consists of the other models in the app and other information, such as: idStr, id, 
created_at, text in the tweet, Coordinates, Entity, RetweetCount, favoriteCount, retweeted and favorited. 

## Spring
- How you managed the dependencies using Spring?

# Test
How did you test you app using Junit and mockito?

## Deployment
Firstly, we used maven clean to get rid of any target packages already existing and then used maven
to package the application into a jar file. Next, we created a Dockerfile and built a 'twitter' image of the
Twitter app. After running the docker image, it was pushed to DockerHub.


# Improvements
- Allow for multiple tweets to be shown, like the 'delete' option.
- Allow users to not show their location if they do not want to. 
- Allow tweets to be retrieved using keywords and not just IDs.
