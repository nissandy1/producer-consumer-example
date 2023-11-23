# Producer consumer example

## goal

Cretae program to demonstrate producer - consumer pattern example with FIFO queue.

## implementation

FIFO queue - Apache ActiveMQ

Embedded DB - H2

Consumer class - com.avitech.example.message.handler.MessageReceiver

Producer class - com.avitech.example.message.handler.MessageSender

## execution
Build jar file by executing
```cmd
mvn clean compile assembly:single
```
Run program by executing
```cmd
java -jar {path-to-project}/target/example-1.0-SNAPSHOT-jar-with-dependencies.jar
```
