package com.ardetrick;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class Receiver {

    private static final LocalDateTime START_DATE_TIME = LocalDateTime.ofInstant(Instant.now(), TimeZone.getDefault().toZoneId());

    JdbcTemplate jdbcTemplate;
    AtomicInteger atomicInteger = new AtomicInteger();

    public void receiveMessage(String message) {
        log.info("Received <{}>", message);
        save(message);
        log.info("Saved <{}>", message);
    }

    private void save(String message) {
        // Save the message, using a start date time to differentiate
        // different application starts if using a persistent datastore.
        jdbcTemplate.update("INSERT INTO foo (id, message) VALUES(?,?)",
                START_DATE_TIME + "-" + atomicInteger.getAndIncrement(),
                message
        );

        jdbcTemplate.query(
                "select id, message from foo",
                resultSet -> {
                    log.info("Found - ID: {} message: {}",
                            resultSet.getString(1),
                            resultSet.getString(2));
                }
        );
    }

}
