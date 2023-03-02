package io.ndarr.kafkassesample.kafka;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.receiver.internals.ConsumerFactory;
import reactor.kafka.receiver.internals.DefaultKafkaReceiver;

@EnableKafka
@Configuration
@Slf4j
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    private static final String GROUP_ID = "iban";
    private static final String CLIENT_ID = "stupidClient";
    private static final String TOPIC = "iban";
    private static final String TIMEOUT = "60000";


    @Bean
    KafkaReceiver<String, String> kafkaReceiver() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        configProps.put(ConsumerConfig.CLIENT_ID_CONFIG, CLIENT_ID);

        configProps.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, TIMEOUT);
        configProps.put(ConsumerConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, TIMEOUT);
        configProps.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, TIMEOUT);
        configProps.put(ConsumerConfig.RECONNECT_BACKOFF_MAX_MS_CONFIG, TIMEOUT);
        configProps.put(ConsumerConfig.RECONNECT_BACKOFF_MS_CONFIG, TIMEOUT);
        configProps.put(ConsumerConfig.RETRY_BACKOFF_MS_CONFIG, TIMEOUT);

        return new DefaultKafkaReceiver<>(ConsumerFactory.INSTANCE,
            ReceiverOptions.<String, String>create(configProps).subscription(List.of(TOPIC))
        );
    }

}
