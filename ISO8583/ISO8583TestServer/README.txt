Product: JavaTestServer :- Testing purpose of ISO8583 connector request and responses.

Pre-requisites:
1. Add following jars as an external jars in java build path.
    jpos-1.9.0.jar
    log4j-1.2.17.jar
    commons-cli-1.3.1.jar
    jdom-1.1.3.jar
    org.osgi.core-4.3.0.jar

2. Update the relative path in log4j.properties for the property "log4j.appender.R.File".

3.Run the main class (mockServer.java).