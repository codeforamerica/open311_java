# Open311 Java wrapper (Jreport)


This is a Java language binding (wrapper) to the Open311 GeoReport REST API. Under construction.

## Testing and building


In order to compile and test this project you should have [Maven](http://maven.apache.org/) installed in your system. You can find it in any repository you use (brew, apt...).

```bash
# Compile the project (and download dependencies)
mvn compile

# Execute tests
mvn test

# Execute tests with cobertura analysis
mvn cobertura:cobertura

# Generate the .jar without dependencies
mvn package

# Generate the .jar with dependencies
mvn assembly:single
```

### Output

 + `mvn cobertura:cobertura` will write its output in `target/site/cobertura/`, open the `index.html` file to check it.
 + `mvn package` will output the `target/jreport-{version}.jar` file.
 + `mvn assembly:single` will output the `target/jreport-{version}-jar-with-dependencies.jar` file.

