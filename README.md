# Diff Service
Is a powerful tool that provides rest endpoints to submit two Json base64 encoded data and then compare its contents.


## Working from code
### Requirements:
* Java JDK 1.8 - JAVA_HOME variable is required for gradle wrapper
* To work with the source code you will need Lombok plugin. You can follow the instructions bellow to install the plugin on your favorite ide
  * IntelliJ: https://projectlombok.org/setup/intellij
  * Eclipse: https://projectlombok.org/setup/eclipse


### Get the code
```$xslt
git clone https://github.com/osnircunha/diff-service.git
cd diff-service
```

### Build
Unix
```$xslt
./gradlew build
```
Windows
```$xslt
gradlew.bat build
```

### Run
Unix
```$xslt
./gradlew bootRun 
```

Windows
```$xslt
gradlew.bat bootRun 
```

## Usage
### Available endpoints:
* PUT http://localhost:8080/v1/diff/{id}/left
* PUT http://localhost:8080/v1/diff/{id}/right
* GET http://localhost:8080/v1/diff/{id}

### Submit files:
#### PUT
```$xslt
curl -X PUT -d <base64 encoded data> -H 'Content-Type: application/octet-stream' http://localhost:8080/v1/diff/{id}/{left|right}
```
#### Responses:

* OK - 200 : File submitted successfully
* BAD_REQUEST - 400 : Empty data
* INTERNAL_SERVER_ERROR - 500 : Invalid path parameters



### Compare files:
#### GET
```$xslt
curl -X GET http://localhost:8080/v1/diff/{id}
```
#### Resonses:
* INTERNAL_SERVER_ERROR - 500 : Missing data for id case left, right or both files are missing.
* OK - 200 : Describing when files are equals, has different bytes size, has different line numbers or how many differences between them.


## Improvements
* Add a path parameter *ignoreCase* to the get diff endpoint to allow compare ignoring case
* Save diff result to the database to avoid calculate diff every request
* Create a endpoint to allow delete a file
