package com.ardetrick;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class MainTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Container
    @ServiceConnection
    static OracleContainer oracleContainer = new OracleContainer(DockerImageName.parse("gvenzl/oracle-xe:21-slim-faststart"))
            .waitingFor(new LogMessageWaitStrategy().withRegEx(".*Completed: ALTER DATABASE OPEN.*").withStartupTimeout(Duration.ofMinutes(10)))
            .withStartupTimeoutSeconds(60 * 10);

    @Container
    @ServiceConnection
    static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.7.25-management-alpine"));

    @Test
    void test() {
        rabbitTemplate.convertAndSend(Main.topicExchangeName, "foo.bar.baz", "from test");

        Awaitility.await()
                .atMost(5, TimeUnit.MINUTES)
                .until(writeIsFound());
    }

    private Callable<Boolean> writeIsFound() {
        return () -> {
            var rowCount = jdbcTemplate.query(
                    "select count(*) from foo where message = 'from test'",
                    resultSet -> resultSet.next() ? resultSet.getInt(1) : 0
            );
            return rowCount != null && rowCount == 1;
        };
    }

}
