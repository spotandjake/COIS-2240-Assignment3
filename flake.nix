{
  inputs = {
    flakelight.url = "github:nix-community/flakelight";
  };
  outputs = { flakelight, ... }:
    flakelight ./. ({ lib, ... }: {
      systems = lib.systems.flakeExposed;
      # Shell packages
      devShell = {
        packages = pkgs: [
          pkgs.jdk8 # Java 8 toolchain
          pkgs.google-java-format # Java formatter
          pkgs.go-task # Taskfile system
        ];
        shellHook = pkgs: ''
          export JAVA_HOME=${pkgs.jdk8}
          PATH="${pkgs.jdk8}/bin:$PATH"
        '';
      };
      # Formatting setup
      formatters = {
        "*.java" = "google-java-format --replace *.java";
      };
    });
}
