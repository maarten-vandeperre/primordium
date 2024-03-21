# primordium

./gradlew :application:configuration:quarkus-cli:quarkusDev --quarkus-args='--help test'

./gradlew :application:configuration:quarkus-cli:build -Dquarkus.package.type=uber-jar  
java -jar application/configuration/quarkus-cli/build/quarkus-cli-0.0.1-SNAPSHOT-runner.jar --help