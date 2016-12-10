# Installation

First, make sure to install **Maven2**. Then, compile the project and add it to your local repository:

```bash
mvn package install
```

## Add as Dependency

To add this project as Maven dependency, insert the following dependency in your `pom.xml` file:

```xmlorg.mockito
<dependency>
    <groupId>edu.nyu.tandon</groupId>
    <artifactId>index-utils</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## Run as Standalone

To run any class, first retrieve the classpath to all the dependencies in your local Maven repository:

```bash
mvn dependency:build-classpath [-Dmdep.outputFile=<your-file>]
```

Then, simply run the desired class:

```bash
java -cp <dependencies-classpath> qualified.name.to.YourClass
```
