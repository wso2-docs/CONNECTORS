Product: JavaTestClient :- Testing purpose of ISO8583 Inbound request and responses with inbound connector version 1.0.0.

STEPS:

1. Add log4j-1.2.17.jar as an external jar in java build path.

2. Update the relative path in log4j.properties for the "log4j.appender.R.File" property.

3. Run the main class (TestClient.java).

4. Use a ISO8583 standard message as input. E.g : 0200B220000100100000000000000002000020134500000050000001115221801234890610000914XYRTUI5269TYUI021ABCDEFGHIJ 1234567890
