##File Service

Crawler/indexing web service.

###Build
```
mvn clean install
```

###Steps

After renaming the .war to fileservice, deploy to Tomcat:

```
curl -X GET http://localhost:8080/fileservice/rest/search
```

Run the indexer once it's deployed:

```
curl -X GET http://localhost:8080/fileservice/rest/indexer/index

```

It requires a path to the database and index. Both are resources directory in paths.properties.

Confirm:

```
curl -X GET http://localhost:8080/fileservice/rest/search/get

```