# pets-database-simple

* Main Repo: https://github.com/bibekaryal86/pets-database

This is a simple app which provides the logic for database CRUD actions. This app is a scaled down version
of `pets-database` app found here: https://github.com/bibekaryal86/pets-database. The other app uses Spring
Boot with MongoTemplate framework to do the exact same function as this app - `pets-database-simple`. However,
this `simple` app does not use any kind of Spring or database frameworks. The web application framework is provided by
Jetty server with Java Servlets providing the endpoints. Database interactions are done using MongoDb Driver.

Because of absence of any frameworks, the footprint of this app is very grounded (~6 MB jar archive and ~100 MB runtime
JVM memory) as opposed to when using Spring Boot (~45 MB archive and ~350 MB memory). And, as a result, the app can be
deployed and continuously run 24/7 on Google Cloud Platform App Engine's free tier.

To run the app, we need to supply the following environment variables:

* Port
    * This is optional, and if it is not provided port defaults to 8080
* MongoDb connection details
    * MONGODB_ACC_NAME: database name (value is different from `pets-database`)
    * MONGODB_USR_NAME: database username
    * MONGODB_USR_PWD: database password
* Authentication header for simple app security
    * BASIC_AUTH_USR: auth username
    * BASIC_AUTH_PWD: auth password

The app has been deployed to GCP:

* https://pets-database.appspot.com/pets-database/tests/ping
