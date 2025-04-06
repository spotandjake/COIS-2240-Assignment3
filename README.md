# Assignment 3

A simple java RentalSystem for COIS 2240.

# Getting started

This process is made a little easier if you have nix installed otherwise you need `gradle` and `java8` installed, it should be possible to build without gradle but no guarantees.

## Cloning

In order to use the project start by running:

```bash
git clone https://github.com/spotandjake/COIS-2240-Assignment3
```

## Building

If you have nix installed you can use the tasksystem however the project still builds fine without it you just need to run the commands normally and ensure `java 8` and `gradle` is installed:

### Nix:

```bash
task
```

### No Nix:

```bash
gradle build -x test # Build project - not tests
java -ea -cp build/classes/java/main VehicleRentalApp # Run the application
```

## Formatting

This only works with nix installed but isn't a neccessity for running the code, to format the project run:

```bash
task fmt
```

## Testing

In order to run the test suite run:

```bash
task test
```

or without nix run:

```bash
gradle test
```

## License

This code is currently unlicensed and held under copyright (only partially by me) as I do not maintain rights to the original source code, anything outside of the `src/` folder can be treated as MIT at your own risk.
