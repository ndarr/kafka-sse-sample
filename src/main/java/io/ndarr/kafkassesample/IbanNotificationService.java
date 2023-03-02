package io.ndarr.kafkassesample;


import io.ndarr.kafkassesample.model.ROIban;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.ConnectableFlux;
import reactor.kafka.receiver.KafkaReceiver;

@Service
@Slf4j
public class IbanNotificationService {
    private final KafkaReceiver<String, String > kafkaReceiver;

    private ConnectableFlux<ROIban> eventPublisher;

    public IbanNotificationService(KafkaReceiver<String, String> kafkaReceiver) {
        this.kafkaReceiver = kafkaReceiver;
    }

    @PostConstruct
    public void init() {
        log.info("Constructing eventPublisher!");
        this.eventPublisher = kafkaReceiver.receive()
            .map(receiverRecord -> deserializeMessage(receiverRecord.value()))
            .publish();
        this.eventPublisher.connect();
    }

    public ConnectableFlux<ROIban> getEventPublisher() {
        return this.eventPublisher;
    }

    private ROIban deserializeMessage(String message) {
        String[] split = message.split(":");
        log.info("User {}: {}", split[1], split[0]);
        return new ROIban(split[0], split[1]);
    }
}
