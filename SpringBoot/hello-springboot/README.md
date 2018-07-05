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

    $ mvn clean package spring-boot:run

You will now find that the server is listening on `http://localhost:8080`
- you can verify this quickly by pointing your browser at `http://localhost:8080/api/hello`.


Externalize Configuration
-------------------------
Spring Boot makes it easy to use external property sources like properties files, command-line arguments, the OS environment, or Java System properties. We can even bind entire “classes” of proper‐ ties to objects in our Spring context.
For example, if we want to bind all helloapp.* properties to our HelloSpringBootController, we can add @ConfigurationProperties(prefix="helloapp"), and Spring Boot will automatically try to bind helloapp.foo and helloapp.bar
to Java Bean properties in the HelloSpringBootController class.

Let’s stop our application from running before (if we haven’t) and restart it:
    $ mvn clean package spring-boot:run
- Now if we navigate to http://localhost:8080/api/hello, we should see the following message on browser
Have a good day. 192.168.0.14

Expose Application Metrics and Information
------------------------------------------
If we want to put this microservice into production, how will we monitor it? How can we get any insight about how things are running? Often our microservices are black boxes unless we explicitly think through how we want to expose metrics to the outside world. Spring Boot comes with a prepackaged starter called actuator that makes doing this a breeze.

Add the following Maven dependency within the <dependencies>...</dependen cies> section:
```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```
Now restart your microservice by stopping it and running:
```console
$ mvn clean package spring-boot:run
```

Just by adding the actuator dependency, our application now has a lot of information exposed that would be very handy for debugging.
As of spring boot version 2.0.1 using below property to enable actuator endpoints.

management.endpoints.web.exposure.include=<comma separated endpoints you wish to expose>
You can use * wildcard to expose all actuator endpoints over the web if security isn't your concern.

- http://localhost:8080/actuator/beans
- http://localhost:8080/actuator/env
- http://localhost:8080/actuator/health
- http://localhost:8080/actuator/metrics
- http://localhost:8080/actuator/trace
- http://localhost:8080/actuator/mappings

Calling Another Service
-----------------------
We are using Spring’s REST client functionality to invloke backend Service.

The backend service contains a very simple HTTP servlet that can be invoked with a GET request and query parameters.

```java
public class BackendHttpServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        String greeting = req.getParameter("greeting");
        ResponseDTO response = new ResponseDTO();
        response.setGreeting(greeting + " from cluster Backend");
        response.setTime(System.currentTimeMillis());
        response.setIp(getIp());
        PrintWriter out = resp.getWriter();
        mapper.writerWithDefaultPrettyPrinter().writeValue(out, response);
    }

    private String getIp() {
        String hostname = null;
        try {
            hostname = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            hostname = "unknown";
        }
        return hostname;
    }
}
```
To start up the backend service on port 8080, navigate to the back end directory and run the following:
```console
$ mvn clean install jetty:run
```

This service is exposed at /api/backend and takes a query parameter greeting. For example, when we call this service with this path /api/ backend?greeting=Hello, then the backend service will respond with a JSON object like this (can also visit this URL with your browser):

```console
$ curl -X GET http://localhost:8080/api/backend?greeting=Hello
```

We get something like this:
```json
{
  "greeting" : "Hello from cluster Backend",
  "time" : 1530771824785,
  "ip" : "192.168.0.14"
}
```
Integrate Spring-boot with backend service
------------------------------------------
We will create a new HTTP endpoint, /api/greeting, in our Spring Boot and use Spring to call this backend!

Now let’s build the microservice and verify that we can call this new Greeting endpoint and that it properly calls the backend. First, let’s start the backend if it’s not already running. Navigate to the backend directory of the source code that comes with this application and run it:
```console
    $ mvn clean install jetty:run
```
Next let’s build and run our Spring Boot microservice. Let’s also configure this service to run on a different port than it’s default port (8080) so that it doesn’t collide with the backend service which is already running on port 8080.
```console
    $ mvn clean install spring-boot:run -Dserver.port=9090
```
Later on in the book we can see how running these microservices in their own Linux container removes the restriction of port swizzling at runtime. Now, let’s navigate our browser to http://localhost:9090/api/greeting to see if our microservice properly calls the back‐end 
