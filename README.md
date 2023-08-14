# RabbitMQ To Oracle

## How To Run

- Requires Java 20
- Requires Docker

### MacOS

Summarized from [here](https://blog.jdriven.com/2022/07/running-oracle-xe-with-testcontainers-on-apple-silicon/).

1. Install Colima:

```shell
brew install colima
```

2. Run Colima:

```shell
colima start --network-address
```

4. Export environment variables:

```shell
export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock
export DOCKER_HOST="unix://${HOME}/.colima/docker.sock"
```

5. Run with gradle:

```shell
./gradlew :app:test --tests "org.example.MainTest.test" --info
```

### IntelliJ

When running in IntelliJ you must set the `DOCKER_HOST` environment variable in the test config.

Note: Unlike the above instructions for the shell, you cannot use `${HOME}` because IntellIJ will not evaluate it,
instead you must give the fully resolved path for the user running the test.

```shell
// For example, if running under the user ardetrick:
// DOCKER_HOST=unix:///Users/ardetrick/.colima/docker.sock
DOCKER_HOST=unix://${HOME}/.colima/docker.sock
```
