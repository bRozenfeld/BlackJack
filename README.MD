# Blackjack is a game between the dealer (server) and the other players (client). For each player, the goal is to obtain a total point superior than the dealer, without exceeding 21. Each player can chat with the other players.Réalisé par SBAITY Haitam-ROZENFELD Benjamin


## build
```bash
mvn install
```

## Run server

In Casino module:
```bash
java -jar target/Casino-1.0-SNAPSHOT.jar <rmi_host> <rmi_port> <jms_host> <jms_port>
```

## Run client

In Player module:
```bash
java -jar target/Player-1.0-SNAPSHOT.jar <rmi_host> <rmi_port> <jms_host> <jms_port> <name>
```
