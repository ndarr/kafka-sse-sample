package io.ndarr.kafkassesample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class KafkaSseSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaSseSampleApplication.class, args);
    }

}
