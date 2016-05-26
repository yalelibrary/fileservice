##Log Service

Java web service that logs events to database.

###Build
```
mvn clean install
```

###Test
```
curl http://localhost:8080/logservice/rest/log/test
t curl -X POST --data "param=yale" http://localhost:8080/logservice/rest/log/test
```
