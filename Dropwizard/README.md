These demos have been built and tested using Java 8

Prerequisites
-------------
Before building and running this example you need:

* Maven 3.0.4 or higher
* JDK 1.8

Building the examples
---------------------
The examples are built using Apache Maven. To build all of the examples (and install the
corresponding artifacts in your local Maven repository) enter the following command from
the top-level directory of the examples:

    $ mvn clean install exec:java

- You will now find that the server is listening on `http://localhost:8080`
- you can verify admin endpoints by pointing your browser at `http://localhost:8081`.


Dropwizard Stack
----------------
* Jetty for the servlet container
* Jersey for the REST/JAX-RS implementation
* Jackson for JSON serialization/deserialization HibernateValidator
* Guava - Google’s library to provide utilities and immutable programming.
* Metrics - library that exposes more than enough insight to manage your services in production
* Logback + SLF4J
* JDBI for dealing with databases

When you run your Dropwizard application out of the box, one Jetty server gets created with two handlers: one for your application (8080 by default) and one for the administration interface (8081 by default). Dropwizard does this so you can expose your microservice without exposing administration details over the same port (i.e., can keep 8081 behind a firewall so it’s inaccessible). Things like metrics and health checks get exposed over the admin port, so take care to secure it properly.

Getting Started
---------------
- Run the following command to create dropwizard project.
```console
$ mvn -B archetype:generate  -DarchetypeGroupId=io.dropwizard.archetypes -DarchetypeArtifactId=java-simple -DarchetypeVersion=0.9.2  -DgroupId=com.sahoora.examples.dropwizard -DartifactId=hello-dropwizard -Dversion=1.0 -Dname=HelloDropwizard
```

- Navigate to the directory that the Maven archetype generator cre‐ ated for us in hola-dropwizard and run the following command to build our project:
```console
    $ mvn clean install
```
You should have a successful build!
This uses the Dropwizard archetype java-simple to create our micro‐ service. If you go into the hello-dropwizard directory, you should see this structure:
    ./src/main/java/com/sahoora/examples/dropwizard/api
    ./src/main/java/com/sahoora/examples/dropwizard/cli
    ./src/main/java/com/sahoora/examples/dropwizard/client
    ./src/main/java/com/sahoora/examples/dropwizard/core
    ./src/main/java/com/sahoora/examples/dropwizard/db
    ./src/main/java/com/sahoora/examples/dropwizard/health
    ./src/main/java/com/sahoora/examples/dropwizard/resources

Note that Dropwizard creates a package structure for you that follows their convention:
    api
    - POJOs that define the objects used in your REST resources (some people call these objects domain objects or DTOs).
    cli
    - This is where your Dropwizard commands go (additional com‐ mands you wish to add to startup of your application).
    client
    - Client helper classes go here.
    db
    - Any DB related code or configuration goes here.
    health
    - Microservice-specific health checking that can be exposed at runtime in the admin interface goes here.
    resources
    - Our REST resource classes go here.

We also have the files HelloDropwizardApplication.java and Hola‐ HelloDropwizardConfiguration.java, which is where our configuration and bootstrapping code goes.

```Java
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class HelloDropwizardApplication extends Application<HelloDropwizardConfiguration> {

    public static void main(final String[] args) throws Exception {
        new HelloDropwizardApplication().run(args);
    }

    @Override
    public String getName() {
        return "HelloDropwizard";
    }

    @Override
    public void initialize(final Bootstrap<HelloDropwizardConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final HelloDropwizardConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application
    }
}
```
This class contains our public static void main() method, which doesn’t do too much except call our microservice’s run() method. It also has a getName() method, which is shown at startup. The initialize() and run() methods are the key places where we can bootstrap our application.

- Dropwizard doesn’t have any special Maven plug-ins on its own, hence we’ll be using the maven-shade-plugin to package up our JAR as an uber JAR. This means all of our project’s dependencies will be unpacked (i.e., all dependency JARs unpacked) and combined into a single JAR that our build will create. For that JAR, we use the maven-jar- plugin to make it executable.
- One plug-in we do want to add is the exec-maven-plugin, to make the jar executable.

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>exec-maven-plugin</artifactId>
    <configuration>
        <mainClass>
            com.sahoora.examples.dropwizard.HelloDropwizardApplication
        </mainClass>
        <arguments>
            <argument>server</argument>
        </arguments>
    </configuration>
</plugin>
```

Now we can execute our application from the command line like this:
``` console
$ mvn exec:java
```

Now we should able to see something like as follows:
```console
================================================================================

                              HelloDropwizard

================================================================================


INFO  [2018-07-07 08:09:03,367] io.dropwizard.server.DefaultServerFactory: Registering jersey handler with root path prefix: /
INFO  [2018-07-07 08:09:03,406] io.dropwizard.server.DefaultServerFactory: Registering admin handler with root path prefix: /
INFO  [2018-07-07 08:09:03,488] org.eclipse.jetty.setuid.SetUIDListener: Opened application@21c5ecd8{HTTP/1.1}{0.0.0.0:8080}
INFO  [2018-07-07 08:09:03,489] org.eclipse.jetty.setuid.SetUIDListener: Opened admin@3afecde4{HTTP/1.1}{0.0.0.0:8081}
INFO  [2018-07-07 08:09:03,494] org.eclipse.jetty.server.Server: jetty-9.2.13.v20150730
INFO  [2018-07-07 08:09:04,479] io.dropwizard.jersey.DropwizardResourceConfig: The following paths were found for the configured resources:

    NONE

INFO  [2018-07-07 08:09:04,481] org.eclipse.jetty.server.handler.ContextHandler: Started i.d.j.MutableServletContextHandler@4e9a0b1f{/,null,AVAILABLE}
INFO  [2018-07-07 08:09:04,491] io.dropwizard.setup.AdminEnvironment: tasks =

    POST    /tasks/log-level (io.dropwizard.servlets.tasks.LogConfigurationTask)
    POST    /tasks/gc (io.dropwizard.servlets.tasks.GarbageCollectionTask)

WARN  [2018-07-07 08:09:04,492] io.dropwizard.setup.AdminEnvironment:
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!    THIS APPLICATION HAS NO HEALTHCHECKS. THIS MEANS YOU WILL NEVER KNOW      !
!     IF IT DIES IN PRODUCTION, WHICH MEANS YOU WILL NEVER KNOW IF YOU'RE      !
!    LETTING YOUR USERS DOWN. YOU SHOULD ADD A HEALTHCHECK FOR EACH OF YOUR    !
!         APPLICATION'S DEPENDENCIES WHICH FULLY (BUT LIGHTLY) TESTS IT.       !
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
INFO  [2018-07-07 08:09:04,500] org.eclipse.jetty.server.handler.ContextHandler: Started i.d.j.MutableServletContextHandler@7dcdfe7{/,null,AVAILABLE}
INFO  [2018-07-07 08:09:04,526] org.eclipse.jetty.server.ServerConnector: Started application@21c5ecd8{HTTP/1.1}{0.0.0.0:8080}
INFO  [2018-07-07 08:09:04,529] org.eclipse.jetty.server.ServerConnector: Started admin@3afecde4{HTTP/1.1}{0.0.0.0:8081}
INFO  [2018-07-07 08:09:04,531] org.eclipse.jetty.server.Server: Started @14115ms
```

If you see the application start up, you can try to navigate in your browser to the default location for RESTful endpoints: http://localhost:8080. You probably won’t see too much:
```json
{"code":404,"message":"HTTP 404 Not Found"}
```
If you try going to the admin endpoint http://localhost:8081, you should see a simple page with a few links.

HelloWorld
----------
Now let’s add a REST endpoint.
We want to expose an HTTP/REST endpoint at /api/hello that will return “Hello Dropwizard from X” where X is the IP address where the service is running.
- To do this, navigate to src/main/java/com/sahoora/examples/dropwizard/resources (remember, this is the convention that Dropwizard follows for where to put REST resources) and create a new Java class called HelloRestResource. We’ll add a method named hola() that returns a string along with the IP address of where the service is running as follows:

```Java
import java.net.InetAddress;
import java.net.UnknownHostException;

public class HelloRestResource {
    public String hello() {
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "Unknown";
        }
        return hostAddress;
    }
}
```
Add the HTTP Endpoints
----------------------
To expose this as a REST service, we’re going to leverage good old JAX-RS annotations

- @Path : Tell JAX-RS what the context path for this service should be.
- @GET : Add a GET HTTP service.

```Java
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Path("/api")
public class HelloRestResource {

    @Path("/hello")
    @GET
    public String hello() {
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "Unknown";
        }
        return hostAddress;
    }
}
```
- Now, in our HelloDropwizardApplication class, let’s give the run() method an implementation to add our new REST resource to our microservice.

```java
@Override
public void run(final HelloDropwizardConfiguration configuration,
                final Environment environment) {
    environment.jersey().register(new HelloRestResource());
}
```
Now we should be able to build and run our Dropwizard microservice:
```console
$ mvn clean package exec:java
```
When we go to the endpoint at http://localhost:8080/api/hello we should see the following:
```console
Hello Dropwizard is running on 192.168.0.14
```

Externalize Configuration
-------------------------
Dropwizard has many options for configuring the built-in compo‐ nents (like the servlet engine or data sources for databases) as well as creating entirely new commands that you can run and configure with a configurations file.
- Let’s bind all of the helloapp.* properties to our HelloRestResource class using YAML.(With Dropwizard, we only have the YAML option.)
- So let’s create a file in the root of our project called conf/applica‐ tion.yml (note, you’ll need to create the conf directory if it doesn’t exist). We put our configuration files in the conf folder to help us organize our project’s different configuration files (the naming of the directory is not significant (i.e., it does not have any conven‐ tional meaning to Dropwizard). Let’s add some configuration to our conf/application.yml file:
```yml
# configurations for our sayingFactory
    helloapp:
      saying: Hello Dropwizard running on
```

In this case, we’re setting the property to a specific value.
- What if we wanted to be able to override it based on some environment conditions? We could override it by passing in a Java system variable like this **-Ddw.helloapp.saying=Guten Tag**. Note that the dw.* part of the system property name is significant; it tells Dropwizard to apply the value to one of the configuration settings of our application.
- What if we wanted to override the property based on the value of an OS environment variable? That would look like:
```yml
helloapp:
  saying: ${HELLOAPP_SAYING:-Guten Tag aus}
```
The pattern for the value of the property is to first look at an environment variable if it exists. If the environment variable is unset, then use the default value provided. We also need to tell our application specifically that we want to use environment-variable substitution.
- In the HelloDropwizardApplication class, edit the initialize() method to look like:
```java
@Override
    public void initialize(final Bootstrap<HelloDropwizardConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)));
    }
```
- Now set up configuration. Let’s create a new class called HelloSayingFactory in src/main/java/com/sahoora/examples/dropwizard/resources directory.
```java
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;

public class HelloSayingFactory {

    @NotEmpty
    private String saying;

    @JsonProperty
    public String getSaying() {
        return saying;
    }

    @JsonProperty
    public void setSaying(String saying) {
        this.saying = saying;
    }
}
```
- This is a simple Java Bean with some validation (@NotEmpty, from the hibernate validators library) and Jackson (@JsonProperty) annotations.
This class wraps whatever configurations are under our helloapp configuration in our YAML file; at the moment, we only have “saying.”

- Now add HelloSayingFactory to HelloDropwizardConfiguration class.
```java
public class HelloDropwizardConfiguration extends Configuration {
    private HelloSayingFactory sayingFactory;

    @JsonProperty("helloapp")
    public HelloSayingFactory getSayingFactory() {
        return sayingFactory;
    }

    @JsonProperty("helloapp")
    public void setSayingFactory(HelloSayingFactory sayingFactory) {
        this.sayingFactory = sayingFactory;
    }
}
```
- Lastly, we need to inject the configuration into our HelloRestResource.
```java
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Path("/api")
public class HelloRestResource {

    private String saying;

    public HelloRestResource(final String saying) {
        this.saying = saying;
    }

    @Path("/hello")
    @GET
    public String hello() {
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "Unknown";
        }
        return saying + hostAddress;
    }
}
```

- Then update our HelloDropwizardApplication to inject the configurations to our REST Resource.
```java
@Override
public void run(final HelloDropwizardConfiguration configuration,
                final Environment environment) {
    environment.jersey().register(
            new HelloRestResource(configuration.getSayingFactory().getSaying()));
}
```
There’s a very clear pattern here: bind our Java objects to a YAML configuration file and keep everything very simple and intuitive without leveraging complex frameworks.

- Let’s run our application. To do this, let’s update our pom.xml to pass our new conf/application.yml file as follows:
```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>exec-maven-plugin</artifactId>
    <configuration>
        <mainClass>${mainClass}</mainClass>
        <arguments>
            <argument>server</argument>
            <argument>conf/application.yml</argument>
        </arguments>
    </configuration>
</plugin>
```

From the command line, we can now run:
```console
$ mvn clean package exec:java
```
When you navigate to http://localhost:8080/api/hello, we should see one of the sayings:
```console
Guten Tag aus192.168.0.14
```
If we stop the microservice, and export an environment variable, we should see a new saying:
```console
    $ export HELLOAPP_SAYING='Hello Dropwizard from'
    $ mvn clean package exec:java
```
then the output is as follows
```console
Hello Dropwizard from 192.168.0.14
```

Expose Application Metrics
--------------------------
Metrics are enabled by default on the admin port (8081, by default) but how does Dropwizard know anything specific about our application? Let’s sprinkle a couple declarative annotations on our HolaRestResource.

```java
import com.codahale.metrics.annotation.Timed;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Path("/api")
public class HelloRestResource {

    private String saying;

    public HelloRestResource(final String saying) {
        this.saying = saying;
    }

    @Timed
    @Path("/hello")
    @GET
    public String hello() {
        String hostAddress = null;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostAddress = "Unknown";
        }
        return saying + hostAddress;
    }
}
```
We’ve added the @Timed annotation which tracks how long invocations to our service take. Other annotations for gathering metrics include:

@Metered
The rate at which the service is called.

@ExceptionMetered
The rate at which exceptions are thrown

Build and restart your microservice and try hitting your service at http://localhost:8080/api/hello a couple times. Then if you navigate to http://localhost:8081/metrics?pretty=true and scroll toward the bottom (may be different for yours), you should see the metrics for our service:

```json
"timers" : {
    "com.sahoora.examples.dropwizard.resources.HelloRestResource.hello" : {
      "count" : 4,
      "max" : 0.013907083,
      "mean" : 0.004214093645040806,
      "min" : 1.26618E-4,
      "p50" : 0.001228685,
      "p75" : 0.0026217180000000003,
      "p95" : 0.013907083,
      "p98" : 0.013907083,
      "p99" : 0.013907083,
      "p999" : 0.013907083,
      "stddev" : 0.005346304971237027,
      "m15_rate" : 0.004251326315048105,
      "m1_rate" : 0.03438649024111103,
      "m5_rate" : 0.011671138629069102,
      "mean_rate" : 0.06889648697524442,
      "duration_units" : "seconds",
      "rate_units" : "calls/second"
    }
  }
```
Calling Another Service
-----------------------
