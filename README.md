# ♟️ CHESS GAME by Group 3!

Members: Hyejin, Aylin, Vasja, Jonas and Theo

This repository contains a student project created for an ongoing lecture on object-oriented programming
with Java at HWR Berlin (summer term 2024).

> :warning: This code is for educational purposes only. Do not rely on it!

## Prerequisites

Installed:

1. IDE of your choice (e.g. IntelliJ IDEA)
2. JDK of choice installed (e.g. through IntelliJ IDEA)
3. Maven installed (e.g. through IntelliJ IDEA)
4. Git installed

## Local Development

This project uses [Apache Maven](https://maven.apache.org/) as build tool.

To build from your shell (without an additional local installation of Maven), ensure that `./mvnw`
is executable:

```
chmod +x ./mvnw
```

I recommend not to dive into details about Maven at the beginning.
Instead, you can use [just][just] to build the project.
It reads the repositories `justfile` which maps simplified commands to corresponding sensible Maven
calls.

With _just_ installed, you can simply run this command to perform a build of this project and run
all of its tests:

```
just build
```

## Abstract 📖

Chess is a Java-based command line chess game developed as a collaborative project as part of the object-oriented
programming course. The game offers a traditional chess experience accessible through the command line interface,
making it lightweight and easy to play on any system with Java installed. Players can engage in classic chess matches,
pitting their strategic prowess against each other. The game includes essential features such as move validation, the
display of a figure's movement options, checkmate detection, a rank system and game storage through csv files. Through
this project, students delve into Java programming principles, object-oriented design, algorithmic thinking, and
software development methodologies, culminating in a functional and enjoyable implementation of the timeless game of
chess in the command line environment. We as a group encountered problems in the development process such as finding
effective collaboration methods in development, managing the branches and commits in the versioning system,
reaching PrintStreams to test them and realizing loose coupling in our library.

## Commands overview

If `chess` does not work in your terminal, try `./chess` or `.\chess` instead.

| Command         | Description      | Example       |
|-----------------|------------------|---------------|
| `<ID>`          | Game Id (int)    | 123, 7        |
| `<FROM>` `<TO>` | Cell coordinates | a4, c7        |
| `<TYPE>`        | Type of figure   | queen, knight |

| Command                        | Description                                              |
|--------------------------------|----------------------------------------------------------|
| `chess` `chess help`           | Show the list of supported commands                      |
| `chess create <ID>`            | Create a new, fresh game (stored in game_\<ID>.csv)      |
| `chess create tournament <ID>` | Create a new tournament (stored in tournament_\<ID>.csv) |

### In-Game Commands `chess on <ID> [...]`

| Command                               | Description                                    |
|---------------------------------------|------------------------------------------------|
| `chess on <ID> move <FROM> <TO>`      | Move the figure on FROM to the cell TO         |
| `chess on <ID> promote <FROM> <TYPE>` | Promote the pawn on cell FROM                  |
| `chess on <ID> show-moves <FROM>`     | Show where the figure on cell FROM can move to |
| `chess on <ID> remis offer`           | Offer remis to the other player                |
| `chess on <ID> remis accept`          | Accept the remis offer                         |
| `chess on <ID> surrender`             | Surrender the game                             |

## Feature List

### Library

| Number | Feature         | Implemented        | Tests              |
|--------|-----------------|--------------------|--------------------|
| 1      | Board           | :heavy_check_mark: | :heavy_check_mark: |
| 2      | Figure          | :heavy_check_mark: | :heavy_check_mark: |
| 3      | FigureType      | :heavy_check_mark: | :heavy_check_mark: |
| 4      | FigureColor     | :heavy_check_mark: | :heavy_check_mark: |
| 5      | Bishop          | :heavy_check_mark: | :heavy_check_mark: |
| 6      | King            | :heavy_check_mark: | :heavy_check_mark: |
| 7      | Knight          | :heavy_check_mark: | :heavy_check_mark: |
| 8      | Pawn            | :heavy_check_mark: | :heavy_check_mark: |
| 9      | Queen           | :heavy_check_mark: | :heavy_check_mark: |
| 10     | Rook            | :heavy_check_mark: | :heavy_check_mark: |
| 11     | Figure Movement | :heavy_check_mark: | :heavy_check_mark: |
| 12     | Cell            | :heavy_check_mark: | :heavy_check_mark: |
| 13     | CellDirection   | :heavy_check_mark: | :heavy_check_mark: |
| 14     | ChessGame       | :heavy_check_mark: | :heavy_check_mark: |
| 15     | Tournament      | :x:                | :x:                |

### CLI

| Number | Feature             | Implemented        | Tests              |
|--------|---------------------|--------------------|--------------------|
| 1      | Create Game         | :heavy_check_mark: | :heavy_check_mark: |
| 2      | Show Help           | :heavy_check_mark: | :heavy_check_mark: |
| 3      | Show Board          | :heavy_check_mark: | :heavy_check_mark: |
| 4      | Move Figure         | :heavy_check_mark: | :heavy_check_mark: |
| 5      | Promotion           | :heavy_check_mark: | :heavy_check_mark: |
| 6      | Show Possible Moves | :heavy_check_mark: | :heavy_check_mark: |
| 7      | Surrender           | :x:                | :x:                |
| 8      | Offer Remi          | :x:                | :x:                |
| 9      | Accept Remi         | :x:                | :x:                |
| 10     | Create Tournament   | :x:                | :x:                |

### Persistence

| Number | Feature         | Implemented        | Tests              |
|--------|-----------------|--------------------|--------------------|
| 1      | Player          | :heavy_check_mark: | :heavy_check_mark: |
| 2      | FEN Notation    | :heavy_check_mark: | :heavy_check_mark: |
| 3      | Save Game       | :heavy_check_mark: | :heavy_check_mark: |
| 4      | Load Game       | :heavy_check_mark: | :heavy_check_mark: |
| 5      | Save Tournament | :x:                | :x:                |
| 6      | Load Tournament | :x:                | :x:                |

## Additional Dependencies

| Number | Dependency Name  | Dependency Description                                                                                                                                                         | Why is it necessary?               |
|--------|------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------|
| 1      | ANSI Color codes | This is a way to add color to the console output. They are not supported on the default windows "cmd". Workaround: Running it in the console of IntelliJ works even on windows | Make the output easy to understand |

[maven]: https://maven.apache.org/

[just]: https://github.com/casey/just
