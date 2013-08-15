## About this branch

This is a branch of the VertX 1.3.1 release to provide additional EventBus extensions to aid responsiveness.

The primary change is an extension to Handler for Java (or an additional closure for scripting langs)

https://github.com/meez/vert.x/blob/meez-ebex/vertx-core/src/main/java/org/vertx/java/core/ReplyHandler.java

which provides an additional channel for errors from the EventBus. Callers implementing ReplyHandler (or providing the 
extra closure) will receive failure information if the remote Verticle is unable to process the message.

* NOT_FOUND if there is no-one listening on that address
* REQUEST_TIMEOUT if there was no response received in the specified timeout
* INTERNAL_ERROR if there was exception thrown in the remote handler

Verticles may also send their own custom failure codes (eg for flow-control, validation, authentication etc). For more
information please see the source code or the examples in the unit-tests.

## What is vert.x?

**Vert.x is the framework for the next generation of asynchronous, effortlessly scalable, concurrent applications.**

Vert.x is a framework which takes inspiration from event driven frameworks like node.js, combines it with a distributed event bus and sticks it all on the JVM - a runtime with *real* concurrency and unrivalled performance. Vert.x then exposes the API in JavaScript, CoffeeScript, Ruby, Python, Groovy and Java.

Some of the key highlights include:

* Polyglot. Write your application components in JavaScript, CoffeeScript, Ruby, Python, Groovy or Java. It's up to you. Or mix and match
several programming languages in a single application. (Scala and Clojure support is scheduled too).

* No more worrying about concurrency. Vert.x allows you to write all your code as single threaded,
freeing you from the hassle of multi-threaded programming, yet unlike other asynchronous framework it scales seamlessly over available cores without you having to fork.

* Vert.x has a super simple, asynchronous programming model for writing truly scalable non-blocking applications.

* Vert.x includes a distributed event bus that spans the client and server side so your applications components can communicate incredibly easily. The event bus even penetrates into in-browser JavaScript allowing you to create effortless so-called *real-time* web applications.

* Vert.x provides real power and simplicity, without being simplistic. No more boilerplate or sprawling xml configuration files.

*If you don't want the whole vert.x platform, vert.x can also be used as a lightweight library in your Java or
Groovy applications*

**Please see the [website](http://vertx.io/) for full documentation and information on vert.x**

The Vert.x project has moved here https://github.com/vert-x/vert.x
