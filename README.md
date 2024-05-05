# CHESS GAME by Hyejin, Aylin, Vasja, Jonas and Theo!

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

## Abstract

This is a chess game which you can play on the cli. The game is stored as a csv file.

[TODO]: # (State most important features.)

[TODO]: # (State the most interesting problems you encountered during the project.)

## Commands overview

If `chess` does not work in your terminal, try `./chess` or `.\chess` instead.

| Command         | Description      | Example       |
|-----------------|------------------|---------------|
| `<ID>`          | Game Id (int)    | 123, 7        |
| `<FROM>` `<TO>` | Cell coordinates | a4, c7        |
| `<TYPE>`        | Type of figure   | queen, knight |

| Command              | Description                                         |
|----------------------|-----------------------------------------------------|
| `chess` `chess help` | Show the list of supported commands                 |
| `chess create <ID>`  | Create a new, fresh game (stored in game_\<ID>.csv) |

### In-Game Commands `chess on <ID> [...]`

| Command                               | Description                                    |
|---------------------------------------|------------------------------------------------|
| `chess on <ID> move <FROM> <TO>`      | Move the figure on FROM to the cell TO         |
| `chess on <ID> promote <FROM> <TYPE>` | Promote the pawn on cell FROM                  |
| `chess on <ID> show-moves <FROM>`     | Show where the figure on cell FROM can move to |

## Feature List

[TODO]: # (For each feature implemented, add a row to the table!)

| Number | Feature | Tests |
|--------|---------|-------|
| 1      | /       | /     |

## Additional Dependencies

| Number | Dependency Name  | Dependency Description                                                                                                                                                         | Why is it necessary?               |
|--------|------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|------------------------------------|
| 1      | ANSI Color codes | This is a way to add color to the console output. They are not supported on the default windows "cmd". Workaround: Running it in the console of IntelliJ works even on windows | Make the output easy to understand |

[maven]: https://maven.apache.org/

[just]: https://github.com/casey/just
