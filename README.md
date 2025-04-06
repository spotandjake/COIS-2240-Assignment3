# Assignment 3

# Getting started

## Cloning

In order to use the project start by running:
TODO:

```bash
git clone https://github.com/spotandjake/COIS-2240-Assignment3
```

## Building

If you have nix installed you can use the tasksystem however the project still builds fine without it you just need to run the commands normally and ensure `java 8` is installed:

### Nix:

```bash
task
```

### No Nix:

```bash
mkdir -p bin
javac -d bin/ -cp src src/VehicleRentalApp.java
java -ea -cp bin VehicleRentalApp
```

## Formatting

This only works with nix installed but isn't a neccessity for running the code, to format the project run:

```bash
task fmt
```

## License

This code is currently unlicensed and held under copyright (only partially by me) as I do not maintain rights to the original source code, anything outside of the `src/` folder can be treated as MIT at your own risk.
