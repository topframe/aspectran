# Aspectran - Java application framework

[![Build Status](https://api.travis-ci.com/aspectran/aspectran.svg?branch=master)](https://travis-ci.com/github/aspectran/aspectran)
[![Coverage Status](https://coveralls.io/repos/github/aspectran/aspectran/badge.svg?branch=master)](https://coveralls.io/github/aspectran/aspectran?branch=master)
[![Maven central](https://maven-badges.herokuapp.com/maven-central/com.aspectran/aspectran/badge.svg#v6.6.11)](https://maven-badges.herokuapp.com/maven-central/com.aspectran/aspectran)
[![Sonatype Nexus (Snapshots)](https://img.shields.io/nexus/s/https/oss.sonatype.org/com.aspectran/aspectran.svg)](https://oss.sonatype.org/content/repositories/snapshots/com/aspectran/aspectran/)
[![javadoc](https://javadoc.io/badge2/com.aspectran/aspectran-all/javadoc.svg)](https://javadoc.io/doc/com.aspectran/aspectran-all)
[![License](https://img.shields.io/:license-apache-brightgreen.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)

[![asciicast](https://asciinema.org/a/325210.png)](https://asciinema.org/a/325210)

Aspectran is a framework for developing Java applications that can be used to build simple shell applications and large enterprise web applications.

Aspectran consists of the following key features:

* **Support POJO (*Plain Old Java Object*) programming model**  
  The complexity of the framework and the complexity of the detailed technology should not be passed on to developers.
  Developers can implement the functionality in a simple Java class and return the resulting value in a Java base object.
* **Support Inversion of Control (*IoC*)**  
  The framework completes the overall functionality by directly invoking the unit functionality implemented by the developer while controlling the overall flow.
  The framework manages the creation and lifecycle of objects so that developers can focus on business logic.
* **Support Dependency Injection (*DI*)**  
  The framework links modules that depend on each other at runtime.
  It can maintain low coupling between modules and increase code reusability.
* **Support Aspect-Oriented Programming (*AOP*)**  
  The framework combines additional functionality such as transactions, logging, security, and exception handling within the core logic implemented by the developer.
  Developers will be able to code the core and add-ons separately.
* **Support building RESTful Web Services**  
  Aspectran is designed from the ground up to be suitable for implementing REST APIs, eliminating the need for a separate framework or additional libraries.
  Intuitive API implementation allows you to send and receive messages in a consistent format quickly.
* **Fast development and startup time**  
  Aspectran's intuitive programming model ensures fast development and start-up time.

Applications developed based on Aspectran support the following execution environments on the JVM:

* Consistent shell interface for command line applications
* Built-in high performance web application server (Undertow, Jetty)
* Daemons running as background processes

Aspectran consists of the following major packages:

* **com.aspectran.core**  
  Package that contains implementations of Aspectran's core features and is the basis for other sub-implements
* **com.aspectran.daemon**  
  Provide daemon services for running Aspectran as a background process on Unix-based or Windows operating systems
* **com.aspectran.embed**  
  Package that provide services for embedding Aspectran instances in other Java applications
* **com.aspectran.rss-lettuce**  
  Add-on package for providing session clustering via persistence to Redis using Lettuce as the client
* **com.aspectran.shell**  
  Package to provide a consistent interactive shell interface on the command line
* **com.aspectran.shell-jline**  
  Package to provide interactive shell interface using feature-rich JLine
* **com.aspectran.web**  
  Package to provide the overall functionality for building web applications
* **com.aspectran.jetty**  
  Add-on package for integrating Jetty
* **com.aspectran.mybatis**  
  Add-on package for integrating MyBatis
* **com.aspectran.undertow**  
  Add-on package for integrating Undertow

## Maven dependencies

Basically, to use Aspectran, the following definition including all dependencies can be added to pom.xml.
However, it is recommended to add only the dependency definition suitable for the execution environment of the Aspectran-based application to be built.

```xml
<!-- You can easily include all dependencies, but it is not recommended. -->
<dependency>
  <groupId>com.aspectran</groupId>
  <artifactId>aspectran-all</artifactId>
  <version>6.6.11</version>
</dependency>
```

The dependency definition according to the execution environment of Aspectran-based application is as follows.
In general, only one definition that fits the execution environment needs to be added.

To build a daemon application running in the background
```xml
<dependency>
  <groupId>com.aspectran</groupId>
  <artifactId>aspectran-daemon</artifactId>
  <version>6.6.11</version>
</dependency>
```

To build a command line application
```xml
<dependency>
  <groupId>com.aspectran</groupId>
  <artifactId>aspectran-shell</artifactId>
  <version>6.6.11</version>
</dependency>
```

To build a command line application that uses a feature-rich JLine
```xml
<dependency>
  <groupId>com.aspectran</groupId>
  <artifactId>aspectran-shell-jline</artifactId>
  <version>6.6.11</version>
</dependency>
```

To build a servlet-based web application
```xml
<dependency>
  <groupId>com.aspectran</groupId>
  <artifactId>aspectran-web</artifactId>
  <version>6.6.11</version>
</dependency>
```

To embed Aspectran in a Java application that is not based on Aspectran
```xml
<dependency>
  <groupId>com.aspectran</groupId>
  <artifactId>aspectran-embed</artifactId>
  <version>6.6.11</version>
</dependency>
```

In addition to defining dependencies according to the execution environment as above, 
the dependencies that can be additionally defined are as follows.

To integrate embedded Jetty to build a standalone web application
```xml
<dependency>
  <groupId>com.aspectran</groupId>
  <artifactId>aspectran-with-jetty</artifactId>
  <version>6.6.11</version>
</dependency>
```

To integrate Undertow to build a standalone web application
```xml
<dependency>
  <groupId>com.aspectran</groupId>
  <artifactId>aspectran-with-undertow</artifactId>
  <version>6.6.11</version>
</dependency>
```

To integrate MyBatis with easy access to relational databases
```xml
<dependency>
  <groupId>com.aspectran</groupId>
  <artifactId>aspectran-with-mybatis</artifactId>
  <version>6.6.11</version>
</dependency>
```

To integrate the text template engine FreeMarker
```xml
<dependency>
  <groupId>com.aspectran</groupId>
  <artifactId>aspectran-with-freemarker</artifactId>
  <version>6.6.11</version>
</dependency>
```

To integrate the text template engine Pebble
```xml
<dependency>
  <groupId>com.aspectran</groupId>
  <artifactId>aspectran-with-freemarker</artifactId>
  <version>6.6.11</version>
</dependency>
```

To use Redis Session Store with Lettuce or support session clustering
```xml
<dependency>
  <groupId>com.aspectran</groupId>
  <artifactId>aspectran-rss-lettuce</artifactId>
  <version>6.6.11</version>
</dependency>
```

## Building

Requirements

* Maven 3.3+ (prefer included maven-wrapper)
* Java 8+

Check out and build:

```sh
git clone git://github.com/aspectran/aspectran.git
cd aspectran
./build rebuild
```

## Running the demo

To run the demo, simply use the following command after having build `Aspectran`

```sh
./build demo
```

## Continuous Integration

* [Travis](https://travis-ci.com/github/aspectran/aspectran)

## Links

* [Official Website](https://aspectran.com/)
* [Aspectran Demo Site](https://demo.aspectran.com/)
* [Aspectran Demo Site on GAE](https://demo-gae.aspectran.com/)
* [Skylark (Online Text to Speech Web APP)](https://skylark.aspectran.com/)
* [API Reference](https://javadoc.io/doc/com.aspectran/aspectran-all)

## License

Aspectran is Open Source software released under the [Apache 2.0 license](http://www.apache.org/licenses/LICENSE-2.0).
