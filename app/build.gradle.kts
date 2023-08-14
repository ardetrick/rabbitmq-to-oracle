plugins {
    java
    id("org.springframework.boot") version "3.1.0"
    id("io.spring.dependency-management") version "1.1.0"
    id("io.freefair.lombok") version "8.2.2"
}

repositories {
    mavenCentral()
}

java.sourceCompatibility = JavaVersion.VERSION_20

dependencies {
    implementation("com.oracle.database.jdbc:ojdbc8:12.2.0.1")
    implementation("org.flywaydb:flyway-core")
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    developmentOnly("org.springframework.boot:spring-boot-docker-compose")

    testImplementation("org.awaitility:awaitility:3.0.0")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-testcontainers")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:oracle-xe")
    testImplementation("org.testcontainers:oracle-xe:1.18.3")
    testImplementation("org.testcontainers:rabbitmq")
    testImplementation("org.testcontainers:testcontainers:1.18.3")
}

tasks.test {
    useJUnitPlatform()
}
