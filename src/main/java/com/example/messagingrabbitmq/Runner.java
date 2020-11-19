package com.example.messagingrabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Component
public class Runner implements CommandLineRunner {
    private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate){
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        BufferedReader  bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            String message = bufferedReader.readLine();
            System.out.println("Sending message...");
            rabbitTemplate.convertAndSend(MessagingRabbitApplication.fanoutExchangeName,
                    "foo.bar.baz", message);
            receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
        }
    }
}
