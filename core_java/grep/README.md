# Introduction
This aim of this project was to create a grep app. This app will search for every line in 
a file and output every line that matches the regex pattern.
To design the app, Java I/O libraries was used for different functionalities. 
Furthermore, Java 8's lambda and stream functions were used to implement the app.
The grep project was deployed via Docker and Maven services was used for dependencies.
The IDE used for this project was IntelliJ.

# Quick Start
1. Maven
```bash
# build package using Maven
mvn clean build package

# run java jar file
java -cp target/grep-1.0-SNAPSHOT.jar {regex} {rootPath} {outFile}
```

2. Docker
```bash
docker run grep regex rootPath outFile
```

# Implementation
## Pseudocode
Pseudocode for process method:
```java
matchedLines = []
for file in listFilesRecursively(rootDir)
    for line in readLines(file)
        if containsPattern(line)
            matchedLines.add(line)
writeToFile(matchedLines)
```
## Performance Issue
The application had JVM memory issues where it was running out of memory for large folders.

# Test
Testing was done manually based on functionality of the app. This was carried out using
a data file as a test case to return the patterns that matched the regex.

# Deployment
A Dockerfile was created to make the application into an image called 'grep' and this was 
pushed to DockerHub so that it could be accessible for public use.

# Improvement
1. Modify Interface, so that each function returns Stream<File> or Stream<String> instead of only two functions.
2. Add other linux grep functionalities
3. Set a limit of memory usage that the application can use and improve memory issues.
