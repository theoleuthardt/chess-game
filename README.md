# CHESS GAME in CLI

This repository contains a student project created for an ongoing lecture on object-oriented programming 
with Java at HWR Berlin (summer term 2024).

> :warning: This code is for educational purposes only. Do not rely on it!

## Local Development

This project uses [Apache Maven](https://maven.apache.org/) as build tool.

To build from your shell (without an additional local installation of Maven), ensure that `./mvnw` is executable:

```
chmod +x ./mvnw
```

I recommend not to dive into details about Maven at the beginning.
Instead, you can use [just](https://github.com/casey/just) to build the project.
It reads the repositories `justfile` which maps simplified commands to corresponding sensible Maven calls.

With _just_ installed, you can simply run this command to perform a build of this project and run all of its tests:

```
just build
```

## Abstract

[TODO]: # (Write a short description of your project.)
[TODO]: # (State most important features.)
[TODO]: # (State the most interesting problems you encountered during the project.)

## Feature List

[TODO]: # (For each feature implemented, add a row to the table!)

| Number | Feature | Tests |
|--------|---------|-------|
| 1      | /       | /     |


## Additional Dependencies

[TODO]: # (For each additional dependency your project requires- Add an additional row to the table!)

| Number | Dependency Name | Dependency Description | Why is it necessary? |
|--------|-----------------|------------------------|----------------------|
| 1      | /               | /                      | /                    |

## Instructions

### Multiple remote repositories

Your local repository should have a reference to both the fork (your own remote repository) 
and the original remote repository.
To configure your git remote repositories, use the `git remote` command set.

1. Clone your fork and go enter the repository.
```
git clone <fork-url>
cd <created-folder>
```
2. Now your fork is configured as primary remote repository (origin).
Next to origin, you should add the original repository as a second remote repository (upstream).
```
git remote add upstream <repository-url>
```
3. Verify that both remotes are configured correctly.
The following command should list both remotes: origin and upstream.
```
git remote -v
```
4. To fetch changes from all remote repositories, use:
```
git fetch --all
```
5. If there are interesting changes (in e.g. the `main` branch) to merge into your branch, use:
```
git pull upstream main
```
