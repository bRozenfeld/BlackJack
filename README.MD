# build

mvn install

## Run server

In Casino module:
java -jar target/Casino-1.0-SNAPSHOT.jar <rmi_host> <rmi_port> <jms_host> <jms_port>

## Run client

In Player module:
java -jar target/Player-1.0-SNAPSHOT.jar <rmi_host> <rmi_port> <jms_host> <jms_port> <name>