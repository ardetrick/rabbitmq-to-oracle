package com.ardetrick;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Component
class Runner implements CommandLineRunner {

    RabbitTemplate rabbitTemplate;

    @Override
    public void run(String... args) throws Exception {
        int i = 1;
        while (i < 10) {
            rabbitTemplate.convertAndSend(Main.topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ! " + i);
            i++;
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));
        }
    }

}
