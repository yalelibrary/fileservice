## File Service

Crawler/indexing web service for BRBL Tiff Finder Desktop GUI app. Basically, it indexes content on a share, and provides a RESTful service. When the user asks for a file name, say '101', it outputs the path '/usr/local/images/101.tif'.

This service is essential for the desktop GUI to run. The GUI repo is here:

https://github.com/yalelibrary/bulktransfer

### Build

The application is packaged as a stanard Java Maven app.

Change the path to the database (file db or db.mv.db) in path.properties. Also change the path to to the indexing folders in path.properties. You'll find both under resources directory.

```
mvn clean install
```

### Steps

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
Or in the browser:

http://smldr01.library.yale.edu:8080/fileservice/rest/search/10597409
