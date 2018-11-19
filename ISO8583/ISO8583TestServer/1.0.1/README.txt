Product: JavaTestServer :- For the purpose of testing ISO8583 connector request and responses with connector version 1.0.1.

Pre-requisites:
1. Add the following jars as external jars in the java build path.
    jpos-1.9.0.jar
    log4j-1.2.17.jar
    commons-cli-1.3.1.jar
    jdom-1.1.3.jar
    org.osgi.core-4.3.0.jar

2. Update the relative path in log4j.properties for the "log4j.appender.R.File" property.

3.Run the main class (ISO8583MockServer.java).
