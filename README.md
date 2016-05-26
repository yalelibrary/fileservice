##File Service

Crawler/indexing web service.

###Build
```
mvn clean install
```

###Test

After renaming the war file to fileservice:

```
curl -X GET http://localhost:8080/fileservice/rest/search/test22
```
