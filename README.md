# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Client-Server Communication Sequence Diagram
[Sequence Diagram](https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2GADEaMBUljAASij2SKoWckgQaIEA7gAWSGBiiKikALQAfOSUNFAAXDAA2gAKAPJkACoAujAA9D4GUAA6aADeAETtlMEAtih9pX0wfQA0U7jqydAc45MzUyjDwEgIK1MAvpjCJTAFrOxclOX9g1AjYxNTs33zqotQyw9rfRtbO58HbE43FgpyOonKUCiMUyUAAFJForFKJEAI4+NRgACUh2KohOhVk8iUKnU5XsKDAAFUOrCbndsYTFMo1Kp8UYdKUAGJITgwamURkwHRhOnAUaYRnElknUG4lTlNA+BAIHEiFRsyXM0kgSFyFD8uE3RkM7RS9Rs4ylBQcDh8jqM1VUPGnTUk1SlHUoPUKHxgVKw4C+1LGiWmrWs06W622n1+h1g9W5U6Ai5lCJQpFQSKqJVYFPAmWFI6XGDXDp3SblVZPQN++oQADW6ErU32jsohfgyHM5QATE4nN0y0MxWMYFXHlNa6l6020C3Vgd0BxTF5fP4AtB2OSYAAZCDRJIBNIZLLdvJF4ol6p1JqtAzqBJoIcDcujheT576V5LD99A7FiChT5iWw63O+46tl8LxvMskGLpgIFAUUaooOUCAHjysL7oeqLorE2IJoYLphm6ZIUgatJvqMJpEuGFocjA3K8gagrCjAoriq60pJpeqHlKx2iOs6BKkSy5Set6QYBkGIbceakaMdGMCxsGQlEZ2SHlDhPLZrmiHnAWvEoSUVyviOox-jWQazs28FtoBnbZD2pbmeBln2V8062fOnlLpwq7eH4gReCg6B7gevjMMe6SZJgzkXiZ17SAAoruKX1ClzQtA+qhPi+3mNnZE7-u2yFaa506QHOf4AoZHayvxMAYfYUXYZFvp4RihFysRol0WRMDklSNKFXOtFMm6DHlMxtqCfIQphGN6ChgNPGNU68p2gK6m9RqYmkkYKDcJkqkyXG2gTWaEaFJaMjHRShiqfGe3GRVOFRXpCB5vVIIbaBMAAVef2FAlYBXIDZgBZ4QUbpCtq7tCMAAOKjqyMWnvF57MP9aYVMjGXZfYo4FTZRW+SVQPUA1wG-WZy0U1BdVAjTJngs10LYdCXUEcJib9ZN4lDRSZ0M1d9GKTNPIxrJ2iLTADOrYLCkbezz27ah+1rYdw2o6MqiwuLU2S0x0sK8qKNo3zxHJnTe7c2o+lIZ2gFmcTowAHKjgAjFZUzuygXujL2fsuGHVPHMZYNu6OQcoL7nlPAHcch4nMBhy4-krjD66BNgPhQNg3DwLqmSW6MKSxWeOQ43xpmVLUDREyTvR9AztWzP0ycQZTZVshV-Tt35nd9N3Hm987qtbZJmSwnApcoDzWLW1ryvusLYCi2T41K9d02m7y6sLexivyRGU9oSpsvyCvJHa+vuujrCY8oEbPG3Yxs3lygbFhC-u8SwvgqJUKoNJ3zXhJBeesUDP1jqON+CkP7lGUtAl6ms3p23nl6TIX0fosxBklNMPRIaOSjtjCGWdAq5wCJYY6GFkgwAAFIQB5N-QIOgECgAbFjGubJXYN0pHeFoAdSZ1nJh3VyRdgC0KgHACAGEoB+1HqOAAktIWqfdbb4PptvYqTMR5SJkXIhRSiA5qI0ZPOu7MABWLC0CwmYbpFAaJuq3wFtdcim9pJiwAcbJBB8ZYXWPktXRaBfHrSsVtI+wA3EyAOg-Ck0DYGjDUQgm67Ipa8lQXLdiZjpDhJVpEy+2Sb5gPceGcotieRJLybMQxlBjHQDSfvSk2AtBlxKcAGAEAABmQ1VH5LAVo1M5RHFoFwQZfBLtgYUM0acaOpZIbLiocFAIXhpFdi9LAYA2Ai6EHiIkSumMwZ8JmZUVK6VMrZWMJo2m2jJmplOU1EA3A8CG1iWfCSryoAGgNs0k20gHpl2ABbeaMSylxPvl8rZqk-kFPSXdQFJ1DAgoQFfIJ4LXrlMGi8rZ0C4Vn33kix65s0WdNvsM4E5REBbImZPXGszSHzPIYsyhQA)